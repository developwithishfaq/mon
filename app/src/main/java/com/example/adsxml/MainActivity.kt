package com.example.adsxml

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.adsxml.databinding.ActivityMainBinding
import com.monetization.adsmain.commons.addNewController
import com.monetization.adsmain.commons.loadAd
import com.monetization.adsmain.commons.sdkNativeAd
import com.monetization.adsmain.splash.AdmobSplashAdController
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.NativeTemplates
import com.monetization.core.utils.dialog.SdkDialogs
import com.monetization.interstitials.AdmobInterstitialAdsManager
import com.monetization.nativeads.AdmobNativeAdsManager
import com.remote.firebaseconfigs.SdkConfigListener
import com.remote.firebaseconfigs.SdkRemoteConfigController
import org.koin.android.ext.android.inject

private var TestEnabled = false

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
            "NewInter", listOf("", "", "", "")
        )
        AdmobNativeAdsManager.addNewController(
            "Native", listOf("", "", "", "")
        )
        val dialog = SdkDialogs(this)

        binding.fetchConfig.setOnClickListener {
            SdkRemoteConfigController.fetchRemoteConfig(
                defaultXml = R.xml.backup_rules,
                callback = object : SdkConfigListener {
                    override fun onDismiss() {

                    }

                    override fun onFailure(error: String) {

                    }

                    override fun onSuccess() {
                        assignConfigs()
                    }

                    override fun onUpdate() {
                    }
                },
                onUpdate = {
                    assignConfigs()
                }
            )
        }
        binding.preloadAd.setOnClickListener {
            "Native".loadAd(AdType.INTERSTITIAL, this@MainActivity)
        }
        binding.showAd.setOnClickListener {
            showNativeAd()
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


        /*
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
        /*
                PreloadInterstitialAdsManager.tryShowingInterstitialAd(
                    "NewInter",
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
                )*/
    }

    private fun assignConfigs() {
        TestEnabled = SdkRemoteConfigController.getRemoteConfigBoolean("TestEnabled")
        Toast.makeText(this, "TestEnabled=$TestEnabled", Toast.LENGTH_SHORT).show()
    }

    private fun showNativeAd() {
        binding.adFrame.sdkNativeAd(
            activity = this,
            adLayout = NativeTemplates.SmallNative,
            adKey = "Native",
            placementKey = "TestEnabled",
            showNewAdEveryTime = true,
            lifecycle = lifecycle
        )
    }
}