# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

-keep class **.model.** { *; }
-dontwarn org.slf4j.impl.StaticLoggerBinder
#Gson
-keepattributes Signature,SourceFile,LineNumberTable
-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken
-keepattributes AnnotationDefault,RuntimeVisibleAnnotations
-keep class ir.mci.kidsvod.core.common.network.ApiResponse$* { *; }

-keep public class com.android.installreferrer.** { *; }

-keepattributes *Annotation*
-keepclassmembers class ** {
    @androidx.lifecycle.** *;
}
-keep class androidx.lifecycle.** { *; }
-keep class androidx.lifecycle.viewmodel.compose.** { *; }
-keep class kotlinx.coroutines.flow.** { *; }

-keepclassmembers class * extends androidx.lifecycle.ViewModel {
    <init>();
}

-keepclassmembers class * {
    @androidx.compose.runtime.Composable <methods>;
}
-keep class androidx.compose.** { *; }
-keep class androidx.activity.** { *; }
-keep class **.lint.** { *; }


#AdTrace start
-keep public class io.adtrace.sdk.** { *; }
-keep class io.adtrace.sdk.** { *; }
-keep class com.google.android.gms.common.ConnectionResult {
	int SUCCESS;
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient {
	com.google.android.gms.ads.identifier.AdvertisingIdClient$Info
	getAdvertisingIdInfo(android.content.Context);
}
-keep class com.google.android.gms.ads.identifier.AdvertisingIdClient$Info {
	java.lang.String getId();
	boolean isLimitAdTrackingEnabled();
}
 -keep public class com.android.installreferrer.** { *; }
#AdTrace end

-dontwarn com.google.protobuf.**
