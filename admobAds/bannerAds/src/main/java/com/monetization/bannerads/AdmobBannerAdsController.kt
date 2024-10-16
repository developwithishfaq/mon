package com.monetization.bannerads

import android.app.Activity
import android.os.Bundle
import com.google.ads.mediation.admob.AdMobAdapter
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.monetization.core.controllers.AdsControllerBaseHelper
import com.monetization.core.managers.AdsLoadingStatusListener
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.ad_units.core.AdUnit
import com.monetization.core.listeners.ControllersListener

class AdmobBannerAdsController(
    adKey: String,
    adIdsList: List<String>,
    listener: ControllersListener? = null
) : AdsControllerBaseHelper(adKey, AdType.BANNER, adIdsList,listener) {

    private var currentBannerAd: AdmobBannerAd? = null

    fun loadBannerAd(
        activity: Activity,
        calledFrom: String,
        callback: AdsLoadingStatusListener?,
        bannerAdType: BannerAdType = BannerAdType.Normal(BannerAdSize.AdaptiveBanner),
    ) {
        val commonLoadChecks = commonLoadAdChecks(callback)
        if (commonLoadChecks.not()) {
            return
        }
        val adId = getAdIdAndIncrementIndex()
        val adView = AdView(activity);
        adView.adUnitId = adId
        val extras = Bundle()
        val adSize = bannerAdType.getBannerSize()
        if (adSize == null) {
            (bannerAdType as? BannerAdType.Collapsible)?.let {
                adView.setAdSize(AdSize.BANNER)
                extras.putString(
                    "collapsible", when (bannerAdType.collapseType) {
                        BannerCollapsable.CollapseTop -> {
                            "top"
                        }

                        BannerCollapsable.CollapseBottom -> {
                            "bottom"
                        }
                    }
                )
            }
        } else {
            adView.setAdSize(adSize)
        }
        val adRequest = AdRequest.Builder()
            .addNetworkExtrasBundle(AdMobAdapter::class.java, extras)
            .build()

        adView.adListener = object : AdListener() {
            override fun onAdLoaded() {
                super.onAdLoaded()
                currentBannerAd?.destroyAd(activity)
                currentBannerAd = AdmobBannerAd(getAdKey(), adView)
                onLoaded()
                currentBannerAd?.adView?.setOnPaidEventListener { paidListener ->
                    onAdRevenue(
                        value = paidListener.valueMicros,
                        currencyCode = paidListener.currencyCode,
                        precisionType = paidListener.precisionType
                    )
                }
            }

            override fun onAdFailedToLoad(error: LoadAdError) {
                super.onAdFailedToLoad(error)
                currentBannerAd = null
                onAdFailed(error.message, error.code)
            }

            override fun onAdImpression() {
                super.onAdImpression()
                onImpression()
            }

            override fun onAdClicked() {
                super.onAdClicked()
                onAdClick()
            }
        }
        adView.loadAd(adRequest)
        onAdRequested()
    }

    override fun loadAd(
        activity: Activity,
        calledFrom: String,
        callback: AdsLoadingStatusListener?,
    ) {
        throw IllegalArgumentException("Please Call loadBannerAd function")
    }

    override fun destroyAd(activity: Activity?) {
        currentBannerAd = null
    }

    override fun isAdAvailable(): Boolean {
        return currentBannerAd != null
    }

    override fun getAvailableAd(): AdUnit? {
        return currentBannerAd
    }

}