# Glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}

# Retrofit
-dontwarn okio.**
-dontwarn javax.annotation.**

-keep class com.google.gson.** { *; }
-keep class com.google.inject.** { *; }

-keep class org.apache.http.** { *; }
-keep class org.apache.james.mime4j.** { *; }

-keep class javax.inject.** { *; }
-keep class javax.xml.stream.** { *; }

-keep class retrofit.** { *; }

-keep class com.google.appengine.** { *; }

-keepattributes *Annotation*
-keepattributes Signature

-dontwarn com.squareup.okhttp.*
-dontwarn rx.**

-dontwarn javax.xml.stream.**
-dontwarn com.google.appengine.**
-dontwarn java.nio.file.**
-dontwarn org.codehaus.**

# New rule
-dontwarn okhttp3.internal.platform.*


