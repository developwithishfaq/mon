package com.monetization.interstitials.extensions

import android.app.Activity
import com.monetization.core.AdmobBasePreloadAdsManager
import com.monetization.core.FullScreenAdsShowListener
import com.monetization.core.ad_units.core.AdType
import com.monetization.interstitials.AdmobInterstitialAd
import com.monetization.interstitials.AdmobInterstitialAdsManager

object PreloadInterstitialAdsManager : AdmobBasePreloadAdsManager(AdType.INTERSTITIAL) {

    fun tryShowingInterstitialAd(
        placementKey: String,
        key: String,
        activity: Activity,
        requestNewIfNotAvailable: Boolean = true,
        requestNewIfAdShown: Boolean = true,
        handlerDelay: Long = 1000,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onAdDismiss: (Boolean) -> Unit,
    ) {

        val controller = AdmobInterstitialAdsManager.getAdController(key)
        canShowAd(
            activity = activity,
            requestNewIfNotAvailable = requestNewIfNotAvailable,
            placementKey = placementKey,
            normalLoadingTime = handlerDelay,
            controller = controller,
            onLoadingDialogStatusChange = onLoadingDialogStatusChange,
            onAdDismiss = onAdDismiss,
            showAd = {
                (controller?.getAvailableAd() as? AdmobInterstitialAd)?.showAd(
                    activity, object : FullScreenAdsShowListener {
                        override fun onAdDismiss(
                            adKey: String,
                            adShown: Boolean,
                            rewardEarned: Boolean,
                        ) {
                            onFreeAd(true)
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