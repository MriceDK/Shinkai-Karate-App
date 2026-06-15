package be.mauricedeke.shinkai.ui.profiel.strength.kiai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.model.beltColorFromDb
import be.mauricedeke.shinkai.domain.repository.MicrophoneRepository
import be.mauricedeke.shinkai.domain.usecase.SubmitKiaiTestUseCase
import be.mauricedeke.shinkai.ui.profiel.strength.MeasurementPhase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.log10
import kotlin.math.roundToInt

@HiltViewModel
class KiaiTestViewModel @Inject constructor(
    private val microphoneRepository: MicrophoneRepository,
    private val submitKiaiTest: SubmitKiaiTestUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(KiaiTestUiState())
    val uiState: StateFlow<KiaiTestUiState> = _uiState

    private var measurementJob: Job? = null

    fun onStart() {
        if (_uiState.value.phase == MeasurementPhase.MEASURING) return

        val durationMs = 3000L
        val startTime = System.currentTimeMillis()
        var sessionPeakRms = 0

        _uiState.update {
            it.copy(
                phase = MeasurementPhase.MEASURING,
                currentDb = 0,
                peakDb = 0,
                resultBelt = null,
                progress = 0f
            )
        }
        microphoneRepository.start()

        measurementJob = viewModelScope.launch {
            microphoneRepository.amplitude
                .takeWhile { System.currentTimeMillis() - startTime < durationMs }
                .collect { rms ->
                    val elapsed = System.currentTimeMillis() - startTime
                    if (rms > sessionPeakRms) sessionPeakRms = rms
                    val db = amplitudeToDb(rms).roundToInt()
                    val peakDb = amplitudeToDb(sessionPeakRms).roundToInt()
                    _uiState.update {
                        it.copy(
                            currentDb = db,
                            peakDb = peakDb,
                            progress = (elapsed / durationMs.toFloat()).coerceIn(0f, 1f)
                        )
                    }
                }

            microphoneRepository.stop()
            val finalPeakDb = amplitudeToDb(sessionPeakRms).roundToInt()
            _uiState.update {
                it.copy(
                    phase = MeasurementPhase.DONE,
                    peakDb = finalPeakDb,
                    resultBelt = beltColorFromDb(finalPeakDb),
                    progress = 1f
                )
            }
            val result = withContext(Dispatchers.IO) { submitKiaiTest(finalPeakDb) }
            val newBestDb = maxOf(_uiState.value.bestDb, finalPeakDb)
            _uiState.update {
                it.copy(
                    bestDb = newBestDb,
                    resultBelt = result?.beltColor ?: beltColorFromDb(finalPeakDb),
                    bestBelt = result?.beltColor ?: beltColorFromDb(newBestDb)
                )
            }
        }
    }

    fun onReset() {
        measurementJob?.cancel()
        microphoneRepository.stop()
        _uiState.update {
            it.copy(
                phase = MeasurementPhase.IDLE,
                currentDb = 0,
                peakDb = 0,
                resultBelt = null,
                progress = 0f
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        measurementJob?.cancel()
        microphoneRepository.stop()
    }

    private fun amplitudeToDb(rms: Int): Double {
        if (rms < 1) return 0.0
        return (120.0 + 20.0 * log10(rms / 32767.0)).coerceIn(0.0, 130.0)
    }
}
