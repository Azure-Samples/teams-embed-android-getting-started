# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in d:\Users\rpatton\AppData\Local\Android\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-optimizations !class/unboxing/enum,!code/allocation/variable

# This preverification flag specifies processed class files are targeted at the Android platform.
# ProGuard then makes sure some features are compatible with Android
-android

-dontwarn java.awt.**,javax.security.**,java.beans.**
-dontwarn okio.**
-dontwarn com.viewpagerindicator.LinePageIndicator
-dontwarn okhttp3.**
-dontwarn butterknife.internal.**
-dontwarn retrofit2.**


-keepattributes Signature
-keepattributes Exceptions
-keepattributes *Annotation*
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeInvisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes RuntimeInvisibleParameterAnnotations

-keepattributes EnclosingMethod

# CortanaSDK
-keep class com.microsoft.bing.cortana.** { *; }
-keep class com.microsoft.cortana.sdk.** { *; }

# Msai SDK
-keep class com.microsoft.msai.** { *; }

# OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# ButterKnife 7
-keep class butterknife.** { *; }
-keep class **$$ViewBinder { *; }
-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}
-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

## GSON 2.2.4 specific rules ##
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
# For using GSON @Expose annotation
-keepattributes EnclosingMethod
# Gson specific classes
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }

# Retrofit 2.X
## https://square.github.io/retrofit/ ##
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclasseswithmembers interface * {
    @retrofit2.http.* <methods>;
}

-keep class com.microsoft.identity.** { *; }
-keep class com.microsoft.intune.mam.client.content.** { *; }

# SqlLite
-keep class org.sqlite.** { *; }
-keep class org.sqlite.database.** { *; }

# DBFlow
-keep class com.raizlabs.android.dbflow.** { *; }
-keep class net.sqlcipher.** { *; }
-dontwarn net.sqlcipher.**

# Fresco
-keep class com.facebook.** { *; }

# Adaptive Card
-keep class io.adaptivecards.** { *; }

-keep class com.microsoft.skype.teams.** { *; }

-keep class com.microsoft.teams.androidutils.** { *; }
-keep class com.microsoft.teams.collections.** { *; }


# teams core subpackages
-keep class com.microsoft.teams.core.app.** { *; }
-keep class com.microsoft.teams.core.calling.** { *; }
-keep class com.microsoft.teams.core.configuration.** { *; }
-keep class com.microsoft.teams.core.data.** { *; }
-keep class com.microsoft.teams.core.diagnostics.** { *; }
-keep class com.microsoft.teams.core.files.common.** { *; }
-keep class com.microsoft.teams.core.files.model.** { *; }
-keep class com.microsoft.teams.core.IDeepLinkUtil { *; }
-keep class com.microsoft.teams.core.IFreRegistry { *; }
-keep class com.microsoft.teams.core.injection.** { *; }
-keep class com.microsoft.teams.core.models.** { *; }
-keep class com.microsoft.teams.core.nativemodules.** { *; }
-keep class com.microsoft.teams.core.platform.** { *; }
-keep class com.microsoft.teams.core.preferences.** { *; }
-keep class com.microsoft.teams.core.roomcontroller.** { *; }
-keep class com.microsoft.teams.core.services.** { *; }
-keep class com.microsoft.teams.core.utilities.** { *; }
-keep class com.microsoft.teams.core.R.** { *; }
-keep class com.microsoft.teams.core.views.callbacks.** { *; }
-keep class com.microsoft.teams.core.views.fragments.** { *; }
-keep class com.microsoft.teams.core.views.tabs.** { *; }
-keep class com.microsoft.teams.core.views.utilities.** { *; }
-keep class com.microsoft.teams.core.views.widgets.** { *; }

-keep class com.microsoft.teams.DeepLinkUtil { *; }
-keep class com.microsoft.teams.expo.** { *; }
-keep class com.microsoft.teams.icons.** { *; }
-keep class com.microsoft.teams.location.** { *; }
-keep class com.microsoft.teams.mediagallery.** { *; }
-keep class com.microsoft.teams.nativepackagesample.** { *; }
-keep class com.microsoft.teams.networkutils.** { *; }
-keep class com.microsoft.teams.proguard.** { *; }
-keep class com.microsoft.teams.proximity.** { *; }
-keep class com.microsoft.teams.sharedstrings.** { *; }
-keep class com.microsoft.teams.slimcore.** { *; }
-keep class com.microsoft.teams.telemetry.** { *; }
-keep class com.microsoft.teams.theme.** { *; }
-keep class com.microsoft.teams.vault.** { *; }
-keep class com.microsoft.teams.widgets.** { *; }
-keep class com.microsoft.teams.zoomable.** { *; }
-keep class com.horcrux.** { *; }

# Teamssdk
-keep class com.microsoft.teamssdk.** { *; }
-keep class com.microsoft.applications.telemetry.** { *; }
-keep class com.microsoft.stardust.** { *; }
-keep class com.msft.stardust.** { *; }

# Trouter
-keep class com.microsoft.trouterclient.** { *; }

# Bluetooth SDK
-keep class com.github.douglasjunior.bluetoothclassiclibrary.** { *; }

# Access hidden Android API
-keep public class android.os.PowerManager {
    public static final ** PROXIMITY_SCREEN_OFF_WAKE_LOCK;
}

# Calling code
-keep class com.skype.** {*; }
-keep class com.skype.pcmhost.jniCallback {<methods>;}
-keep interface com.skype.pcmhost.jniInput {<methods>;}
-keep class com.skype.pcmhost.PcmHost {<methods>;}

# VideoHost has specific (not-reflected) access to 2.3 android.hardware.Camera methods

-dontnote junit.**,org.**,android.**,java.**,javax.**,com.android.**,dalvik.**

-keep class com.microsoft.dl.** { public *; }
-keep class com.skype.android.video.** { *; }
-keep class com.skype.android.util.Log { *; }
-keep class com.skype.android.util.Systrace { *; }
-keep class com.skype.android.util.VideoBuild { *; }
-keep class com.skype.android.util.config.** { *; }

-dontwarn com.skype.android.video.**
-dontwarn com.skype.android.media.**
-dontwarn com.microsoft.media.NTLMEngine*
-keep class com.microsoft.media.** {*;}
-keep class com.skype.android.media.CameraView { *; }

# Token Sharing Library
-keepclasseswithmembers class com.microsoft.tokenshare.AccountInfo { *; }
-keep enum com.microsoft.tokenshare.AccountInfo$** { *; }

# Intune
-keep class com.microsoft.intune.mam.** { *; }
-keep class android.** { *; }

# CodePush
-keep class com.microsoft.codepush.** { *; }

-ignorewarnings

# SharingSDK
-keep class com.microsoft.onedrive.** { *; }

# PowerLift
-keep class com.microsoft.skype.teams.powerlift.TeamsPowerLiftLogData { *; }

#### START SHIFTR SPECIFIC PROGUARD ####

# Variable names of network calls must not be changed
-keep class ols.microsoft.com.shiftr.network.** { *; }
-keep class ols.microsoft.com.sharedhelperutils.network.** { *; }

# Keep names of fragments for navigation purposes
-keepnames class ols.microsoft.com.shiftr.fragment.**

-dontnote org.apache.**
-dontnote android.net.**

# Strip out verbose logs
-assumenosideeffects class android.util.Log {
    public static *** v(...);
}

# GreenRobot EventBus

-keepattributes *Annotation*
-keepclassmembers,includedescriptorclasses class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}

# GreenRobot GreenDao

-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties { *; }

# GreenDao: If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
#### END SHIFTR SPECIFIC PROGUARD ####

# Baidu
-dontwarn com.baidu.**
-keep class com.baidu.**{ *; }

# Guava
-keep class com.google.common.** { *; }
# required for RateLimiter
-keep class com.google.common.base.Preconditions {
void checkArgument(boolean,java.lang.String,int);
}
-keep class com.google.common.base.Preconditions {
void checkArgument(boolean,java.lang.String,char,java.lang.Object);
}
# kotlin coroutines
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineScope {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}

# Espresso
-keep class androidx.test.espresso.** { *; }

#Work Manager
-keep class * extends androidx.work.Worker
-keep class * extends androidx.work.InputMerger
# Keep all constructors on ListenableWorker, Worker (also marked with @Keep)
-keep public class * extends androidx.work.ListenableWorker {
    public <init>(...);
}
# We need to keep WorkerParameters for the ListenableWorker constructor
-keep class androidx.work.WorkerParameters

# Hermes engine for React Native
-keep class com.facebook.hermes.unicode.** { *; }

#### START TALKNOW SPECIFIC PROGUARD ####

# SignalR
-keep class com.microsoft.signalr.** { *; }
-keep interface com.microsoft.signalr.** { *; }

#### END TALKNOW SPECIFIC PROGUARD ####

#DataBinding
-keep class androidx.databinding.DataBindingComponent {*;}
-keep class * extends androidx.databinding.DataBinderMapper { *; }
