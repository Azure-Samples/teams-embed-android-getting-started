package com.microsoft.MeetingSDKAndroidGettingStarted;


import android.app.Application;

import androidx.annotation.NonNull;

import com.microsoft.teams.core.app.ITeamsApplication;
import com.microsoft.teamssdk.injection.components.DataContextComponent;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;

@Module(subcomponents = DataContextComponent.class)
public abstract class ApplicationModule {
    @Binds
    @Singleton
    abstract Application bindApplication(@NonNull MeetingSDKAndroidGettingStarted application);

    @Binds
    @Singleton
    abstract ITeamsApplication bindsTeamsApplication(MeetingSDKAndroidGettingStarted application);
}