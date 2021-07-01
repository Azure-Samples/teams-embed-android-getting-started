/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.microsoft.TeamsEmbedAndroidGettingStarted.delegates;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.azure.android.communication.common.CommunicationIdentifier;
import com.azure.android.communication.ui.meetings.MeetingUIClientCallEventListener;
import com.azure.android.communication.ui.meetings.MeetingUIClientCallState;
import com.azure.android.communication.ui.meetings.MeetingUIClientCallUserEventListener;
import com.microsoft.TeamsEmbedAndroidGettingStarted.R;
import com.microsoft.TeamsEmbedAndroidGettingStarted.ui.teams.TeamsFragment;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.List;

public class CustomEventListener implements MeetingUIClientCallEventListener, MeetingUIClientCallUserEventListener {

    private final WeakReference<TeamsFragment> teamsFragmentWeakReference;

    public CustomEventListener(@NonNull TeamsFragment teamsFragment) {
        teamsFragmentWeakReference = new WeakReference<>(teamsFragment);
    }

    @Override
    public void onCallStateChanged(MeetingUIClientCallState callState) {
        switch(callState) {
            case CONNECTING:
                System.out.println("Call state changed to 'Connecting'");
                teamsFragmentWeakReference.get().updateStatusLabel(R.string.call_connecting);
                break;
            case CONNECTED:
                System.out.println("Call state changed to 'Connected'");
                teamsFragmentWeakReference.get().updateStatusLabel(R.string.call_connected);
                break;
            case WAITING_IN_LOBBY:
                System.out.println("Call state changed to 'Waiting in Lobby'");
                teamsFragmentWeakReference.get().updateStatusLabel(R.string.call_in_lobby);
                break;
            case ENDED:
                System.out.println("Call state changed to 'Ended'");
                teamsFragmentWeakReference.get().updateStatusLabel(R.string.no_active_call);
                break;
        }
    }

    @Override
    public void onRemoteParticipantCountChanged(int newCount) {
        System.out.println("Remote participant count changed: " + newCount);
    }

    @Override
    public void onIsMutedChanged() {
        System.out.println("onIsMutedChanged");
    }

    @Override
    public void onIsSendingVideoChanged() {
        System.out.println("onIsSendingVideoChanged");
    }

    @Override
    public void onIsHandRaisedChanged(List<String> participantIds) {
        System.out.println("onIsHandRaisedChanged");
    }

    @Override
    public void onNamePlateOptionsClicked(@NonNull Activity activity, @NonNull CommunicationIdentifier communicationIdentifier) {
        Toast.makeText(activity, "Name plate options clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onParticipantViewLongPressed(@NonNull Activity activity, @NonNull CommunicationIdentifier communicationIdentifier) {
        Toast.makeText(activity, "Participant view long pressed", Toast.LENGTH_SHORT).show();
        return true;
    }
}
