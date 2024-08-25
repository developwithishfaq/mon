package com.monetization.core

import android.app.Activity
import android.os.Handler
import android.os.Looper
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.AdsCommons
import com.monetization.core.commons.AdsCommons.isAdEnabled
import com.monetization.core.commons.AdsCommons.isFullScreenAdShowing
import com.monetization.core.commons.AdsCommons.logAds
import com.monetization.core.commons.SdkConfigs

abstract class AdmobBasePreloadAdsManager(
    private val adType: AdType,
) {
    private var loadingDialogListener: ((Boolean) -> Unit)? = null

    private var onDismissListener: ((Boolean) -> Unit)? = null
    fun onFreeAd(check: Boolean = false, rewardEarned: Boolean = false) {
        onDismissListener?.invoke(check)
        onDismissListener = null
        isFullScreenAdShowing = false
    }

    fun allowed(key: String): Boolean {
        val allowed = SdkConfigs.canShowAd(key, adType)
        if (allowed.not()) {
            logAds("Ad is restricted by Sdk to show Key=$key,type=$adType", true)
        }
        return allowed
    }

    fun canShowAd(
        activity: Activity,
        placementKey: String,
        normalLoadingTime: Long = 1_000,
        controller: AdsController?,
        requestNewIfNotAvailable: Boolean,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onAdDismiss: ((Boolean) -> Unit)? = null,
        showAd: () -> Unit,
    ) {
        val enabled = placementKey.isAdEnabled()
        if (AdsCommons.isFullScreenAdShowing) {
            logAds("Full Screen Ad is already showing")
            return
        }
        loadingDialogListener = onLoadingDialogStatusChange
        onDismissListener = onAdDismiss
        val key = controller?.getAdKey() ?: ""
        val availableAd = controller?.getAvailableAd()

        if (allowed(key).not()) {
            onFreeAd()
            return
        }
        if (enabled.not()) {
            onFreeAd()
            return
        }
        if (requestNewIfNotAvailable && availableAd == null) {
            controller?.loadAd(activity, "", null)
            onFreeAd()
            return
        }
        if (availableAd == null) {
            onFreeAd()
            return
        }
        if (controller?.getAvailableAd() == null) {
            onFreeAd()
            return
        }
        if (normalLoadingTime > 0) {
            loadingDialogListener?.invoke(true)
        }
        nowShowAd(
            normalLoadingTime = normalLoadingTime,
            controller = controller,
            showAd = showAd
        )
    }

    private fun nowShowAd(
        normalLoadingTime: Long,
        controller: AdsController,
        showAd: () -> Unit,
    ) {
        val adToShow = controller.getAvailableAd()
        if (adToShow != null) {
            Handler(Looper.getMainLooper()).postDelayed({
                loadingDialogListener?.invoke(false)
                showAd.invoke()
            }, normalLoadingTime)
        } else {
            onFreeAd()
        }
    }


}