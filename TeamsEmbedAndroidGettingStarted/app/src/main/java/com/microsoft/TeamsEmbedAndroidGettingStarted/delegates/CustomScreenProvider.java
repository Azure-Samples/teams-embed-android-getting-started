/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.microsoft.TeamsEmbedAndroidGettingStarted.delegates;

import android.content.Context;
import android.content.DialogInterface;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;

import com.azure.android.communication.common.CommunicationUserIdentifier;
import com.azure.android.communication.ui.meetings.MeetingUIClientAudioRoute;
import com.azure.android.communication.ui.meetings.MeetingUIClientCall;
import com.azure.android.communication.ui.meetings.MeetingUIClientConnectingScreenProvider;
import com.azure.android.communication.ui.meetings.MeetingUIClientInCallScreenProvider;
import com.azure.android.communication.ui.meetings.MeetingUIClientLayoutMode;
import com.azure.android.communication.ui.meetings.MeetingUIClientStagingScreenProvider;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.microsoft.TeamsEmbedAndroidGettingStarted.R;
import com.microsoft.TeamsEmbedAndroidGettingStarted.ui.teams.TeamsFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class CustomScreenProvider implements MeetingUIClientInCallScreenProvider,
        MeetingUIClientStagingScreenProvider,
        MeetingUIClientConnectingScreenProvider {

    private final Context mContext;
    private View mCallControlsView;
    private List<String> mHandRaisedParticipants = new ArrayList<>();
    private final WeakReference<TeamsFragment> teamsFragmentWeakReference;

    public CustomScreenProvider(@NonNull Context context, @NonNull TeamsFragment teamsFragment) {
        mContext = context;
        teamsFragmentWeakReference = new WeakReference<>(teamsFragment);
    }

    @Nullable
    public View getCallControlsView() {
        return mCallControlsView;
    }

    @Override
    public View provideInCallControlBottomBar() {
        View customCallControls = getCustomCallControlsLayout();
        mCallControlsView = customCallControls;
        MeetingUIClientCall meetingUIClientCall = getMeetingUIClientCall();

        Button callControlMicButtonView = customCallControls.findViewById(R.id.call_placeholder_button_2);
        callControlMicButtonView.setText((meetingUIClientCall != null && meetingUIClientCall.isMuted()) ? R.string.mic_off : R.string.mic_on);
        callControlMicButtonView.setOnClickListener(l -> toggleMute());

        Button callControlVideoButtonView = customCallControls.findViewById(R.id.call_placeholder_button_1);
        callControlVideoButtonView.setText((meetingUIClientCall != null && meetingUIClientCall.isSendingVideo()) ? R.string.video_on : R.string.video_off);
        callControlVideoButtonView.setOnClickListener(l -> toggleCamera());

        Button callControlSpeakerButtonView = customCallControls.findViewById(R.id.call_placeholder_button_3);
        callControlSpeakerButtonView.setOnClickListener(l -> showBottomSheetDialog((ViewGroup) mCallControlsView.getParent()));

        Button callControlRaiseHandButtonView = customCallControls.findViewById(R.id.call_placeholder_button_4);
        callControlRaiseHandButtonView.setOnClickListener(l -> toggleHand());
        return customCallControls;
    }

    @Override
    public View provideInCallControlTopBar() {
        View customCallControlTopBar = LayoutInflater.from(mContext).inflate(R.layout.custom_app_bar, null, false);
        Button closeCallButton = customCallControlTopBar.findViewById(R.id.end_call);
        closeCallButton.setOnClickListener(l -> hangUp());

        Button appbarPeopleButton = customCallControlTopBar.findViewById(R.id.people_button);
        appbarPeopleButton.setOnClickListener(l -> showCallRoster());

        Button moreButton = customCallControlTopBar.findViewById(R.id.more_button);
        moreButton.setOnClickListener(l -> showLayoutModeDialog((ViewGroup) customCallControlTopBar.getParent()));

        return customCallControlTopBar;
    }

    @Override
    public int provideJoinButtonBackground() {
        return R.drawable.join_button_background;
    }

    @Override
    public int provideStagingScreenBackgroundColor() {
        return ContextCompat.getColor(mContext, com.microsoft.teams.calling.ui.R.color.black);
    }

    @Override
    public int provideConnectingScreenBackgroundColor() {
        return ContextCompat.getColor(mContext, com.microsoft.teams.calling.ui.R.color.black);
    }

    private View getCustomCallControlsLayout() {
        View customCallControls = LayoutInflater.from(mContext).inflate(R.layout.custom_call_controls, null, true);
        Display display = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int screenOrientation = display.getRotation();
        switch (screenOrientation) {
            case Surface.ROTATION_90:
            case Surface.ROTATION_270:
                customCallControls = LayoutInflater.from(mContext).inflate(R.layout.custom_call_controls_90, null, true);
                break;
            case Surface.ROTATION_0:
            case Surface.ROTATION_180:
            default:
                break;
        }
        return customCallControls;
    }

    private void toggleMute() {
        MeetingUIClientCall meetingUIClientCall = getMeetingUIClientCall();
        if (meetingUIClientCall != null) {
            try {
                if(meetingUIClientCall.isMuted()) {
                    meetingUIClientCall.unmute();
                } else {
                    meetingUIClientCall.mute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void toggleCamera() {
        MeetingUIClientCall meetingUIClientCall = getMeetingUIClientCall();
        if (meetingUIClientCall != null) {
            try {
                if (meetingUIClientCall.isSendingVideo()) {
                    meetingUIClientCall.stopVideo();
                } else {
                    meetingUIClientCall.startVideo();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void toggleHand() {
        MeetingUIClientCall meetingUIClientCall = getMeetingUIClientCall();
        if (meetingUIClientCall != null) {
            try {
                if (meetingUIClientCall.isHandRaised()) {
                    if(mHandRaisedParticipants.size() > 0) {
                        String userIdentifier = mHandRaisedParticipants.get(0);
                        meetingUIClientCall.lowerHand(new CommunicationUserIdentifier(userIdentifier));
                    }
                } else {
                    meetingUIClientCall.raiseHand();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void hangUp() {
        MeetingUIClientCall meetingUIClientCall = getMeetingUIClientCall();
        try {
            if (meetingUIClientCall != null) {
                meetingUIClientCall.hangUp();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCallRoster() {
        MeetingUIClientCall meetingUIClientCall = getMeetingUIClientCall();
        try {
            if (meetingUIClientCall != null) {
                meetingUIClientCall.showCallRoster();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void showLayoutModeDialog(ViewGroup parent) {
        List<MeetingUIClientLayoutMode> supportedModes = null;
        MeetingUIClientCall meetingUIClientCall = getMeetingUIClientCall();
        try {
            supportedModes = meetingUIClientCall.getSupportedLayoutModes();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final ArrayAdapter<MeetingUIClientLayoutMode> arrayAdapter = new ArrayAdapter<MeetingUIClientLayoutMode>(parent.getContext(), android.R.layout.simple_list_item_activated_1);
        for(MeetingUIClientLayoutMode mode: supportedModes) {
            switch (mode) {
                case LayoutModeGallery:
                    arrayAdapter.add(MeetingUIClientLayoutMode.LayoutModeGallery);
                    break;
                case LayoutModeTogether:
                    arrayAdapter.add(MeetingUIClientLayoutMode.LayoutModeTogether);
                    break;
                case LayoutModeLargeGallery:
                    arrayAdapter.add(MeetingUIClientLayoutMode.LayoutModeLargeGallery);
                    break;
            }
        }
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(parent.getContext());
        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MeetingUIClientLayoutMode mode = arrayAdapter.getItem(which);
                try {
                    meetingUIClientCall.changeLayout(mode);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }

    private void showBottomSheetDialog(ViewGroup parent) {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(parent.getContext());
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_audio_route);
        final TextView device_audio = bottomSheetDialog.findViewById(R.id.device);
        final TextView speaker_audio = bottomSheetDialog.findViewById(R.id.speaker);
        final TextView audio_off = bottomSheetDialog.findViewById(R.id.audio_off);

        device_audio.setOnClickListener(l -> setAudioRoute(MeetingUIClientAudioRoute.AudioRouteEarpiece, R.string.device_audio, bottomSheetDialog));

        speaker_audio.setOnClickListener(l -> setAudioRoute(MeetingUIClientAudioRoute.AudioRouteSpeakerOn, R.string.speaker, bottomSheetDialog));

        audio_off.setOnClickListener(l -> setAudioRoute(MeetingUIClientAudioRoute.AudioRouteAudioOff, R.string.audio_off, bottomSheetDialog));
        bottomSheetDialog.show();
    }

    private void setAudioRoute(MeetingUIClientAudioRoute audioRoute, int route, BottomSheetDialog bottomSheetDialog) {
        Button callControlSpeakerButtonView = mCallControlsView.findViewById(R.id.call_placeholder_button_3);
        MeetingUIClientCall meetingUIClientCall = getMeetingUIClientCall();
        try {
            if(callControlSpeakerButtonView != null) {
                callControlSpeakerButtonView.setText(route);
            }
            meetingUIClientCall.setAudioRoute(audioRoute);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bottomSheetDialog.dismiss();
    }

    void setHandRaisedParticipants(List<String> handRaisedParticipants) {
        mHandRaisedParticipants = handRaisedParticipants;
    }

    MeetingUIClientCall getMeetingUIClientCall() {
        return teamsFragmentWeakReference.get().getMeetingUIClientCall();
    }
}
