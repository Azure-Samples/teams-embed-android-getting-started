package com.microsoft.MeetingSDKAndroidGettingStarted;

import android.app.Application;

import com.microsoft.skype.teams.app.DaggerApplication;
import com.microsoft.teamssdk.app.TeamsSDKApplication;
import com.microsoft.teamssdk.calling.MicrosoftTeamsSDK;
import com.microsoft.teamssdk.injection.components.ApplicationComponent;

import dagger.android.AndroidInjector;

public class MeetingSDKAndroidGettingStarted extends TeamsSDKApplication {

    @SuppressWarnings("rawtypes")
    @Override
    protected AndroidInjector< ? extends DaggerApplication> applicationInjector() {
        ApplicationComponent applicationComponent = DaggerMeetingSDKAndroidGettingStartedComponent.factory().create(this);
        setApplicationComponent(applicationComponent);
        return applicationComponent;
    }
}
