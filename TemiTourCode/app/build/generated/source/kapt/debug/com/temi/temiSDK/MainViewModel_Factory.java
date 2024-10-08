// Generated by Dagger (https://dagger.dev).
package com.temi.temiSDK;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class MainViewModel_Factory implements Factory<MainViewModel> {
  private final Provider<RobotController> robotControllerProvider;

  public MainViewModel_Factory(Provider<RobotController> robotControllerProvider) {
    this.robotControllerProvider = robotControllerProvider;
  }

  @Override
  public MainViewModel get() {
    return newInstance(robotControllerProvider.get());
  }

  public static MainViewModel_Factory create(Provider<RobotController> robotControllerProvider) {
    return new MainViewModel_Factory(robotControllerProvider);
  }

  public static MainViewModel newInstance(RobotController robotController) {
    return new MainViewModel(robotController);
  }
}
