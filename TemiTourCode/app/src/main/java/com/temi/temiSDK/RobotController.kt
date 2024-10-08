package com.temi.temiSDK

import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import com.robotemi.sdk.Robot
import com.robotemi.sdk.listeners.OnRobotReadyListener
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener
import com.robotemi.sdk.listeners.OnDetectionDataChangedListener
import com.robotemi.sdk.TtsRequest
import com.robotemi.sdk.model.DetectionData
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Singleton

data class TtsStatus(val status: TtsRequest.Status)

enum class DetectionStateChangedStatus(val state: Int) { // Why is it like this?
    DETECTED(state = 2),
    LOST(state = 1),
    IDLE(state = 0);

    companion object {
        fun fromState(state: Int): DetectionStateChangedStatus? = entries.find { it.state == state }
    }
}

data class DetectionDataChangedStatus( val angle: Double, val distance: Double)

@Module
@InstallIn(SingletonComponent::class)
object RobotModule {
    @Provides
    @Singleton
    fun provideRobotController() = RobotController()
}

class RobotController():
    OnRobotReadyListener,
    OnDetectionStateChangedListener,
    Robot.TtsListener,
    OnDetectionDataChangedListener
{
    private val robot = Robot.getInstance() //This is needed to reference the data coming from Temi

    // Setting up the Stateflows here
    private val _ttsStatus = MutableStateFlow( TtsStatus(status = TtsRequest.Status.PENDING) )
    val ttsStatus = _ttsStatus.asStateFlow()

    private val _detectionStateChangedStatus = MutableStateFlow(DetectionStateChangedStatus.IDLE)
    val detectionStateChangedStatus = _detectionStateChangedStatus.asStateFlow()

    private val _detectionDataChangedStatus = MutableStateFlow(DetectionDataChangedStatus(angle = 0.0, distance = 0.0))
    val detectionDataChangedStatus = _detectionDataChangedStatus.asStateFlow() // This can include talking state as well

    init {
        robot.addOnRobotReadyListener(this)
        robot.addTtsListener(this)
        robot.addOnDetectionStateChangedListener((this))
        robot.addOnDetectionDataChangedListener(this)
    }
    //********************************* General Functions
    suspend fun speak(speech: String, buffer: Long) {
        delay(buffer)
        var request = TtsRequest.create(
            speech = speech,
            isShowOnConversationLayer = true,
            showAnimationOnly = true,
        ) // Need to create TtsRequest
        robot.speak(request)
        delay(buffer)
    }

    //********************************* Override is below
    /**
     * Called when connection with robot was established.
     *
     * @param isReady `true` when connection is open. `false` otherwise.
     */
    override fun onRobotReady(isReady: Boolean) {
        if (!isReady) return
        robot.setDetectionModeOn(on = true, distance = 2.0f) // Set how far it can detect stuff
        robot.setKioskModeOn(on = false)
        Log.d("DetectOn", robot.detectionModeOn.toString()) // This line does not seem to have the intended effect
    }

    override fun onTtsStatusChanged(ttsRequest: TtsRequest) {
        Log.i("onTtsStatusChanged", "status: ${ttsRequest.status}")
        _ttsStatus.update {
            TtsStatus(status = ttsRequest.status)
        }
    }

    override fun onDetectionStateChanged(state: Int) {
        _detectionStateChangedStatus.update {
            DetectionStateChangedStatus.fromState(state = state) ?: return@update it
        }
    }

    override fun onDetectionDataChanged(detectionData: DetectionData) {
        _detectionDataChangedStatus.update {
            DetectionDataChangedStatus(angle = detectionData.angle, distance = detectionData.distance)
        }
    }

}
