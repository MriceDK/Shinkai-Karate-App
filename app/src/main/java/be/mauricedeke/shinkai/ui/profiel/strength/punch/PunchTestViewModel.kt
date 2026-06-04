package be.mauricedeke.shinkai.ui.profiel.strength.punch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.model.beltColorFromPunch
import be.mauricedeke.shinkai.domain.repository.AccelerometerRepository
import be.mauricedeke.shinkai.domain.usecase.SaveStrengthResultUseCase
import be.mauricedeke.shinkai.ui.profiel.strength.MeasurementPhase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class PunchTestViewModel @Inject constructor(
    private val accelerometerRepository: AccelerometerRepository,
    private val saveStrengthResult: SaveStrengthResultUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(PunchTestUiState())
    val uiState: StateFlow<PunchTestUiState> = _uiState

    private var measurementJob: Job? = null

    fun onStart() {
        if (_uiState.value.phase == MeasurementPhase.MEASURING) return
        val durationMs = 3000L
        val startTime = System.currentTimeMillis()
        var peakImpact = 0f

        _uiState.update { it.copy(phase = MeasurementPhase.MEASURING, score = 0, resultBelt = null, progress = 0f) }
        accelerometerRepository.start()

        measurementJob = viewModelScope.launch {
            accelerometerRepository.impact
                .takeWhile { System.currentTimeMillis() - startTime < durationMs }
                .collect { impact ->
                    val elapsed = System.currentTimeMillis() - startTime
                    val progress = (elapsed / durationMs.toFloat()).coerceIn(0f, 1f)
                    if (impact > peakImpact) {
                        peakImpact = impact
                        _uiState.update { it.copy(score = (impact * 5).roundToInt(), progress = progress) }
                    } else {
                        _uiState.update { it.copy(progress = progress) }
                    }
                }

            accelerometerRepository.stop()
            val finalScore = _uiState.value.score
            val newBestScore = maxOf(_uiState.value.bestScore, finalScore)
            _uiState.update {
                it.copy(
                    phase = MeasurementPhase.DONE,
                    progress = 1f,
                    bestScore = newBestScore,
                    resultBelt = beltColorFromPunch(finalScore),
                    bestBelt = beltColorFromPunch(newBestScore)
                )
            }
            withContext(Dispatchers.IO) { saveStrengthResult("Punching Strength", newBestScore) }
        }
    }

    fun onReset() {
        measurementJob?.cancel()
        accelerometerRepository.stop()
        _uiState.update { it.copy(phase = MeasurementPhase.IDLE, score = 0, resultBelt = null, progress = 0f) }
    }

    override fun onCleared() {
        super.onCleared()
        measurementJob?.cancel()
        accelerometerRepository.stop()
    }
}
