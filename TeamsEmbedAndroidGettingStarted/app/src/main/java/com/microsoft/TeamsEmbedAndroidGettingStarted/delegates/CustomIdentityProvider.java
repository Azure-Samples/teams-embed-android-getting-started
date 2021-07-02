/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.microsoft.TeamsEmbedAndroidGettingStarted.delegates;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.azure.android.communication.common.CommunicationIdentifier;
import com.azure.android.communication.common.CommunicationUserIdentifier;
import com.azure.android.communication.ui.meetings.MeetingUIClientAvatarSize;
import com.azure.android.communication.ui.meetings.MeetingUIClientCallIdentityProvider;
import com.azure.android.communication.ui.meetings.MeetingUIClientCallIdentityProviderCallback;
import com.microsoft.TeamsEmbedAndroidGettingStarted.R;

public class CustomIdentityProvider implements MeetingUIClientCallIdentityProvider {

    private final Context mContext;

    public CustomIdentityProvider(@NonNull Context context) {
        mContext = context;
    }

    @Override
    public void provideAvatarFor(CommunicationIdentifier communicationIdentifier, MeetingUIClientAvatarSize meetingUIClientAvatarSize, MeetingUIClientCallIdentityProviderCallback meetingUIClientCallIdentityProviderCallback) {
        try {
            if(communicationIdentifier instanceof CommunicationUserIdentifier) {
                CommunicationUserIdentifier userIdentifier = (CommunicationUserIdentifier) communicationIdentifier;
                System.out.println("MeetingUIClientIdentityProvider.provideAvatarFor called for userIdentifier: " + userIdentifier.getId());
                if (isAnonymousVisitor(userIdentifier.getId())) {
                    meetingUIClientCallIdentityProviderCallback.onAvatarAvailable(ContextCompat.getDrawable(mContext, R.drawable.nodpi_avatar_placeholder_large_pink));
                } else if (isOrgIdUser(userIdentifier.getId())) {
                    meetingUIClientCallIdentityProviderCallback.onAvatarAvailable(ContextCompat.getDrawable(mContext, R.drawable.nodpi_doctor_avatar));
                } else if (isACSUser(userIdentifier.getId())) {
                    meetingUIClientCallIdentityProviderCallback.onAvatarAvailable(ContextCompat.getDrawable(mContext, R.drawable.nodpi_avatar_placeholder_large_green));
                }
            }
        } catch (Exception e) {
            System.out.println("MeetingUIClientIdentityProvider: Exception while provideAvatarFor" + e.getMessage());
        }
    }

    @Override
    public void provideDisplayNameFor(CommunicationIdentifier communicationIdentifier, MeetingUIClientCallIdentityProviderCallback meetingUIClientCallIdentityProviderCallback) {
        if(communicationIdentifier instanceof CommunicationUserIdentifier) {
            CommunicationUserIdentifier userIdentifier = (CommunicationUserIdentifier) communicationIdentifier;
            System.out.println("MeetingUIClientIdentityProvider.provideDisplayNameFor called for userIdentifier: " + userIdentifier.getId());
            if (isACSUser(userIdentifier.getId())) {
                meetingUIClientCallIdentityProviderCallback.onDisplayNameAvailable("ACS User");
            }
        }

    }

    @Override
    public void provideSubTitleFor(CommunicationIdentifier communicationIdentifier, MeetingUIClientCallIdentityProviderCallback meetingUIClientCallIdentityProviderCallback) {
        if(communicationIdentifier instanceof CommunicationUserIdentifier) {
            CommunicationUserIdentifier userIdentifier = (CommunicationUserIdentifier) communicationIdentifier;
            System.out.println("MeetingUIClientIdentityProvider.provideSubTitleFor called for userIdentifier: " + userIdentifier.getId());
            if (isACSUser(userIdentifier.getId())) {
                meetingUIClientCallIdentityProviderCallback.onSubTitleAvailable("ACS Subtitle");
            }
        }
    }

    private boolean isAnonymousVisitor(String userIdentifier) {
        return userIdentifier.startsWith("8:teamsvisitor:");
    }

    private boolean isOrgIdUser(String userIdentifier) {
        return userIdentifier.startsWith("8:orgid:");
    }

    private boolean isACSUser(String userIdentifier) {
        return userIdentifier.startsWith("8:acs:");
    }
}
