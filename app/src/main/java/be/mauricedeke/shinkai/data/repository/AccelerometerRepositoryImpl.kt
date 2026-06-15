package be.mauricedeke.shinkai.data.repository

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import be.mauricedeke.shinkai.domain.repository.AccelerometerRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

@Singleton
class AccelerometerRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : AccelerometerRepository {

    private val sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

    private val _impact = MutableSharedFlow<Float>(extraBufferCapacity = 128)
    override val impact: Flow<Float> = _impact.asSharedFlow()

    private val listener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent) {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val magnitude = sqrt(x * x + y * y + z * z)
            val impact = (magnitude - SensorManager.GRAVITY_EARTH).coerceAtLeast(0f)
            _impact.tryEmit(impact)
        }

        override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {}
    }

    override fun start() {
        if (accelerometer == null) return
        sensorManager.registerListener(listener, accelerometer, SensorManager.SENSOR_DELAY_FASTEST)
    }

    override fun stop() {
        sensorManager.unregisterListener(listener)
    }
}
