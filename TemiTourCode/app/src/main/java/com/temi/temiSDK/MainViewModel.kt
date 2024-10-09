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
    CONSTRAINT_FOLLOW,
    NULL
}

enum class Direction {
    LEFT,
    RIGHT,
    DEFAULT
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
    private var currentState = State.CONSTRAINT_FOLLOW
    private val defaultAngle = 180 + round(Math.toDegrees(robotController.getPositionYaw().toDouble())) // Default angle temi will go to.
    private var userRelativeDirection = Direction.DEFAULT

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
                                conditionGate ({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })

                                robotController.speak("Wow, that is really interesting", buffer)
                                conditionGate ({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
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
                            conditionGate ({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
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
                            conditionGate ({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
                        }
                        // Ensure to cancel the monitoring job if the loop finishes
                        conditionTimer({!isDetected}, time = 50)
                        job.cancel()
                    }
                    State.CONSTRAINT_FOLLOW -> {
                        val currentAngle = 180 + round(Math.toDegrees(robotController.getPositionYaw().toDouble()))
                        val userRelativeAngle = round(Math.toDegrees(detectionData.value.angle))/1.70
                        val turnAngle = (userRelativeAngle).toInt()

                        Log.i("currentAngle", currentAngle.toString())
                        Log.i("userRelativeAngle", userRelativeAngle.toString())
                        Log.i("new", turnAngle.toString())

                        // Use this to determine which direction the user was lost in
                        when {
                            userRelativeAngle > 0 -> {
                                userRelativeDirection = Direction.LEFT
                            }
                            userRelativeAngle < 0 -> {
                                userRelativeDirection = Direction.RIGHT
                            }
                            else -> {
                                // Do nothing
                            }
                        }

                        // This method will allow play multiple per detection
                        var isDetected = false
                        var isLost = false

                        // Launch a coroutine to monitor detectionStatus
                        val job = launch {
                            detectionStatus.collect { status ->
                                when (status) {
                                    DetectionStateChangedStatus.DETECTED -> {
                                        isDetected = true
                                        isLost = false
                                        buffer()
                                    }
                                    DetectionStateChangedStatus.LOST -> {
                                        isDetected = false
                                        isLost = true
                                        buffer()
                                    }
                                    else -> {
                                        isDetected = false
                                        isLost = false
                                        buffer()
                                    }
                                }
                            }
                        }

                        Log.i("Movement", movementStatus.value.status.toString())

                        if (defaultAngle != currentAngle) {
                            robotController.turnBy((defaultAngle - currentAngle).toInt(), 1f, buffer)
                            conditionGate ({ movementStatus.value.status !in listOf(MovementStatus.COMPLETE,MovementStatus.ABORT) }, movementStatus.value.status.toString())
                        }

//                        if (isDetected && (turnAngle > 6 || turnAngle < -6)) {
//                            robotController.turnBy(turnAngle, 1f, buffer)
//                            // The conditionGate makes this system more janky, not good to use in this case
////                            conditionGate ({ movementStatus.value.status !in listOf(MovementStatus.COMPLETE,MovementStatus.ABORT) })
//                        } else if (isLost) {
//                            when (userRelativeDirection) {
//                                Direction.LEFT  -> {
//                                    robotController.turnBy(90, 0.25f, buffer)
//                                    userRelativeDirection = Direction.DEFAULT
//                                }
//                                Direction.RIGHT -> {
//                                    robotController.turnBy(-90, 0.25f, buffer)
//                                    userRelativeDirection = Direction.DEFAULT
//                                }
//                                else -> {
//                                    // Do nothing
//                                }
//                            }
//                        }
                        // Ensure to cancel the monitoring job if the loop finishes
                        job.cancel()
                    }
                    State.NULL -> {
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

                        Log.i("Movement", movementStatus.value.status.toString())

                        if (isDetected) {
                        }
                        else {
                            robotController.turnBy(50, 1f, buffer)
                            conditionGate ({ movementStatus.value.status !in listOf(MovementStatus.COMPLETE,MovementStatus.ABORT) }, movementStatus.value.status.toString())
                            robotController.turnBy(-50, 1f, buffer)
                            conditionGate ({ movementStatus.value.status !in listOf(MovementStatus.COMPLETE,MovementStatus.ABORT) }, movementStatus.value.status.toString())
                        }
                        // Ensure to cancel the monitoring job if the loop finishes
                        job.cancel()
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

    private suspend fun conditionGate(trigger: () -> Boolean, log: String = "Null") {
        // Loop until the trigger condition returns false
        while (trigger()) {
            Log.i("ConditionGate", "Trigger: $log")
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