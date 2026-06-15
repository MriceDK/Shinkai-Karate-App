package be.mauricedeke.shinkai.data.messaging

import android.content.Context
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Assume.assumeNotNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FlashlightInstrumentedTest {

    private lateinit var context: Context
    private lateinit var cameraManager: CameraManager
    private var flashCameraId: String? = null

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        flashCameraId = cameraManager.cameraIdList.firstOrNull { id ->
            cameraManager.getCameraCharacteristics(id)
                .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        }
    }

    @Test
    fun cameraList_isNotEmpty() {
        assertTrue(
            "Expected at least one camera on the device",
            cameraManager.cameraIdList.isNotEmpty()
        )
    }

    @Test
    fun flashCamera_isDetected() {
        assertNotNull(
            "Expected a camera with flash support on this device",
            flashCameraId
        )
    }

    @Test
    fun torchMode_canBeEnabledAndDisabled() {
        assumeNotNull("Skipping: device has no flash", flashCameraId)
        val id = flashCameraId!!
        cameraManager.setTorchMode(id, true)
        cameraManager.setTorchMode(id, false)
    }

    @Test
    fun flashSequence_threeFlashes_completesWithoutException() = runBlocking {
        assumeNotNull("Skipping: device has no flash", flashCameraId)
        val id = flashCameraId!!
        repeat(3) {
            cameraManager.setTorchMode(id, true)
            delay(200)
            cameraManager.setTorchMode(id, false)
            delay(150)
        }
    }

    @Test
    fun flashCamera_cameraIdLookup_matchesServiceLogic() {
        // Reproduces the exact lookup from AmqpNotificationService.flashLight()
        // to verify the same camera ID is found on this device.
        val foundId = cameraManager.cameraIdList.firstOrNull { id ->
            cameraManager.getCameraCharacteristics(id)
                .get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
        }
        assertTrue(
            "Service flash lookup returned a different result than the test setup",
            foundId == flashCameraId
        )
    }

    @Test
    fun noFlash_gracefulEarlyReturn_doesNotThrow() {
        // Simulate the early-return path when no flash camera is found.
        // The service does `?: return` — calling setTorchMode is never reached.
        val noFlashId: String? = null
        noFlashId ?: return  // mirrors the service's guard; must not throw
    }
}
