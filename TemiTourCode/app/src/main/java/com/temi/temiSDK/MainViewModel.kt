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
    TEST_MOVEMENT,
    DETECTION_LOGIC,
    NULL
}

enum class YDirection {
    FAR,
    MIDRANGE,
    CLOSE,
    MISSING
}

enum class XDirection {
    LEFT,
    RIGHT,
    MIDDLE,
    GONE

}



enum class YMovement {
    CLOSER,
    FURTHER,
    NOWHERE
}

enum class XMovement {
    LEFTER,
    RIGHTER,
    NOWHERE
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
    private var currentState = State.NULL // Current state of the system
    private val defaultAngle =
        120.0 // 180 + round(Math.toDegrees(robotController.getPositionYaw().toDouble())) // Default angle temi will go to.
    private var userRelativeDirection =
        XDirection.GONE // Used for checking direction user was lost

    private var previousUserAngle = 0.0
    private var currentUserAngle = 0.0
    private var xPosition = XDirection.GONE
    private var xMotion = XMovement.NOWHERE

    private var previousUserDistance = 0.0
    private var currentUserDistance = 0.0
    private var yPosition = YDirection.MISSING
    private var yMotion = YMovement.NOWHERE

    init {
        viewModelScope.launch {
            while (true) { // This while loop is used to refresh the state to allow for refresh
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
                                conditionGate({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })

                                robotController.speak("Wow, that is really interesting", buffer)
                                conditionGate({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
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
                                } else {
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
                            conditionGate({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
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
                                } else {
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
                            conditionGate({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
                        }
                        // Ensure to cancel the monitoring job if the loop finishes
                        conditionTimer({ !isDetected }, time = 50)
                        job.cancel()
                    }

                    State.CONSTRAINT_FOLLOW -> {
                        val currentAngle =
                            180 + round(Math.toDegrees(robotController.getPositionYaw().toDouble()))
                        val userRelativeAngle =
                            round(Math.toDegrees(detectionData.value.angle)) / 1.70
                        val turnAngle = (userRelativeAngle).toInt()

                        Log.i("currentAngle", currentAngle.toString())
                        Log.i("userRelativeAngle", userRelativeAngle.toString())
                        Log.i("new", turnAngle.toString())

                        // Use this to determine which direction the user was lost in
                        when {
                            userRelativeAngle > 0 -> {
                                userRelativeDirection = XDirection.LEFT
                            }

                            userRelativeAngle < 0 -> {
                                userRelativeDirection = XDirection.RIGHT
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

                        if (isDetected && (turnAngle > 10 || turnAngle < -10) && (currentAngle < defaultAngle + 90 && currentAngle > defaultAngle - 90)) {
                            robotController.turnBy(turnAngle, 1f, buffer)
                            // The conditionGate makes this system more janky, not good to use in this case
//                            conditionGate ({ movementStatus.value.status !in listOf(MovementStatus.COMPLETE,MovementStatus.ABORT) })
                        } else if (isLost && (currentAngle < defaultAngle + 90 && currentAngle > defaultAngle - 90)) {
                            when (userRelativeDirection) {
                                XDirection.LEFT -> {
                                    robotController.turnBy(90, 0.1f, buffer)
                                    userRelativeDirection = XDirection.GONE
                                }

                                XDirection.RIGHT -> {
                                    robotController.turnBy(-90, 0.1f, buffer)
                                    userRelativeDirection = XDirection.GONE
                                }

                                else -> {
                                    // Do nothing
                                }
                            }
                        } else if (!isDetected && !isLost) {
                            if (defaultAngle != currentAngle) {
                                robotController.turnBy(
                                    getDirectedAngle(
                                        defaultAngle,
                                        currentAngle
                                    ).toInt(), 1f, buffer
                                )
                                conditionGate({
                                    movementStatus.value.status !in listOf(
                                        MovementStatus.COMPLETE,
                                        MovementStatus.ABORT
                                    )
                                }, movementStatus.value.status.toString())
                            }
                        }
                        // Ensure to cancel the monitoring job if the loop finishes
                        job.cancel()
                    }

                    State.TEST_MOVEMENT -> {
                        // This method will allow play multiple per detection
                        var isDetected = false

                        // Launch a coroutine to monitor detectionStatus
                        val job = launch {
                            detectionStatus.collect { status ->
                                if (status == DetectionStateChangedStatus.DETECTED) {
                                    isDetected = true
                                    buffer()
                                } else {
                                    isDetected = false
                                }
                            }
                        }

                        Log.i("Movement", movementStatus.value.status.toString())

                        if (isDetected) {
                        } else {
                            robotController.turnBy(50, 1f, buffer)
                            conditionGate({
                                movementStatus.value.status !in listOf(
                                    MovementStatus.COMPLETE,
                                    MovementStatus.ABORT
                                )
                            }, movementStatus.value.status.toString())
                            robotController.turnBy(-50, 1f, buffer)
                            conditionGate({
                                movementStatus.value.status !in listOf(
                                    MovementStatus.COMPLETE,
                                    MovementStatus.ABORT
                                )
                            }, movementStatus.value.status.toString())
                        }
                        // Ensure to cancel the monitoring job if the loop finishes
                        job.cancel()
                    }

                    State.DETECTION_LOGIC -> {

                        // This method will allow play multiple per detection
                        var isDetected = false

                        // Launch a coroutine to monitor detectionStatus
                        val job = launch {
                            detectionStatus.collect { status ->
                                if (status == DetectionStateChangedStatus.DETECTED) {
                                    isDetected = true
                                    buffer()
                                } else {
                                    isDetected = false
                                }
                            }
                        }

                        previousUserAngle = currentUserAngle
                        previousUserDistance = currentUserDistance
                        delay(500L)
                        currentUserAngle = detectionData.value.angle
                        currentUserDistance = detectionData.value.distance


//                        Log.i("currentUserAngle", (currentUserDistance).toString())
//                        Log.i("previousUserAngle", (previousUserDistance).toString())
//                        Log.i("Direction", (currentUserDistance - previousUserDistance).toString())

                        if (isDetected) { //&& previousUserDistance != 0.0 && previousUserDistance == currentUserDistance) {
                            // logic for close or far position
                            when {
                                // System for detecting how far
                            }
//                                currentUserDistance < 0.75 -> {
//                                    robotController.speak("You are close", buffer)
//                                    conditionGate ({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
//                                }
//                                currentUserDistance < 1.35 -> {
//                                    robotController.speak("You are midrange", buffer)
//                                    conditionGate ({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
//                                }
//                                else -> {
//                                    robotController.speak("You are far", buffer)
//                                    conditionGate ({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
//                                }
//                            }

                            Log.i("currentUserAngle", (currentUserDistance).toString())
                            Log.i("previousUserAngle", (previousUserDistance).toString())
                            Log.i(
                                "Direction",
                                (currentUserDistance - previousUserDistance).toString()
                            )
                            when {
                                currentUserDistance - previousUserDistance > 0.04 -> {
                                    Log.i("Type", "going away")
//                                    robotController.speak("You are going left to right", buffer)
//                                    conditionGate ({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
                                }

                                currentUserDistance - previousUserDistance < -0.04 -> {
                                    Log.i("Type", "going towards")
//                                    robotController.speak("You are going left to right", buffer)
//                                    conditionGate ({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
                                }

                                else -> {
                                    Log.i("Type", "no change")
//                                    robotController.speak("You did not move", buffer)
//                                    conditionGate ({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
                                }
                            }
                        } else {
                            currentUserDistance = 0.0
                        }


                        if (isDetected && previousUserAngle != 0.0 && previousUserAngle == currentUserAngle && false) {
                            // logic for left right position
//                            when {
//                                currentUserAngle > 0.1 -> {
//                                    robotController.speak("You are to my left", buffer)
//                                    conditionGate ({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
//                                }
//                                currentUserAngle < -0.1 -> {
//                                    robotController.speak("You are to my right", buffer)
//                                    conditionGate ({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
//                                }
//                                else -> {
//                                    robotController.speak("You are in front of me", buffer)
//                                    conditionGate ({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
//                                }
//                            }

                            Log.i("currentUserAngle", (currentUserAngle).toString())
                            Log.i("previousUserAngle", (previousUserAngle).toString())
                            Log.i("Direction", (currentUserAngle - previousUserAngle).toString())
                            when {
                                currentUserAngle - previousUserAngle > 0.125 -> {
                                    Log.i("Type", "left to right")
//                                    robotController.speak("You are going left to right", buffer)
//                                    conditionGate ({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
                                }

                                currentUserAngle - previousUserAngle < -0.125 -> {
                                    Log.i("Type", "right to left")
//                                    robotController.speak("You are going left to right", buffer)
//                                    conditionGate ({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
                                }

                                else -> {
                                    Log.i("Type", "no change")
//                                    robotController.speak("You did not move", buffer)
//                                    conditionGate ({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
                                }
                            }
                        } else {
                            currentUserAngle = 0.0
                        }

//                        conditionTimer({!isDetected}, time = 50)

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
                                } else {
                                    isDetected = false
                                }
                            }
                        }

//                        Log.d("DetectStatus", detectionStatus.toString())
//                        Log.i("DetectData", detectionData.value.distance.toString())

                        if (isDetected && yPosition != YDirection.MISSING) {
                            robotController.speak(
                                "You are $xPosition to me and getting $xMotion", //"You are $yPosition and getting $yMotion",
                                buffer
                            )
                            conditionGate({ ttsStatus.value.status != TtsRequest.Status.COMPLETED })
                        }
                        // Ensure to cancel the monitoring job if the loop finishes
                        conditionTimer(
                            { !(isDetected && yPosition != YDirection.MISSING) },
                            time = 5
                        )
                        job.cancel()
                    }
                }
                buffer() // Add delay to ensure system work properly
            }
        }

        viewModelScope.launch { // Used to get state for x-direction and motion
            while (true) {
                // This method will allow play multiple per detection
                var isDetected = false

                // Launch a coroutine to monitor detectionStatus
                val job = launch {
                    detectionStatus.collect { status ->
                        if (status == DetectionStateChangedStatus.DETECTED) {
                            isDetected = true
                            buffer()
                        } else {
                            isDetected = false
                        }
                    }
                }

                previousUserAngle = currentUserAngle
                delay(500L)
                currentUserAngle = detectionData.value.angle

                Log.i("currentUserAngle", (currentUserAngle).toString())
                Log.i("previousUserAngle", (previousUserAngle).toString())
                Log.i("Direction", (currentUserAngle - previousUserAngle).toString())

                if (isDetected && previousUserDistance != 0.0) { //&& previousUserDistance != 0.0 && previousUserDistance == currentUserDistance) {
                    // logic for close or far position
                            when {
                                currentUserAngle > 0.1 -> {
                                    xPosition = XDirection.LEFT
                                }
                                currentUserAngle < -0.1 -> {
                                    xPosition = XDirection.RIGHT
                                }
                                else -> {
                                    xPosition = XDirection.MIDDLE
                                }
                            }
                } else {
                    xPosition = XDirection.GONE
                }

                if (isDetected && previousUserAngle != 0.0 && previousUserAngle != currentUserAngle) {
                    xMotion = when {
                        currentUserAngle - previousUserAngle > 0.125 -> {
                            XMovement.LEFTER
                        }

                        currentUserAngle - previousUserAngle < -0.125 -> {
                            XMovement.RIGHTER
                        }

                        else -> {
                            XMovement.NOWHERE
                        }
                    }
                } else {
                    currentUserAngle = 0.0
                }

                Log.i("STATE", (xPosition).toString())

                job.cancel()
            }
        }

        viewModelScope.launch { // Used to get state for y-direction and motion
            while (true) {
                // This method will allow play multiple per detection
                var isDetected = false

                // Launch a coroutine to monitor detectionStatus
                val job = launch {
                    detectionStatus.collect { status ->
                        if (status == DetectionStateChangedStatus.DETECTED) {
                            isDetected = true
                            buffer()
                        } else {
                            isDetected = false
                        }
                    }
                }

                previousUserDistance = currentUserDistance
                delay(500L)
                currentUserDistance = detectionData.value.distance

//                Log.i("currentUserAngle", (currentUserDistance).toString())
//                Log.i("previousUserAngle", (previousUserDistance).toString())
//                Log.i("Direction", (currentUserDistance - previousUserDistance).toString())

                if (isDetected && previousUserDistance != 0.0) { //&& previousUserDistance != 0.0 && previousUserDistance == currentUserDistance) {
                    // logic for close or far position
                    yPosition = when {
                        currentUserDistance < 0.60 -> {
                            YDirection.CLOSE
                        }

                        currentUserDistance < 1.35 -> {
                            YDirection.MIDRANGE
                        }
                        else -> {
                            YDirection.FAR
                        }
                    }
                } else {
                    yPosition = YDirection.MISSING
                }

                if (isDetected && previousUserDistance != 0.0 && previousUserDistance != currentUserDistance) { //&& previousUserDistance != 0.0 && previousUserDistance == currentUserDistance) {
                    yMotion = when {
                        currentUserDistance - previousUserDistance > 0.01 -> {
                            YMovement.FURTHER
                        }

                        currentUserDistance - previousUserDistance < -0.01 -> {
                            YMovement.CLOSER
                        }

                        else -> {
                            YMovement.NOWHERE
                        }
                    }
            }
//                Log.i("STATE", (yMotion).toString())

                job.cancel()
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
//            Log.i("Trigger", trigger().toString())
                if (trigger()) {
                    break
                }
            }
        }
    }

    private suspend fun conditionGate(trigger: () -> Boolean, log: String = "Null") {
        // Loop until the trigger condition returns false
        while (trigger()) {
//        Log.i("ConditionGate", "Trigger: $log")
            buffer() // Pause between checks to prevent busy-waiting
        }
//    Log.i("ConditionGate", "End")
    }

    private fun getDirectedAngle(a1: Double, a2: Double): Double {
        var difference = a1 - a2
        // Normalize the angle to keep it between -180 and 180 degrees
        if (difference > 180) difference -= 360
        if (difference < -180) difference += 360
        return difference
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