package be.mauricedeke.shinkai.ui.profiel.strength.kiai

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.audiofx.AcousticEchoCanceler
import android.media.audiofx.AutomaticGainControl
import android.media.audiofx.NoiseSuppressor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.model.beltColorFromDb
import be.mauricedeke.shinkai.domain.usecase.SaveStrengthResultUseCase
import be.mauricedeke.shinkai.ui.profiel.strength.MeasurementPhase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.log10
import kotlin.math.roundToInt
import kotlin.math.sqrt

@HiltViewModel
class KiaiTestViewModel @Inject constructor(
    private val saveStrengthResult: SaveStrengthResultUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(KiaiTestUiState())
    val uiState: StateFlow<KiaiTestUiState> = _uiState

    private var audioRecord: AudioRecord? = null
    private var measurementJob: Job? = null

    @SuppressLint("MissingPermission")
    fun onStart() {
        if (_uiState.value.phase == MeasurementPhase.MEASURING) return

        val sampleRate = 44100
        val bufferSize = maxOf(
            AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT),
            2048
        )

        val recorder = try {
            AudioRecord(
                MediaRecorder.AudioSource.UNPROCESSED,
                sampleRate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
                bufferSize
            )
        } catch (e: SecurityException) { return }

        if (NoiseSuppressor.isAvailable()) NoiseSuppressor.create(recorder.audioSessionId)?.enabled = false
        if (AutomaticGainControl.isAvailable()) AutomaticGainControl.create(recorder.audioSessionId)?.enabled = false
        if (AcousticEchoCanceler.isAvailable()) AcousticEchoCanceler.create(recorder.audioSessionId)?.enabled = false

        if (recorder.state != AudioRecord.STATE_INITIALIZED) {
            recorder.release()
            return
        }

        audioRecord = recorder
        _uiState.update { it.copy(phase = MeasurementPhase.MEASURING, currentDb = 0, peakDb = 0, resultBelt = null, progress = 0f) }
        recorder.startRecording()

        measurementJob = viewModelScope.launch(Dispatchers.IO) {
            val buffer = ShortArray(bufferSize)
            val durationMs = 3000L
            val startTime = System.currentTimeMillis()
            var sessionPeakRms = 0

            while (isActive) {
                val elapsed = System.currentTimeMillis() - startTime
                val progress = (elapsed / durationMs.toFloat()).coerceIn(0f, 1f)

                val read = recorder.read(buffer, 0, bufferSize)
                if (read > 0) {
                    val rms = sqrt(buffer.take(read)
                        .map { it.toDouble() * it.toDouble() }
                        .average()
                    ).toInt()

                    val db = amplitudeToDb(rms).roundToInt()
                    if (rms > sessionPeakRms) sessionPeakRms = rms
                    val peakDb = amplitudeToDb(sessionPeakRms).roundToInt()

                    withContext(Dispatchers.Main) {
                        _uiState.update { it.copy(currentDb = db, peakDb = peakDb, progress = progress) }
                    }
                }

                if (elapsed >= durationMs) break
            }

            recorder.stop()
            recorder.release()
            audioRecord = null

            val finalPeakDb = amplitudeToDb(sessionPeakRms).roundToInt()
            val newBestDb = maxOf(_uiState.value.bestDb, finalPeakDb)
            val resultBelt = beltColorFromDb(finalPeakDb)

            withContext(Dispatchers.Main) {
                _uiState.update {
                    it.copy(
                        phase = MeasurementPhase.DONE,
                        peakDb = finalPeakDb,
                        bestDb = newBestDb,
                        resultBelt = resultBelt,
                        bestBelt = beltColorFromDb(newBestDb),
                        progress = 1f
                    )
                }
            }
            saveStrengthResult("Kiai Strength", newBestDb)
        }
    }

    fun onReset() {
        measurementJob?.cancel()
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
        _uiState.update { it.copy(phase = MeasurementPhase.IDLE, currentDb = 0, peakDb = 0, resultBelt = null, progress = 0f) }
    }

    override fun onCleared() {
        super.onCleared()
        measurementJob?.cancel()
        audioRecord?.stop()
        audioRecord?.release()
    }

    private fun amplitudeToDb(rms: Int): Double {
        if (rms < 1) return 0.0
        return (120.0 + 20.0 * log10(rms / 32767.0)).coerceIn(0.0, 130.0)
    }
}
