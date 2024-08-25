package com.example.rewadedad.extensions

import android.app.Activity
import com.example.rewadedad.AdmobRewardedAd
import com.example.rewadedad.AdmobRewardedAdsManager
import com.monetization.core.AdmobBaseInstantAdsManager
import com.monetization.core.ad_units.core.AdType

object InstantRewardedAdsManager : AdmobBaseInstantAdsManager(AdType.REWARDED) {


    fun showInstantRewardedAd(
        enableKey: String,
        activity: Activity,
        key: String,
        normalLoadingTime: Long = 1_000,
        instantLoadingTime: Long = 8_000,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onAdDismiss: ((Boolean) -> Unit)? = null,
    ) {
        val controller = AdmobRewardedAdsManager.getAdController(key)
        canShowAd(
            activity = activity,
            placementKey = enableKey,
            normalLoadingTime = normalLoadingTime,
            instantLoadingTime = instantLoadingTime,
            controller = controller,
            onLoadingDialogStatusChange = onLoadingDialogStatusChange,
            onAdDismiss = onAdDismiss,
            showAd = {
                (controller?.getAvailableAd() as? AdmobRewardedAd)?.showAd(
                    activity,
                    fullScreenAdListener
                )
            }
        )
    }
}





















