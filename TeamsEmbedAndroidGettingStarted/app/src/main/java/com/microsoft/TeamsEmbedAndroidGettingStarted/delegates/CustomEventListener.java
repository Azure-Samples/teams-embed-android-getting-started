/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.microsoft.TeamsEmbedAndroidGettingStarted.delegates;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.azure.android.communication.common.CommunicationIdentifier;
import com.azure.android.communication.ui.meetings.MeetingUIClientCallEventListener;
import com.azure.android.communication.ui.meetings.MeetingUIClientCallState;
import com.azure.android.communication.ui.meetings.MeetingUIClientCallUserEventListener;
import com.microsoft.TeamsEmbedAndroidGettingStarted.R;
import com.microsoft.TeamsEmbedAndroidGettingStarted.ui.teams.TeamsFragment;
import com.microsoft.teamssdk.calling.MicrosoftTeamsSDK;

import java.lang.ref.WeakReference;
import java.util.List;

public class CustomEventListener implements MeetingUIClientCallEventListener, MeetingUIClientCallUserEventListener {

    @Nullable
    private final CustomScreenProvider mCustomScreenProvider;
    private final WeakReference<TeamsFragment> teamsFragmentWeakReference;

    public CustomEventListener(@Nullable CustomScreenProvider customScreenProvider, @NonNull TeamsFragment teamsFragment) {
        mCustomScreenProvider = customScreenProvider;
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
        if (mCustomScreenProvider == null) return;
        View callControlsView = mCustomScreenProvider.getCallControlsView();
        if (callControlsView == null) return;
        Button mCallControlMicButtonView = callControlsView.findViewById(R.id.call_placeholder_button_2);
        if(mCallControlMicButtonView != null) {
            mCallControlMicButtonView.setText(MicrosoftTeamsSDK.isMuted() ? R.string.mic_off : R.string.mic_on);
        }
    }

    @Override
    public void onIsSendingVideoChanged() {
        if (mCustomScreenProvider == null) return;
        View callControlsView = mCustomScreenProvider.getCallControlsView();
        if (callControlsView == null) return;
        Button mCallControlVideoButtonView = callControlsView.findViewById(R.id.call_placeholder_button_1);
        if(mCallControlVideoButtonView != null) {
            mCallControlVideoButtonView.setText(MicrosoftTeamsSDK.isSendingVideo() ? R.string.video_on : R.string.video_off);
        }
    }

    @Override
    public void onIsHandRaisedChanged(List<String> participantIds) {
        if (mCustomScreenProvider == null) return;
        mCustomScreenProvider.setHandRaisedParticipants(participantIds);
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
