// Generated by Dagger (https://dagger.dev).
package com.temi.temiSDK;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class RobotModule_ProvideRobotControllerFactory implements Factory<RobotController> {
  @Override
  public RobotController get() {
    return provideRobotController();
  }

  public static RobotModule_ProvideRobotControllerFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static RobotController provideRobotController() {
    return Preconditions.checkNotNullFromProvides(RobotModule.INSTANCE.provideRobotController());
  }

  private static final class InstanceHolder {
    private static final RobotModule_ProvideRobotControllerFactory INSTANCE = new RobotModule_ProvideRobotControllerFactory();
  }
}
