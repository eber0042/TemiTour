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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000~\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0011\u0010\u0005\u001a\u00020!H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010\"J)\u0010#\u001a\u00020!2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020&0%2\b\b\u0002\u0010\'\u001a\u00020(H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010)J\'\u0010*\u001a\u00020!2\f\u0010$\u001a\b\u0012\u0004\u0012\u00020&0%2\u0006\u0010+\u001a\u00020,H\u0082@\u00f8\u0001\u0000\u00a2\u0006\u0002\u0010-J\u0018\u0010.\u001a\u00020\n2\u0006\u0010/\u001a\u00020\n2\u0006\u00100\u001a\u00020\nH\u0002R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082D\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\nX\u0082D\u00a2\u0006\u0002\n\u0000R\u0014\u0010\r\u001a\b\u0012\u0004\u0012\u00020\u000f0\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\u00110\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0012\u001a\b\u0012\u0004\u0012\u00020\u00130\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0014\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0015\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\u00170\u000eX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001a\u001a\u00020\u001bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001c\u001a\u00020\u0019X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001d\u001a\u00020\u001eX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001f\u001a\u00020 X\u0082\u000e\u00a2\u0006\u0002\n\u0000\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u00061"}, d2 = {"Lcom/temi/temiSDK/MainViewModel;", "Landroidx/lifecycle/ViewModel;", "robotController", "Lcom/temi/temiSDK/RobotController;", "(Lcom/temi/temiSDK/RobotController;)V", "buffer", "", "currentState", "Lcom/temi/temiSDK/State;", "currentUserAngle", "", "currentUserDistance", "defaultAngle", "detectionData", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/temi/temiSDK/DetectionDataChangedStatus;", "detectionStatus", "Lcom/temi/temiSDK/DetectionStateChangedStatus;", "movementStatus", "Lcom/temi/temiSDK/MovementStatusChangedStatus;", "previousUserAngle", "previousUserDistance", "ttsStatus", "Lcom/temi/temiSDK/TtsStatus;", "userRelativeDirection", "Lcom/temi/temiSDK/XDirection;", "xMotion", "Lcom/temi/temiSDK/XMovement;", "xPosition", "yMotion", "Lcom/temi/temiSDK/YMovement;", "yPosition", "Lcom/temi/temiSDK/YDirection;", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "conditionGate", "trigger", "Lkotlin/Function0;", "", "log", "", "(Lkotlin/jvm/functions/Function0;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "conditionTimer", "time", "", "(Lkotlin/jvm/functions/Function0;ILkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getDirectedAngle", "a1", "a2", "app_debug"})
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
    private com.temi.temiSDK.State currentState = com.temi.temiSDK.State.NULL;
    private final double defaultAngle = 120.0;
    @org.jetbrains.annotations.NotNull
    private com.temi.temiSDK.XDirection userRelativeDirection = com.temi.temiSDK.XDirection.GONE;
    private double previousUserAngle = 0.0;
    private double currentUserAngle = 0.0;
    @org.jetbrains.annotations.NotNull
    private com.temi.temiSDK.XDirection xPosition = com.temi.temiSDK.XDirection.GONE;
    @org.jetbrains.annotations.NotNull
    private com.temi.temiSDK.XMovement xMotion = com.temi.temiSDK.XMovement.NOWHERE;
    private double previousUserDistance = 0.0;
    private double currentUserDistance = 0.0;
    @org.jetbrains.annotations.NotNull
    private com.temi.temiSDK.YDirection yPosition = com.temi.temiSDK.YDirection.MISSING;
    @org.jetbrains.annotations.NotNull
    private com.temi.temiSDK.YMovement yMotion = com.temi.temiSDK.YMovement.NOWHERE;
    
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
    
    private final double getDirectedAngle(double a1, double a2) {
        return 0.0;
    }
}