/*
 * Copyright © Microsoft Corporation. All rights reserved.
 */

package com.microsoft.TeamsEmbedAndroidGettingStarted.delegates;

import android.app.Activity;

import androidx.annotation.NonNull;

import com.azure.android.communication.common.CommunicationIdentifier;
import com.azure.android.communication.ui.meetings.MeetingUIClientCallRosterDelegate;
import com.microsoft.skype.teams.util.SystemUtil;

public class CustomCallRosterDelegate implements MeetingUIClientCallRosterDelegate {

    public CustomCallRosterDelegate() {
    }

    @Override
    public boolean onCallParticipantCellTapped(@NonNull Activity activity, @NonNull CommunicationIdentifier userIdentifier) {
        SystemUtil.showToast(activity, "onCallParticipantCellTapped");
        return true;
    }
}
