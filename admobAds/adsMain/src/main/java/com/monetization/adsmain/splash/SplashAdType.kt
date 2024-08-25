package com.monetization.adsmain.splash

/*enum class SplashAdType() {
    None,
    AdmobInter,
    AdmobAppOpen
}*/
sealed class SplashAdType {
    data object None : SplashAdType()
    data class AdmobInter(val key: String) : SplashAdType()
    data class AdmobAppOpen(val key: String) : SplashAdType()
}