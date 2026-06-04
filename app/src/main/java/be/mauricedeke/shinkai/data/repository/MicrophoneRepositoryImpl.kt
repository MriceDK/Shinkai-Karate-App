package be.mauricedeke.shinkai.data.repository

import android.annotation.SuppressLint
import android.media.AudioFormat
import android.media.AudioRecord
import android.media.MediaRecorder
import android.media.audiofx.AcousticEchoCanceler
import android.media.audiofx.AutomaticGainControl
import android.media.audiofx.NoiseSuppressor
import be.mauricedeke.shinkai.domain.repository.MicrophoneRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

@Singleton
class MicrophoneRepositoryImpl @Inject constructor() : MicrophoneRepository {

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val _amplitude = MutableSharedFlow<Int>(extraBufferCapacity = 128)
    override val amplitude: Flow<Int> = _amplitude.asSharedFlow()

    private var audioRecord: AudioRecord? = null
    private var recordingJob: Job? = null

    @SuppressLint("MissingPermission")
    override fun start() {
        if (recordingJob?.isActive == true) return

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
        recorder.startRecording()

        recordingJob = scope.launch {
            val buffer = ShortArray(bufferSize)
            while (isActive) {
                val read = recorder.read(buffer, 0, bufferSize)
                if (read > 0) {
                    val rms = sqrt(
                        buffer.take(read).map { it.toDouble() * it.toDouble() }.average()
                    ).toInt()
                    _amplitude.tryEmit(rms)
                }
            }
        }
    }

    override fun stop() {
        recordingJob?.cancel()
        recordingJob = null
        audioRecord?.stop()
        audioRecord?.release()
        audioRecord = null
    }
}
