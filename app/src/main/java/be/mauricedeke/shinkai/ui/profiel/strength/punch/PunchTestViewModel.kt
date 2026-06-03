package be.mauricedeke.shinkai.ui.profiel.strength.punch

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import be.mauricedeke.shinkai.domain.model.beltColorFromPunch
import be.mauricedeke.shinkai.domain.usecase.SaveStrengthResultUseCase
import be.mauricedeke.shinkai.ui.profiel.strength.MeasurementPhase
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.math.roundToInt
import kotlin.math.sqrt

@HiltViewModel
class PunchTestViewModel @Inject constructor(
    @ApplicationContext context: Context,
    private val saveStrengthResult: SaveStrengthResultUseCase
) : ViewModel() {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val _uiState = MutableStateFlow(PunchTestUiState())
    val uiState: StateFlow<PunchTestUiState> = _uiState

    private var peakImpact = 0f
    private var measurementJob: Job? = null

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val magnitude = sqrt(x * x + y * y + z * z)
            val impact = (magnitude - SensorManager.GRAVITY_EARTH).coerceAtLeast(0f)
            if (impact > peakImpact) {
                peakImpact = impact
                _uiState.update { it.copy(score = (impact * 5).roundToInt()) }
            }
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    fun onStart() {
        if (_uiState.value.phase == MeasurementPhase.MEASURING) return
        if (accelerometer == null) return
        peakImpact = 0f
        _uiState.update { it.copy(phase = MeasurementPhase.MEASURING, score = 0, resultBelt = null, progress = 0f) }
        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST)

        measurementJob = viewModelScope.launch {
            val durationMs = 3000L
            val startTime = System.currentTimeMillis()
            while (isActive) {
                val elapsed = System.currentTimeMillis() - startTime
                _uiState.update { it.copy(progress = (elapsed / durationMs.toFloat()).coerceIn(0f, 1f)) }
                if (elapsed >= durationMs) break
                delay(50)
            }
            sensorManager.unregisterListener(listener)
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
        sensorManager.unregisterListener(listener)
        _uiState.update { it.copy(phase = MeasurementPhase.IDLE, score = 0, resultBelt = null, progress = 0f) }
    }

    override fun onCleared() {
        super.onCleared()
        measurementJob?.cancel()
        sensorManager.unregisterListener(listener)
    }
}
