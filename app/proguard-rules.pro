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
#-renamesourcefileattribute SourceFile   com.razorpay

#-keep public class * extends android.support.v7.app.AppCompatActivity

#-keepclassmembers class * implements java.io.Serializable{
# static ** CREATOR;
# }

-keepclassmembernames class android.support.v7.widget.PopupMenu {
private android.support.v7.internal.view.menu.MenuPopupHelper mPopup; }

-keepclassmembernames class android.support.v7.internal.view.menu.MenuPopupHelper {
public void setForceShowIcon(boolean); }

-keepclassmembers class com.cstpl.menorahoes.view.MathJaxWebView {
   public *;
}
-keep public class * extends android.app.Activity
-keep public class * extends android.support.v7.app.AppCompatActivity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgent
-keep public class * extends android.preference.Preference
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v4.app.DialogFragment
-keep public class * extends com.actionbarsherlock.app.SherlockListFragment
-keep public class * extends com.actionbarsherlock.app.SherlockFragment
-keep public class * extends com.actionbarsherlock.app.SherlockFragmentActivity
-keep public class * extends android.app.Fragment
-keep public class com.android.vending.licensing.ILicensingService
-keepclassmembers class * implements java.io.Serializable{
 static ** CREATOR;
 }
-keep class android.support.v7.widget.SearchView { *; }
-dontwarn com.razorpay.**
-keep class com.razorpay.** {*;}
-keepattributes SourceFile,LineNumberTable

-dontwarn android.support.design.**
-keep class android.support.design.** { *; }
-keep interface android.support.design.** { *; }
