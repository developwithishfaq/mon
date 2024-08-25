package com.monetization.adsmain.sdk

import android.app.Application
import com.monetization.appopen.AdmobAppOpenAdsManager
import com.monetization.appopen.AppOpenListener

object AppOpenAdsHelper  {
    fun initOpensAds(
        onShowAppOpenAd: () -> Unit,
        canShowAppOpenAd: () -> Boolean
    ) {
        AdmobAppOpenAdsManager.initAppOpen(object : AppOpenListener {
            override fun onShowAd() {
                if (canShowAppOpenAd.invoke()) {
                    onShowAppOpenAd.invoke()
                }
            }
        })
    }
    /*
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
            currentActivity = activity
        }

        override fun onActivityStarted(activity: Activity) {
            isAppInPause = false
            currentActivity = activity
        }

        override fun onActivityResumed(activity: Activity) {
            isAppInPause = false
            currentActivity = activity
        }

        override fun onActivityPaused(activity: Activity) {
            isAppInPause = true
            currentActivity = activity
        }

        override fun onActivityStopped(activity: Activity) {

        }

        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        }

        override fun onActivityDestroyed(activity: Activity) {
            currentActivity = null
            isAppInPause = false
        }*/
}