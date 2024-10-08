package com.temi.temiSDK;

import android.content.pm.PackageManager;
import android.util.Log;
import androidx.core.content.ContextCompat;
import com.robotemi.sdk.Robot;
import com.robotemi.sdk.listeners.OnRobotReadyListener;
import com.robotemi.sdk.listeners.OnDetectionStateChangedListener;
import com.robotemi.sdk.listeners.OnDetectionDataChangedListener;
import com.robotemi.sdk.listeners.OnMovementStatusChangedListener;
import com.robotemi.sdk.TtsRequest;
import com.robotemi.sdk.model.DetectionData;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import javax.inject.Singleton;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0086\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0004\n\u0002\u0010\u0007\n\u0002\b\u0002\u0018\u00002\u00020\u00012\u00020\u00022\u00020\u00032\u00020\u00042\u00020\u0005B\u0005\u00a2\u0006\u0002\u0010\u0006J\u0010\u0010\u001c\u001a\u00020\u001d2\u0006\u0010\u001e\u001a\u00020\u001fH\u0016J\u0010\u0010 \u001a\u00020\u001d2\u0006\u0010!\u001a\u00020\"H\u0016J\u0018\u0010#\u001a\u00020\u001d2\u0006\u0010$\u001a\u00020%2\u0006\u0010&\u001a\u00020%H\u0016J\u0010\u0010\'\u001a\u00020\u001d2\u0006\u0010(\u001a\u00020)H\u0016J\u0010\u0010*\u001a\u00020\u001d2\u0006\u0010+\u001a\u00020,H\u0016J!\u0010-\u001a\u00020\u001d2\u0006\u0010.\u001a\u00020%2\u0006\u0010/\u001a\u000200H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00101J+\u00102\u001a\u00020\u001d2\u0006\u00103\u001a\u00020\"2\b\b\u0002\u00104\u001a\u0002052\u0006\u0010/\u001a\u000200H\u0086@\u00f8\u0001\u0000\u00a2\u0006\u0002\u00106R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\u000b0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\f\u001a\b\u0012\u0004\u0012\u00020\r0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u000f0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\t0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0012\u0010\u0013R\u0017\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u000b0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0015\u0010\u0013R\u0017\u0010\u0016\u001a\b\u0012\u0004\u0012\u00020\r0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0017\u0010\u0013R\u000e\u0010\u0018\u001a\u00020\u0019X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u000f0\u0011\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0013\u0082\u0002\u0004\n\u0002\b\u0019\u00a8\u00067"}, d2 = {"Lcom/temi/temiSDK/RobotController;", "Lcom/robotemi/sdk/listeners/OnRobotReadyListener;", "Lcom/robotemi/sdk/listeners/OnDetectionStateChangedListener;", "Lcom/robotemi/sdk/Robot$TtsListener;", "Lcom/robotemi/sdk/listeners/OnDetectionDataChangedListener;", "Lcom/robotemi/sdk/listeners/OnMovementStatusChangedListener;", "()V", "_detectionDataChangedStatus", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/temi/temiSDK/DetectionDataChangedStatus;", "_detectionStateChangedStatus", "Lcom/temi/temiSDK/DetectionStateChangedStatus;", "_movementStatusChangedStatus", "Lcom/temi/temiSDK/MovementStatusChangedStatus;", "_ttsStatus", "Lcom/temi/temiSDK/TtsStatus;", "detectionDataChangedStatus", "Lkotlinx/coroutines/flow/StateFlow;", "getDetectionDataChangedStatus", "()Lkotlinx/coroutines/flow/StateFlow;", "detectionStateChangedStatus", "getDetectionStateChangedStatus", "movementStatusChangedStatus", "getMovementStatusChangedStatus", "robot", "Lcom/robotemi/sdk/Robot;", "ttsStatus", "getTtsStatus", "onDetectionDataChanged", "", "detectionData", "Lcom/robotemi/sdk/model/DetectionData;", "onDetectionStateChanged", "state", "", "onMovementStatusChanged", "type", "", "status", "onRobotReady", "isReady", "", "onTtsStatusChanged", "ttsRequest", "Lcom/robotemi/sdk/TtsRequest;", "speak", "speech", "buffer", "", "(Ljava/lang/String;JLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "turnBy", "degree", "speed", "", "(IFJLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"})
public final class RobotController implements com.robotemi.sdk.listeners.OnRobotReadyListener, com.robotemi.sdk.listeners.OnDetectionStateChangedListener, com.robotemi.sdk.Robot.TtsListener, com.robotemi.sdk.listeners.OnDetectionDataChangedListener, com.robotemi.sdk.listeners.OnMovementStatusChangedListener {
    @org.jetbrains.annotations.NotNull
    private final com.robotemi.sdk.Robot robot = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.temi.temiSDK.TtsStatus> _ttsStatus = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.temi.temiSDK.TtsStatus> ttsStatus = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.temi.temiSDK.DetectionStateChangedStatus> _detectionStateChangedStatus = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.temi.temiSDK.DetectionStateChangedStatus> detectionStateChangedStatus = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.temi.temiSDK.DetectionDataChangedStatus> _detectionDataChangedStatus = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.temi.temiSDK.DetectionDataChangedStatus> detectionDataChangedStatus = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.MutableStateFlow<com.temi.temiSDK.MovementStatusChangedStatus> _movementStatusChangedStatus = null;
    @org.jetbrains.annotations.NotNull
    private final kotlinx.coroutines.flow.StateFlow<com.temi.temiSDK.MovementStatusChangedStatus> movementStatusChangedStatus = null;
    
    public RobotController() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.temi.temiSDK.TtsStatus> getTtsStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.temi.temiSDK.DetectionStateChangedStatus> getDetectionStateChangedStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.temi.temiSDK.DetectionDataChangedStatus> getDetectionDataChangedStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final kotlinx.coroutines.flow.StateFlow<com.temi.temiSDK.MovementStatusChangedStatus> getMovementStatusChangedStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object speak(@org.jetbrains.annotations.NotNull
    java.lang.String speech, long buffer, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable
    public final java.lang.Object turnBy(int degree, float speed, long buffer, @org.jetbrains.annotations.NotNull
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    /**
     * Called when connection with robot was established.
     *
     * @param isReady `true` when connection is open. `false` otherwise.
     */
    @java.lang.Override
    public void onRobotReady(boolean isReady) {
    }
    
    @java.lang.Override
    public void onTtsStatusChanged(@org.jetbrains.annotations.NotNull
    com.robotemi.sdk.TtsRequest ttsRequest) {
    }
    
    @java.lang.Override
    public void onDetectionStateChanged(int state) {
    }
    
    @java.lang.Override
    public void onDetectionDataChanged(@org.jetbrains.annotations.NotNull
    com.robotemi.sdk.model.DetectionData detectionData) {
    }
    
    @java.lang.Override
    public void onMovementStatusChanged(@org.jetbrains.annotations.NotNull
    java.lang.String type, @org.jetbrains.annotations.NotNull
    java.lang.String status) {
    }
}