package com.example.rewadedad

import android.app.Activity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.monetization.core.AdsController
import com.monetization.core.AdsControllerBaseHelper
import com.monetization.core.FullScreenAdsShowListener
import com.monetization.core.ad_units.GeneralInterOrAppOpenAd
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.AdsCommons
import com.monetization.core.listeners.ControllersListener

class AdmobRewardedAd(
    val rewardedAd: RewardedAd,
    val adKey: String,
) : GeneralInterOrAppOpenAd {
    private var rewardEarned = false

    override fun showAd(activity: Activity, callBack: FullScreenAdsShowListener) {
        if (AdsCommons.isFullScreenAdShowing) {
            return
        }
        AdsCommons.isFullScreenAdShowing = true
        val controller: AdsControllerBaseHelper? = AdmobRewardedAdsManager.getAdController(adKey)
        rewardedAd.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                super.onAdFailedToShowFullScreenContent(p0)
                AdsCommons.isFullScreenAdShowing = false
                AdmobRewardedAdsManager.getAdController(adKey)?.destroyAd(activity)
                callBack.onAdDismiss(adKey)
            }

            override fun onAdImpression() {
                super.onAdImpression()
                controller?.onImpression()
            }

            override fun onAdShowedFullScreenContent() {
                super.onAdShowedFullScreenContent()
                AdmobRewardedAdsManager.getAdController(adKey)?.destroyAd(activity)
                callBack.onAdShown(adKey)
            }

            override fun onAdClicked() {
                super.onAdClicked()
                controller?.onAdClick()
                callBack.onAdClick(adKey)
            }

            override fun onAdDismissedFullScreenContent() {
                super.onAdDismissedFullScreenContent()
                AdsCommons.isFullScreenAdShowing = false
                callBack.onAdDismiss(adKey,true, rewardEarned)
            }
        }
        rewardedAd.show(
            activity
        ) { rewardEarned = true }
    }

    override fun destroyAd() {
    }


    override fun getAdType(): AdType {
        return AdType.REWARDED
    }
}