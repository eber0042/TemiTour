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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000f\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0011\u0010\u0005\u001a\u00020\u0016H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u0017J)\u0010\u0018\u001a\u00020\u00162\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001a2\b\b\u0002\u0010\u001c\u001a\u00020\u001dH\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\u001eJ\'\u0010\u001f\u001a\u00020\u00162\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u001b0\u001a2\u0006\u0010 \u001a\u00020!H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\"R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\u0015X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u0006#"}, d2 = {"Lcom/temi/temiSDK/MainViewModel;", "Landroidx/lifecycle/ViewModel;", "robotController", "Lcom/temi/temiSDK/RobotController;", "(Lcom/temi/temiSDK/RobotController;)V", "buffer", "", "currentState", "Lcom/temi/temiSDK/State;", "defaultAngle", "", "detectionData", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/temi/temiSDK/DetectionDataChangedStatus;", "detectionStatus", "Lcom/temi/temiSDK/DetectionStateChangedStatus;", "movementStatus", "Lcom/temi/temiSDK/MovementStatusChangedStatus;", "ttsStatus", "Lcom/temi/temiSDK/TtsStatus;", "userRelativeDirection", "Lcom/temi/temiSDK/Direction;", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "conditionGate", "trigger", "Lkotlin/Function0;", "", "log", "", "(Lkotlin/jvm/functions/Function0;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "conditionTimer", "time", "", "(Lkotlin/jvm/functions/Function0;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel
public final class MainViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull
    private final com.temi.temiSDK.RobotController robotController = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.temi.temiSDK.TtsStatus> ttsStatus = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.temi.temiSDK.DetectionStateChangedStatus> detectionStatus = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.temi.temiSDK.DetectionDataChangedStatus> detectionData = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.temi.temiSDK.MovementStatusChangedStatus> movementStatus = null;
    private final long buffer = 100L;
    @org.jetbrains.annotations.NotNull
    private com.temi.temiSDK.State currentState = com.temi.temiSDK.State.CONSTRAINT_FOLLOW;
    private final double defaultAngle = 0.0;
    @org.jetbrains.annotations.NotNull
    private com.temi.temiSDK.Direction userRelativeDirection = com.temi.temiSDK.Direction.DEFAULT;
    
    @javax.inject.Inject
    public MainViewModel(@org.jetbrains.annotations.NotNull
    com.temi.temiSDK.RobotController robotController) {
        super();
    }
    
    private final java.lang.Object buffer(kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object conditionTimer(kotlin.jvm.functions.Function0<java.lang.Boolean> trigger, int time, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    private final java.lang.Object conditionGate(kotlin.jvm.functions.Function0<java.lang.Boolean> trigger, java.lang.String log, kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
}