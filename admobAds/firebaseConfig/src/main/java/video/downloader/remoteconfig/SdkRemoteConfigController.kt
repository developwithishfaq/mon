package video.downloader.remoteconfig

import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.annotation.XmlRes
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings

object SdkRemoteConfigController {

    private var remoteConfig: FirebaseRemoteConfig? = null
    private var listener: SdkConfigListener? = null

    private var handler: Handler = Handler(Looper.getMainLooper())
    private var isHandlerRunning = false


    private var runnable = {
        if (isHandlerRunning) {
            logAds("")
            responseCallBack()
        }
    }


    fun getRemoteConfigString(key: String, def: String = ""): String {
        val values = remoteConfig?.getString(key) ?: def
        logConfig(key, values)
        return values
    }

    fun getRemoteConfigLong(key: String, def: Long = -1): Long {
        val values = remoteConfig?.getLong(key) ?: def
        logConfig(key, values.toString())
        return values
    }

    fun logConfig(key: String, value: String) {
        Log.d("configss", "Key=$key, value=$value ")
        Log.d("configss", "-----------------------------------")
    }

    fun getRemoteConfigBoolean(key: String, def: Boolean = false): Boolean {
        when (key) {
            "SDK_FALSE" -> {
                return false
            }
            "SDK_TRUE" -> {
                return true
            }
            else -> {
                val values = remoteConfig?.getBoolean(key) ?: def
                logConfig(key, values.toString())
                return values
            }
        }
    }

    fun fetchRemoteConfig(
        @XmlRes defaultXml: Int,
        fetchOutTimeInSeconds: Long,
        handlerDelayInSeconds: Long,
        callback: SdkConfigListener?,
        onUpdate: () -> Unit,
    ) {
        try {
            if (callback != null) {
                startHandler(handlerDelayInSeconds)
            }
            this.listener = callback
            remoteConfig = FirebaseRemoteConfig.getInstance()
            logAds("Remote Config Is Ok ${remoteConfig != null}", isError = remoteConfig == null)
            val remoteConfigBuilder = FirebaseRemoteConfigSettings.Builder()
            remoteConfigBuilder.fetchTimeoutInSeconds = fetchOutTimeInSeconds
            if (com.google.firebase.remoteconfig.BuildConfig.DEBUG) {
                remoteConfigBuilder.setMinimumFetchIntervalInSeconds(0)
            }
            remoteConfig?.setConfigSettingsAsync(
                remoteConfigBuilder.build()
            )
            remoteConfig?.setDefaultsAsync(defaultXml)
            remoteConfig?.fetchAndActivate()
                ?.addOnCompleteListener {
                    responseCallBack()
                }?.addOnCanceledListener {
                    responseCallBack()
                }?.addOnFailureListener {
                    responseCallBack()
                }
            remoteConfig?.addOnConfigUpdateListener(object : ConfigUpdateListener {
                override fun onUpdate(configUpdate: ConfigUpdate) {
                    onUpdate.invoke()
                }

                override fun onError(error: FirebaseRemoteConfigException) {

                }
            })
        } catch (_: Exception) {
            logAds("Firebase is not initialized", true)
            responseCallBack()
        }
    }

    private fun startHandler(time: Long) {
        if (isHandlerRunning.not()) {
            isHandlerRunning = true
            val actualTime = time * 1000
            logAds("Remote Config Time Started Of Millies=$actualTime")
            handler.postDelayed(runnable, actualTime)
        }
    }

    private fun responseCallBack() {
        listener?.onFetch()
        listener = null
        stopHandler()
    }

    private fun stopHandler() {
        if (isHandlerRunning) {
            handler.removeCallbacks(runnable)
            handler.removeCallbacksAndMessages(null)
        }
    }
}

fun logAds(message: String, isError: Boolean = false) {
    if (isError) {
        Log.e("adsPlugin", "Ads: $message")
    } else {
        Log.d("adsPlugin", "Ads: $message")
    }
}