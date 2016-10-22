# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/yshrsmz/android-sdk/tools/proguard/proguard-android.txt
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

-keepattributes *Annotation*
-useuniqueclassmembernames
-keepattributes SourceFile,LineNumberTable
-allowaccessmodification
-keep public class * extends java.lang.Exception

-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule

# RxJava
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
   long producerIndex;
   long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
   long producerNode;
   long consumerNode;
}

# configs for app
-dontwarn net.yslibrary.monotweety.activity.main.MainActivity
-dontwarn net.yslibrary.monotweety.activity.compose.ComposeActivity