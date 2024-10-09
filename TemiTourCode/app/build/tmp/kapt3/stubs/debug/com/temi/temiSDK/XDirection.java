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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\f\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\b\u0006\b\u0086\u0081\u0002\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002j\u0002\b\u0003j\u0002\b\u0004j\u0002\b\u0005j\u0002\b\u0006\u00a8\u0006\u0007"}, d2 = {"Lcom/temi/temiSDK/XDirection;", "", "(Ljava/lang/String;I)V", "LEFT", "RIGHT", "MIDDLE", "GONE", "app_debug"})
public enum XDirection {
    /*public static final*/ LEFT /* = new LEFT() */,
    /*public static final*/ RIGHT /* = new RIGHT() */,
    /*public static final*/ MIDDLE /* = new MIDDLE() */,
    /*public static final*/ GONE /* = new GONE() */;
    
    XDirection() {
    }
    
    @org.jetbrains.annotations.NotNull
    public static kotlin.enums.EnumEntries<com.temi.temiSDK.XDirection> getEntries() {
        return null;
    }
}