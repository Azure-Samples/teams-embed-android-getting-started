package com.microsoft.MeetingSDKAndroidGettingStarted;

import com.microsoft.teamssdk.injection.components.ApplicationComponent;
import com.microsoft.teamssdk.injection.modules.SDKModule;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;

@Singleton
@Component(modules =
        {
                ApplicationModule.class,
                SDKModule.class
        })

public interface MeetingSDKAndroidGettingStartedComponent extends ApplicationComponent<MeetingSDKAndroidGettingStarted> {
    @Component.Factory
    interface Factory {
        ApplicationComponent create(@BindsInstance MeetingSDKAndroidGettingStarted application);
    }
}