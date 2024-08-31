package com.example.adsxml.base

import android.app.Application
import com.example.debug.DebugListener
import com.example.debug.SdkDebugHelper
import com.monetization.composeviews.statefull.nativeAd.SdkNativeViewModel
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.SdkConfigs
import com.monetization.core.listeners.SdkListener
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        val module = module {
            viewModel {
                SdkNativeViewModel()
            }
        }
        startKoin {
            modules(module)
            androidContext(applicationContext)
        }

        SdkDebugHelper.initDebugMode(applicationContext, object : DebugListener {
            override fun canLaunchDebugActivity(): Boolean {
                return true
            }
        })
        SdkConfigs.setListener(object : SdkListener {
            override fun canShowAd(adType: AdType, adKey: String): Boolean {
                return true
            }

            override fun canLoadAd(adType: AdType, adKey: String): Boolean {
                return true
            }
        }, true)
    }
}