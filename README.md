# Quickstart: Add Teams Embed to your Android app

For full instructions on how to build this code sample from scratch, look at [Quickstart: Add Teams Embed to your Android app](https://docs.microsoft.com/en-us/azure/communication-services/quickstarts/meeting/getting-started-with-teams-embed?pivots=platform-android)

## Prerequisites

To complete this tutorial, you’ll need the following prerequisites:

- An Azure account with an active subscription. [Create an account for free](https://azure.microsoft.com/free/?WT.mc_id=A261C142F). 
- [Android Studio](https://developer.android.com/studio), for running your Android application.
- A deployed Communication Services resource. [Create a Communication Services resource](https://docs.microsoft.com/en-us/azure/communication-services/quickstarts/create-communication-resource).
- A [User Access Token](https://docs.microsoft.com/en-us/azure/communication-services/quickstarts/access-tokens?pivots=programming-language-csharp) for your Azure Communication Service.

## Code Structure

- **./app/src/main/java/com/microsoft/TeamsEmbedAndroidGettingStarted/MainActivity.java:** Contains core logic for calling SDK integration.
- **./app/src/main/res/layout/activity_main.xml:** Contains core UI for sample app.

## Object model

The following classes and interfaces used in the quickstart handle some of the major features of the Azure Communication Services Teams Embed library:

| Name                                  | Description                                                  |
| ------------------------------------- | ------------------------------------------------------------ |
| MeetingUIClient| The MeetingUIClient is the main entry point to the Meeting library.|
| MeetingUIClientJoinOptions | MeetingUIClientJoinOptions are used for configurable options such as display name|
| MeetingUIClientCallState | The MeetingUIClientCallState is used to for reporting call state changes. The options are as follows: connecting, waitingInLobby, connected, and ended.|

## Before running sample code

1. Open an instance of PowerShell, Windows Terminal, Command Prompt or equivalent and navigate to the directory that you'd like to clone the sample to.
2. `git clone https://github.com/Azure-Samples/teams-embed-android-getting-started.git` 
3. With the `Access Token` procured in pre-requisites, add it to the **./app/src/main/java/com/microsoft/TeamsEmbedAndroidGettingStarted/MainActivity.java** file. Assign your access token in line 27:
   ```private static final String UserToken = "<User_Access_Token>";```

## Run the sample

Open the sample project using Android Studio and run the application.

![Final look and feel of the quick start app](Media/quickstart-android-join-meeting.png)

![Final look and feel of the quick start app](Media/quickstart-android-joined-meeting.png)
