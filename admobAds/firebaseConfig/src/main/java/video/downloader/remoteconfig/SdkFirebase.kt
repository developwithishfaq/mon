package video.downloader.remoteconfig

import android.content.Context
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.crashlytics.FirebaseCrashlytics
import java.lang.Exception

object SdkFirebase {

    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var firebaseCrashlytics: FirebaseCrashlytics

    fun initialize(context: Context) {
        FirebaseApp.initializeApp(context)
    }

    fun sendEvent(context: Context, message: String, bundle: Bundle = Bundle()) {
        if (::firebaseAnalytics.isInitialized.not()) {
            firebaseAnalytics = FirebaseAnalytics.getInstance(context)
        }
        firebaseAnalytics.logEvent("" + message, bundle)
    }

    fun sendException(exception: Exception) {
        if (::firebaseCrashlytics.isInitialized.not()) {
            firebaseCrashlytics = FirebaseCrashlytics.getInstance()
        }
        firebaseCrashlytics.recordException(exception)
    }
}