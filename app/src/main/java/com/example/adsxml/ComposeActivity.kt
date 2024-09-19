package com.example.adsxml

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.Dialog
import com.easy.supabase.SdkSupaBase
import com.example.adsxml.ads.AdsEntryManager
import com.monetization.adsmain.commons.loadAd
import com.monetization.composeviews.SdkNativeAd
import com.monetization.composeviews.statefull.nativeAd.SdkNativeViewModel
import com.monetization.core.ad_units.core.AdType
import com.monetization.core.commons.NativeTemplates
import org.koin.android.ext.android.inject
import org.koin.androidx.compose.koinViewModel
import video.downloader.remoteconfig.SdkRemoteConfigConstants.toConfigString

class ComposeActivity : ComponentActivity() {

    private val supaBase: SdkSupaBase by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AdsEntryManager.initAds(this, supaBase)
        supaBase.initSupaBase("2341234123412341")
        
        setContent {
            val sdkNativeViewModel: SdkNativeViewModel = koinViewModel()
            var showDialog by rememberSaveable {
                mutableStateOf(false)
            }
            if (showDialog) {
                Dialog(onDismissRequest = {
                    sdkNativeViewModel.destroyAdByKey("Test")
                    showDialog = false
                }) {
                    Surface {
                        SdkNativeAd(
                            activity = this,
                            adLayout = NativeTemplates.LargeNative,
                            adKey = "Main",
                            placementKey = true.toConfigString(),
                            showNewAdEveryTime = true,
                            sdkNativeViewModel = sdkNativeViewModel
                        )
                    }
                }
            }
            Column {
                Button(onClick = {
//                    AdType.INTERSTITIAL.loadAd("Main", this@ComposeActivity)
                    showDialog = true
                }) {

                }
            }
        }
    }
}