package com.temi.temiSDK;

import android.service.notification.Condition;
import android.util.Log;
import androidx.lifecycle.ViewModel;
import com.robotemi.sdk.voice.model.TtsVoice;
import com.robotemi.sdk.constants.Gender;
import com.robotemi.sdk.TtsRequest;
import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.flow.StateFlow;
import java.util.Calendar;
import javax.inject.Inject;
import kotlin.math.*;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0005\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005\u00a8\u0006\u0006"}, d2 = {"Lcom/temi/temiSDK/State;", "", "(Ljava/lang/String;I)V", "TALK", "DISTANCE", "ANGLE", "app_debug"})
public enum State {
    /*public static final*/ TALK /* = new TALK() */,
    /*public static final*/ DISTANCE /* = new DISTANCE() */,
    /*public static final*/ ANGLE /* = new ANGLE() */;
    
    State() {
    }
    
    @org.jetbrains.annotations.NotNull
    public static kotlin.enums.EnumEntries<com.temi.temiSDK.State> getEntries() {
        return null;
    }
}