package com.temi.temiSDK

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.robotemi.sdk.voice.model.TtsVoice
import com.robotemi.sdk.constants.Gender
import com.robotemi.sdk.TtsRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlin.random.Random
import kotlin.math.*
import kotlin.system.exitProcess

enum class State {

}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val robotController: RobotController
) : ViewModel() {

    private val ttsStatus = robotController.ttsStatus // Current speech state

    init {
        viewModelScope.launch {
            robotController.speak("Hi there, I am Temi. What can I do for you today?")
            conditionGate {ttsStatus.value.status != TtsRequest.Status.COMPLETED}
            robotController.speak("Wow, that is really interesting")
        }
    }

    private suspend fun buffer() {
        // Used to create a delay
        val buffer = 1L
        delay(buffer)
    }

    private suspend fun conditionGate(trigger: () -> Boolean) {
        // Loop until the trigger condition returns false
        while (trigger()) {
            buffer() // Pause between checks to prevent busy-waiting
        }
    }
}
