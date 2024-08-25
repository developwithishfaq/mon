package com.monetization.interstitials.extensions

import android.app.Activity
import com.monetization.core.AdmobBaseInstantAdsManager
import com.monetization.core.ad_units.core.AdType
import com.monetization.interstitials.AdmobInterstitialAd
import com.monetization.interstitials.AdmobInterstitialAdsManager

object InstantInterstitialAdsManager : AdmobBaseInstantAdsManager(AdType.INTERSTITIAL) {

    fun showInstantInterstitialAd(
        placementKey: String,
        activity: Activity,
        key: String,
        normalLoadingTime: Long = 1_000,
        instantLoadingTime: Long = 8_000,
        onLoadingDialogStatusChange: (Boolean) -> Unit,
        onAdDismiss: ((Boolean) -> Unit)? = null,
    ) {
        val controller = AdmobInterstitialAdsManager.getAdController(key)
        canShowAd(
            activity = activity,
            placementKey = placementKey,
            normalLoadingTime = normalLoadingTime,
            instantLoadingTime = instantLoadingTime,
            controller = controller,
            onLoadingDialogStatusChange = onLoadingDialogStatusChange,
            onAdDismiss = onAdDismiss,
            showAd = {
                (controller?.getAvailableAd() as? AdmobInterstitialAd)?.showAd(
                    activity,
                    fullScreenAdListener
                )
            }
        )
    }
}

/*
        if (canShowAd.not()) {
            return
        }
        nowShowAd(activity, normalLoadingTime, instantLoadingTime, controller!!) {
            Log.d("cvv", "Inter ad is going to show: ")

            (controller.getAvailableAd() as? AdmobInterstitialAd)?.showAd(
                activity,
                fullScreenAdListener
            )
        }
        if (adToShow != null) {
            Handler(Looper.getMainLooper()).postDelayed({
                loadingDialogListener?.invoke(false)
                adToShow.showAd(activity, fullScreenAdListener)
            }, normalLoadingTime)
        } else {
            startHandler(instantLoadingTime)
            controller.loadAd(context = activity,
                calledFrom = "showInstantAd ad to show null",
                callback = object : AdsLoadingStatusListener {
                    override fun onAdLoaded(adKey: String) {
                        loadingDialogListener.invoke(false)
                        val newAd = (controller.getAvailableAd() as? AdmobInterstitialAd)
                        logAds("showInstantAd onAdLoaded, Checks = ${onDismissListener != null && newAd != null && activity.isFinishing.not() && activity.isDestroyed.not()}")
                        if (onDismissListener != null && newAd != null && activity.isFinishing.not() && activity.isDestroyed.not()) {
                            newAd.showAd(
                                activity, fullScreenAdListener
                            )
                        } else {
                            stopHandler()
                            onFreeAd()
                        }
                    }

                    override fun onAdFailedToLoad(adKey: String, message: String, code: Int) {
                        logAds("showInstantAd onAdFailedToLoad $message,$code")
                        loadingDialogListener.invoke(false)
                        stopHandler()
                        onFreeAd()
                    }
                })
        }*/




















