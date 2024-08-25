package com.example.appupdate

import android.app.Activity
import android.content.Context
import android.content.IntentSender
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.common.IntentSenderForResultStarter
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import java.util.UUID

object AppUpdateHelper {

    private var loadingListener: AppUpdateLoadingListener? = null
    private var updateListener: SdkUpdateListener? = null
    private var appUpdateManager: AppUpdateManager? = null

    private var updateInfo: AppUpdateInfo? = null
    private var alreadyRequested = false


    private fun <I, O> ComponentActivity.registerActivityResultLauncher(
        contract: ActivityResultContract<I, O>,
        callback: ActivityResultCallback<O>,
    ): ActivityResultLauncher<I> {
        val key = UUID.randomUUID().toString()
        return activityResultRegistry.register(key, contract, callback)
    }

    private var updateLauncher: ActivityResultLauncher<IntentSenderRequest>? = null

    private val updateResultStarter =
        IntentSenderForResultStarter { intent, _, fillInIntent, flagsMask, flagsValues, _, _ ->
            val request = IntentSenderRequest.Builder(intent)
                .setFillInIntent(fillInIntent)
                .setFlags(flagsValues, flagsMask)
                .build()

            updateLauncher?.launch(request)
        }


    private fun getAppUpdateManager(context: Context): AppUpdateManager {
        if (appUpdateManager == null) {
            appUpdateManager = AppUpdateManagerFactory.create(context)
        }
        return appUpdateManager!!
    }


    fun checkAndStartUpdate(
        activity: ComponentActivity,
        callback: SdkUpdateListener,
        updateType: UpdateType = UpdateType.Immediate,
    ) {

        updateListener = callback
        checkForUpdate(activity, object : AppUpdateLoadingListener {
            override fun onUpdateAvailable(type: UpdateType) {
                if (updateType == type || updateType == UpdateType.Any) {
                    startUpdate(activity, updateType, callback)
                } else {
                    updateListener?.onUpdateFailed("Update Available, But Update Type Didn't Matched")
                }
            }

            override fun noNoUpdateAvailable() {
                updateListener?.onUpdateFailed("No Update Available")
            }

            override fun onFailure(reason: String) {
                updateListener?.onUpdateFailed(reason)
            }
        })
    }

    fun checkAndStartUpdate(
        activity: AppCompatActivity,
        callback: SdkUpdateListener,
        updateType: UpdateType = UpdateType.Immediate,
    ) {
        checkAndStartUpdate(activity as ComponentActivity, callback, updateType)
    }

    fun startUpdate(
        activity: ComponentActivity,
        updateType: UpdateType,
        listener: SdkUpdateListener,
    ) {
        updateListener = listener
        updateLauncher = activity.registerActivityResultLauncher(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            result.data?.let {
                if (result.resultCode == 909) {
                    if (result.resultCode == Activity.RESULT_OK) {
                        updateListener?.onUpdateStarted()
                        Toast.makeText(activity, "Downloading stated", Toast.LENGTH_SHORT).show()
                    } else {
                        updateListener?.onUpdateFailed("Update Failed")
                        Toast.makeText(activity, "Downloading failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    updateListener?.onUpdateFailed("Update Failed, result code not matched")
                }
            } ?: run {
                updateListener?.onUpdateFailed("Update Failed, No Data Found From Intent")
            }
        }
        try {
            appUpdateManager?.startUpdateFlowForResult(
                updateInfo!!,
                updateResultStarter,
                AppUpdateOptions.newBuilder(
                    if (updateType == UpdateType.Flexible) {
                        AppUpdateType.FLEXIBLE
                    } else {
                        AppUpdateType.IMMEDIATE
                    }
                ).build(),
                909
            )
        } catch (exception: IntentSender.SendIntentException) {
            updateListener?.onUpdateFailed("Exception in startUpdateFlowForResult ${exception.message}")
        }
    }

    fun startUpdate(
        activity: AppCompatActivity,
        updateType: UpdateType,
        listener: SdkUpdateListener,
    ) {
        startUpdate(activity as ComponentActivity, updateType, listener)
    }


    fun checkForUpdate(context: Context, callback: AppUpdateLoadingListener) {
        if (alreadyRequested) {
            return
        }
        this.loadingListener = callback
        alreadyRequested = true
        val appUpdateInfoTask = getAppUpdateManager(context).appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                val updateType =
                    if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                        UpdateType.Immediate
                    } else {
                        UpdateType.Flexible
                    }
                updateInfo = appUpdateInfo
                alreadyRequested = false
                // Request the update
                loadingListener?.onUpdateAvailable(updateType)
                loadingListener = null
            } else {
                updateInfo = null
                alreadyRequested = false

                loadingListener?.noNoUpdateAvailable()
                loadingListener = null
            }
        }.addOnFailureListener {
            logAds("App Update On Failure : ${it.message}")
            updateInfo = null
            alreadyRequested = false

            loadingListener?.onFailure(it.message ?: "OnFailure")
            loadingListener = null
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