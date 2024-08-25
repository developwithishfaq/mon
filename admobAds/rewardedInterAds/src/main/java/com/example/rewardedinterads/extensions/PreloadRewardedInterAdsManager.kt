package com.example.rewardedinterads.extensions

import android.app.Activity
import com.example.rewardedinterads.AdmobRewardedInterAd
import com.example.rewardedinterads.AdmobRewardedInterAdsManager
import com.monetization.core.AdmobBasePreloadAdsManager
import com.monetization.core.FullScreenAdsShowListener
import com.monetization.core.ad_units.core.AdType

object PreloadRewardedInterAdsManager : AdmobBasePreloadAdsManager(AdType.REWARDED_INTERSTITIAL) {

    fun tryShowingRewardedInterAd(
        placementKey: String,
        key: String,
        activity: Activity,
        requestNewIfNotAvailable: Boolean = true,
        requestNewIfAdShown: Boolean = true,
        handlerDelay: Long = 1000,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onAdDismiss: (Boolean, Boolean) -> Unit,
    ) {

        val controller = AdmobRewardedInterAdsManager.getAdController(key)
        canShowAd(
            activity = activity,
            requestNewIfNotAvailable = requestNewIfNotAvailable,
            placementKey = placementKey,
            normalLoadingTime = handlerDelay,
            controller = controller,
            onLoadingDialogStatusChange = onLoadingDialogStatusChange,
            onAdDismiss = { shown ->
                onAdDismiss.invoke(shown, false)
            },
            showAd = {
                (controller?.getAvailableAd() as? AdmobRewardedInterAd)?.showAd(
                    activity, object : FullScreenAdsShowListener {
                        override fun onAdDismiss(
                            adKey: String,
                            adShown: Boolean,
                            rewardEarned: Boolean,
                        ) {
                            onFreeAd(adShown, rewardEarned)
                            if (requestNewIfAdShown) {
                                controller.loadAd(activity, "", null)
                            }
                        }
                    }
                )
            }
        )
    }
}