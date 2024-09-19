package com.example.adsxml.ads

import android.content.Context
import com.easy.supabase.SdkSupaBase
import com.monetization.adsmain.commons.addNewController
import com.monetization.adsmain.commons.getAdController
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.SdkConfigs
import com.monetization.core.listeners.ControllersListener
import com.monetization.interstitials.AdmobInterstitialAdsManager
import com.monetization.nativeads.AdmobNativeAdsManager

object AdsEntryManager {


    fun initAds(context: Context, supaBase: SdkSupaBase) {
        val listener = object : ControllersListener {
            override fun onAdImpression(
                adKey: String,
                adType: AdType,
                dataMap: HashMap<String, String>
            ) {
                val controller = adType.getAdController(adKey)!!
                supaBase.onAdImpression(
                    userId = SdkConfigs.getUserId(context),
                    adId = controller.getAdId(),
                    adKey = adKey,
                    adType = adType,
                    dataMap = dataMap,
                )
                super.onAdImpression(adKey, adType, dataMap)
            }
            override fun onAdFailed(
                adKey: String,
                adType: AdType,
                message: String,
                error: Int,
                dataMap: HashMap<String, String>
            ) {
                val controller = adType.getAdController(adKey)!!
                supaBase.onAdFailed(
                    userId = SdkConfigs.getUserId(context),
                    adId = controller.getAdId(),
                    adKey = adKey,
                    adType = adType,
                    dataMap = dataMap,
                    errorCode = error,
                    message = message
                )
                super.onAdFailed(adKey, adType, message, error, dataMap)
            }

            override fun onAdLoaded(
                adKey: String,
                adType: AdType,
                dataMap: HashMap<String, String>
            ) {
                val controller = adType.getAdController(adKey)!!
                supaBase.onAdLoaded(
                    userId = SdkConfigs.getUserId(context),
                    adId = controller.getAdId(),
                    adKey = adKey,
                    adType = adType,
                    dataMap = dataMap
                )
                super.onAdLoaded(adKey, adType, dataMap)
            }

            override fun onAdRequested(
                adKey: String,
                adType: AdType,
                dataMap: HashMap<String, String>
            ) {
                val controller = adType.getAdController(adKey)!!
                supaBase.onAdRequested(
                    userId = SdkConfigs.getUserId(context),
                    adId = controller.getAdId(),
                    adKey = adKey,
                    adType = adType,
                    dataMap = dataMap
                )
                super.onAdRequested(adKey, adType, dataMap)
            }
        }
        AdmobInterstitialAdsManager.addNewController(
            adKey = "Main",
            adIdsList = listOf(""),
            listener = listener
        )
        AdmobNativeAdsManager.addNewController(
            adKey = "Main",
            adIdsList = listOf(""),
            listener = listener
        )

    }
}