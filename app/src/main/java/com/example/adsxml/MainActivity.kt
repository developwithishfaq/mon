package com.example.adsxml

import android.os.Bundle
import androidx.activity.ComponentActivity
import com.example.adsxml.databinding.ActivityMainBinding
import com.monetization.adsmain.commons.addNewController
import com.monetization.adsmain.commons.loadAd
import com.monetization.adsmain.splash.AdmobSplashAdController
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.utils.dialog.SdkDialogs
import com.monetization.core.utils.dialog.showNormalLoadingDialog
import com.monetization.interstitials.AdmobInterstitialAdsManager
import com.monetization.interstitials.extensions.InstantInterstitialAdsManager
import com.monetization.interstitials.extensions.PreloadInterstitialAdsManager
import org.koin.android.ext.android.inject
import video.downloader.remoteconfig.SdkRemoteConfigConstants.toConfigString

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityMainBinding

    private val splashAdController: AdmobSplashAdController by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AdmobInterstitialAdsManager.addNewController(
            "Splash", listOf("")
        )
        AdmobInterstitialAdsManager.addNewController(
            "NewInter", listOf("", "","","")
        )
        binding.preloadAd.setOnClickListener {
            "NewInter".loadAd(AdType.INTERSTITIAL, this@MainActivity)
        }
        val dialog = SdkDialogs(this)
        binding.showAd.setOnClickListener {
            PreloadInterstitialAdsManager.tryShowingInterstitialAd(
                true.toConfigString(),
                key = "NewInter",
                activity = this@MainActivity,
                onLoadingDialogStatusChange = {
                    if (it) {
                        dialog.showNormalLoadingDialog()
                    } else {
                        dialog.hideLoadingDialog()
                    }
                },
                onAdDismiss = {

                },
                requestNewIfAdShown = true,
                requestNewIfNotAvailable = true
            )/*
            InstantInterstitialAdsManager.showInstantInterstitialAd(true.toConfigString(),
                this@MainActivity,
                "NewInter",
                onLoadingDialogStatusChange = {
                    if (it) {
                        dialog.showNormalLoadingDialog()
                    } else {
                        dialog.hideLoadingDialog()
                    }
                })*/
        }
        /*
                splashAdController.showSplashAd(
                    enableKey = true.toConfigString(),
                    adType = SplashAdType.AdmobInter("Splash"),
                    activity = this,
                    timeInMillis = 10_000,
                    lifecycle = lifecycle,
                    normalLoadingTime = 10_000,
                    normalLoadingDialog = {
                        dialog.showNormalLoadingDialog()
                    },
                    callBack = object : FullScreenAdsShowListener {
                        override fun onAdShown(adKey: String) {
                            super.onAdShown(adKey)
                            dialog.hideLoadingDialog()
                        }

                        override fun onAdDismiss(adKey: String, adShown: Boolean, rewardEarned: Boolean) {
                            super.onAdDismiss(adKey, adShown, rewardEarned)
                            Toast.makeText(this@MainActivity, "Ad Shown=$adShown", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                )*/

    }
}