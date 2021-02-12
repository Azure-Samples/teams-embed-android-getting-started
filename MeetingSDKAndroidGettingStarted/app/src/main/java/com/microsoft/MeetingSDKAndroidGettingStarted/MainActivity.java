package com.microsoft.MeetingSDKAndroidGettingStarted;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;

import com.azure.android.communication.common.CommunicationTokenCredential;
import com.azure.android.communication.ui.meetings.CallState;
import com.azure.android.communication.ui.meetings.MeetingAvatarAvailableCallback;
import com.azure.android.communication.ui.meetings.MeetingEventListener;
import com.azure.android.communication.ui.meetings.MeetingIdentityProvider;
import com.azure.android.communication.ui.meetings.MeetingJoinOptions;
import com.azure.android.communication.ui.meetings.MeetingUIClient;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MeetingEventListener, MeetingIdentityProvider {

    private final String ACS_TOKEN = "<ACS_TOKEN>";
    private final String meetingUrl = "<MEETING_URL>";
    private final String displayName = "John Smith";

    private MeetingUIClient meetingUIClient;
    private MeetingJoinOptions meetingJoinOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        meetingJoinOptions = new MeetingJoinOptions(displayName);
        
        getAllPermissions();
        createMeetingClient();

        Button joinMeeting = findViewById(R.id.join_meeting);
        joinMeeting.setOnClickListener(l -> joinMeeting());
    }

    private void createMeetingClient() {
        try {
            CommunicationTokenCredential credential = new CommunicationTokenCredential(ACS_TOKEN);
            meetingUIClient = new MeetingUIClient(credential);
            meetingUIClient.setMeetingEventListener(this);
            meetingUIClient.setMeetingIdentityProvider(this);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Failed to create meeting client: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void joinMeeting() {
        try {
            meetingUIClient.joinMeeting(meetingUrl, meetingJoinOptions);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Failed to join meeting: " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void getAllPermissions() {
        String[] requiredPermissions = new String[]{Manifest.permission.RECORD_AUDIO};
        ArrayList<String> permissionsToAskFor = new ArrayList<>();
        for (String permission : requiredPermissions) {
            if (ActivityCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsToAskFor.add(permission);
            }
        }
        if (!permissionsToAskFor.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsToAskFor.toArray(new String[0]), 202);
        }
    }

    private void updateAvatarImageAsynchronously(Context context, String identifier, MeetingAvatarAvailableCallback observer)
    {
        WeakReference<MeetingAvatarAvailableCallback> weakReferenceObserver = new WeakReference<MeetingAvatarAvailableCallback>(observer);
        // Fetch Avatar in new thread
        new Thread(new Runnable() {
            public void run()
            {
                // simulate a long task to get avatar and put it in cache if needed.
                System.out.println("fetching avatar in Asynchronous Task");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Drawable myAvatar = AppCompatResources.getDrawable(context, R.drawable.nodpi_avatar_placeholder_large_pink);

                // provide avatar when it is available.
                MeetingAvatarAvailableCallback callback = weakReferenceObserver.get();
                if (callback != null) {
                    System.out.println("invoke the callback method with fetched avatar");
                    callback.onAvatarAvailable(myAvatar);
                } else {
                    System.out.println("callback observer are reclaimed, there is no need to update avatar in UI anymore");
                }
            }
        }).start();
    }

    @Override
    public void onCallStateChanged(CallState callState) {
        System.out.println("Call state changed: " + callState);
    }

    @Override
    public void onRemoteParticipantCountChanged(int newCount) {
        System.out.println("Remote participant count changed: " + newCount);
    }

    @Override
    public void requestForAvatar(String userIdentifier, MeetingAvatarAvailableCallback meetingAvatarAvailableCallback) {
        Drawable myAvatar = null;

        try {
            System.out.println("MicrosoftTeamsSDKIdentityProvider.requestForAvatar called for userIdentifier: " + userIdentifier );
            if (userIdentifier.startsWith("8:teamsvisitor:")) {
                // get and provide avatar picture asynchronously with long fetching/decoding delay.
                System.out.println("invoke the callback method asynchronously with avatar for Anonymous user");
                updateAvatarImageAsynchronously(this, userIdentifier, meetingAvatarAvailableCallback);
            } else if (userIdentifier.startsWith("8:orgid:")) {
                System.out.println("invoke the callback method immediately with avatar for OrgID user");
                myAvatar = AppCompatResources.getDrawable(this, R.drawable.nodpi_doctor_image);
                meetingAvatarAvailableCallback.onAvatarAvailable(myAvatar);
            }  else if (userIdentifier.startsWith("8:acs:")) {
                System.out.println("invoke the callback method immediately with avatar for ACS user");
                myAvatar = AppCompatResources.getDrawable(this, R.drawable.nodpi_avatar_placeholder_large_green);
                meetingAvatarAvailableCallback.onAvatarAvailable(myAvatar);
            }
        } catch (Exception e) {
            System.out.println("MicrosoftTeamsSDKIdentityProvider: Exception while requestForAvatar for userIdentifier: " + userIdentifier + e.getMessage());
        }
    }
}