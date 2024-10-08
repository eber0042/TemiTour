package com.temi.temiSDK

import android.service.notification.Condition
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.robotemi.sdk.voice.model.TtsVoice
import com.robotemi.sdk.constants.Gender
import com.robotemi.sdk.TtsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.Calendar
import javax.inject.Inject
import kotlin.random.Random
import kotlin.math.*
import kotlin.system.exitProcess

enum class State {
    TALK,          // Testing talking feature
    DISTANCE,      // Track distance of user
    ANGLE,
    CONSTRAINT_FOLLOW
}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val robotController: RobotController
) : ViewModel() {

    private val ttsStatus = robotController.ttsStatus // Current speech state
    private val detectionStatus = robotController.detectionStateChangedStatus
    private val detectionData = robotController.detectionDataChangedStatus
    private val movementStatus = robotController.movementStatusChangedStatus

    private val buffer = 100L
    private var currentState = State.ANGLE

    init {
        viewModelScope.launch {
            while(true) { // This while loop is used to refresh the state to allow for refresh
                when (currentState) {
                    State.TALK -> { // Creating a method for getting multiple lines of dialogue to work
                        // This method will allow play once per detection
                        detectionStatus.collect { detectionStatus: DetectionStateChangedStatus ->
                            Log.d("DetectStatus", detectionStatus.toString())
                            Log.i("DetectData", detectionData.value.distance.toString())

                            if (detectionStatus == DetectionStateChangedStatus.DETECTED) {
                                robotController.speak(
                                    "Hi there, I am Temi. What can I do for you today?",
                                    buffer
                                )
                                conditionGate { ttsStatus.value.status != TtsRequest.Status.COMPLETED }

                                robotController.speak("Wow, that is really interesting", buffer)
                                conditionGate { ttsStatus.value.status != TtsRequest.Status.COMPLETED }
                            }
                        }
                    }

                    State.DISTANCE -> { // Used to test the distance feature of Temi
                        // This method will allow play multiple per detection
                        var isDetected = false

                        // Launch a coroutine to monitor detectionStatus
                        val job = launch {
                            detectionStatus.collect { status ->
                                if (status == DetectionStateChangedStatus.DETECTED) {
                                    isDetected = true
                                    buffer()
                                }
                                else {
                                    isDetected = false
                                }
                            }
                        }

                        Log.d("DetectStatus", detectionStatus.toString())
                        Log.i("DetectData", detectionData.value.distance.toString())

                        if (isDetected) {
                            robotController.speak(
                                "You are " + detectionData.value.distance.toString() + " meters away from me",
                                buffer
                            )
                            conditionGate { ttsStatus.value.status != TtsRequest.Status.COMPLETED }
                        }
                        // Ensure to cancel the monitoring job if the loop finishes
                        job.cancel()
                    }
                    State.ANGLE -> {
                        // This method will allow play multiple per detection
                        var isDetected = false

                        // Launch a coroutine to monitor detectionStatus
                        val job = launch {
                            detectionStatus.collect { status ->
                                if (status == DetectionStateChangedStatus.DETECTED) {
                                    isDetected = true
                                    buffer()
                                }
                                else {
                                    isDetected = false
                                }
                            }
                        }

//                        Log.d("DetectStatus", detectionStatus.toString())
//                        Log.i("DetectData", detectionData.value.distance.toString())

                        if (isDetected) {
                            robotController.speak(
                                "You are " + detectionData.value.angle.toString() + " degrees from my normal",
                                buffer
                            )
                            conditionGate { ttsStatus.value.status != TtsRequest.Status.COMPLETED }
                        }
                        // Ensure to cancel the monitoring job if the loop finishes
                        conditionTimer({!isDetected}, time = 50)
                        job.cancel()
                    }

                    State.CONSTRAINT_FOLLOW -> {
                        Log.i("Angle", detectionData.value.angle.toString())
                        val turn = 10
//                        robotController.turnBy(turn, 1f, buffer)
//                        conditionGate { movementStatus.value.status !=  MovementStatus.COMPLETE}
//
//                        robotController.turnBy(-turn, 1f, buffer)
//                        conditionGate { movementStatus.value.status !=  MovementStatus.COMPLETE}
//                        if (detectionData.value.angle > 0.1) {
//                            robotController.turnBy(turn, 1f, buffer)
//                            Log.i("HELLOO", "hello")
//                            conditionGate { movementStatus.value.status !=  MovementStatus.COMPLETE}
//                        }
//                        else if (detectionData.value.angle < -0.1) {
//                            robotController.turnBy(turn, 1f, buffer)
//                            conditionGate { movementStatus.value.status !=  MovementStatus.COMPLETE}
//                        } else {
//                            // Do nothing
//                        }
                    }
                }
                buffer() // Add delay to ensure system work properly
            }
        }
    }

    //**************************System Function
    private suspend fun buffer() {
        // Increase buffer time to ensure enough delay between checks
        delay(this.buffer)
    }

    private suspend fun conditionTimer(trigger: () -> Boolean, time: Int) {
        if (!trigger()) {
            for (i in 1..time) {
                buffer()
                Log.i("Trigger", trigger().toString())
                if (trigger()) {
                    break
                }
            }
        }
    }

    private suspend fun conditionGate(trigger: () -> Boolean) {
        // Loop until the trigger condition returns false
        while (trigger()) {
//            Log.i("ConditionGate", "Trigger: ${trigger()}")
            buffer() // Pause between checks to prevent busy-waiting
        }
        Log.i("ConditionGate", "End")
    }
    //**************************Sequence Functions

}


//    private suspend fun conditionGate(trigger: () -> Boolean) {
//        // Infinite loop to keep checking until the trigger condition becomes false
//        var gate = true
//        while (gate) {
//            // Collect from ttsStatus and wait for a new value
//            ttsStatus.collect { ttsState ->
//                // Check the trigger condition
//                if (!trigger()) {
//                    // If the trigger condition is false, break the loop
//                    gate = false
//                }
//            }
//        }
//    }

//****************************************OLD USELESS CODE
//    enum class Trigger {
//        IDLE,          // Testing talking feature
//        DETECTION_STATE      // Track distance of user
//    }
//    private var trigger = Trigger.IDLE
//viewModelScope.launch { // Used to create forced reset
//    detectionStatus.collect { currentStatus: DetectionStateChangedStatus ->
//        if (currentStatus == DetectionStateChangedStatus.DETECTED) {
//            handleDetectionReset(
//                emitTrigger = { collectedTrigger ->
//                    // Handle the emitted trigger, e.g., pass it to another system
//                    trigger = collectedTrigger
//                },
//                condition = true, // Pass in the condition for the check
//                timer = 1000       // Optional timer parameter
//            )
//        }
//    }
//}
//private suspend fun handleDetectionReset(emitTrigger: (Trigger) -> Unit, condition: Boolean, timer: Int = 1) {
//    // Flag to check if detection state remains the same
//    var isStillDetected = true
//
//    // Launch a coroutine to monitor detectionStatus
//    val job = viewModelScope.launch {
//        detectionStatus.collect { status ->
//            if (status != DetectionStateChangedStatus.DETECTED) {
//                isStillDetected = false
//                cancel() // Cancel the monitoring coroutine if the status changes
//            }
//        }
//    }
//
//    while (condition) {
//        buffer() // Assuming buffer() is a suspend function
//
//        // Check the flag after the delay
//        if (!isStillDetected) {
//            break // Exit the loop if the status changed
//        }
//    }
//
//    for (i in 1..timer) {
//        buffer() // Assuming buffer() is a suspend function
//
//        // Check the flag after the delay
//        if (!isStillDetected) {
//            break // Exit the loop if the status changed
//        }
//    }
//
//    // Ensure to cancel the monitoring job if the loop finishes
//    job.cancel()
//
//    if (isStillDetected) {
//        emitTrigger(Trigger.DETECTION_STATE)
//        Log.i("ForcedRefresh", "DetectionStatus has been forced refreshed.")
//    } else {
//        Log.i("ForcedRefresh", "DetectionStatus changed before completion.")
//    }
//}