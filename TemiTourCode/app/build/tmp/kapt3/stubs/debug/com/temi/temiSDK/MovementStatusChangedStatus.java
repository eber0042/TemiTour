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

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\t\n\u0002\u0010\u000b\n\u0002\b\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0010\u000e\n\u0000\b\u0086\b\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\t\u0010\u000b\u001a\u00020\u0003H\u00c6\u0003J\t\u0010\f\u001a\u00020\u0005H\u00c6\u0003J\u001d\u0010\r\u001a\u00020\u00002\b\b\u0002\u0010\u0002\u001a\u00020\u00032\b\b\u0002\u0010\u0004\u001a\u00020\u0005H\u00c6\u0001J\u0013\u0010\u000e\u001a\u00020\u000f2\b\u0010\u0010\u001a\u0004\u0018\u00010\u0001H\u00d6\u0003J\t\u0010\u0011\u001a\u00020\u0012H\u00d6\u0001J\t\u0010\u0013\u001a\u00020\u0014H\u00d6\u0001R\u0011\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\n\u00a8\u0006\u0015"}, d2 = {"Lcom/temi/temiSDK/MovementStatusChangedStatus;", "", "type", "Lcom/temi/temiSDK/MovementType;", "status", "Lcom/temi/temiSDK/MovementStatus;", "(Lcom/temi/temiSDK/MovementType;Lcom/temi/temiSDK/MovementStatus;)V", "getStatus", "()Lcom/temi/temiSDK/MovementStatus;", "getType", "()Lcom/temi/temiSDK/MovementType;", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "", "app_debug"})
public final class MovementStatusChangedStatus {
    @org.jetbrains.annotations.NotNull
    private final com.temi.temiSDK.MovementType type = null;
    @org.jetbrains.annotations.NotNull
    private final com.temi.temiSDK.MovementStatus status = null;
    
    public MovementStatusChangedStatus(@org.jetbrains.annotations.NotNull
    com.temi.temiSDK.MovementType type, @org.jetbrains.annotations.NotNull
    com.temi.temiSDK.MovementStatus status) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.temi.temiSDK.MovementType getType() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.temi.temiSDK.MovementStatus getStatus() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.temi.temiSDK.MovementType component1() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.temi.temiSDK.MovementStatus component2() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull
    public final com.temi.temiSDK.MovementStatusChangedStatus copy(@org.jetbrains.annotations.NotNull
    com.temi.temiSDK.MovementType type, @org.jetbrains.annotations.NotNull
    com.temi.temiSDK.MovementStatus status) {
        return null;
    }
    
    @java.lang.Override
    public boolean equals(@org.jetbrains.annotations.Nullable
    java.lang.Object other) {
        return false;
    }
    
    @java.lang.Override
    public int hashCode() {
        return 0;
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public java.lang.String toString() {
        return null;
    }
}