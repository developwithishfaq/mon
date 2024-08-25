## Setup
> Step 1: Add it to your build.gradle/setting gradle (project):
```gradle
allprojects {
    repositories {
        maven {
            url = URI("https://jitpack.io")
        }
    }
}
```
> Step 2: Add it to your build.gradle (app)

```gradle
dependencies {
     implementation("com.github.developwithishfaq:admob-ads:0.7")
}
```

# Ishfaq Ads Sdk
Using this library we can Easily Implement Admob Ads.

**App Id**
Add Your Admob Original App Id Like This Inside Manifest <application 
```
  <!-- Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713 -->
  <meta-data
    android:name="com.google.android.gms.ads.APPLICATION_ID"
    android:value="ca-app-pub-3940256099942544~3347511713"/>
```


**Sdk Intialization**
```gradle
  // I am using koin for di
  private val adsManager: IshfaqAdsSdk by inject()
  // Call this Function For Ad Network's Intializations
  adsManager.initAdsSdk(mContext) {
  }
```
## Native/Banner Ads Implementation
At First create an instance of IshfaqNativeAdsManager/IshfaqBannerAdsManager.
```
  // Make Sure These Instance Are Singelton
  private val nativeAdsManager: IshfaqNativeAdsManager by inject()
  private val bannerAdsManager: IshfaqBannerAdsManager by inject()
```
Now Create Controllers For Your Native/Banner Ads.
```
  //Native
  nativeAdsManager.addNewController(
    adKey = "MainNative",
    adId = IshfaqConfigs.TestNativeId
  )

  //Banner
  bannerAdsManager.addNewController(
    adKey = "MainBanner",
    adId = IshfaqConfigs.TestBannerId
  )
```
Now Lets Show Native/Banner Ads
We Made Easy For You To Implement Native Ads On Your Screens, Just Extend Your Activity With Sdk's **IshfaqNativeAdsActivity/IshfaqBannerAdsActivity**, and call this funtion.

```
  // Show Native Ad

  showNativeAd(
    key = "MainNative",
    layoutName = NativeTemplates.TemplateTwo, // Your Ad Layout Name 
    enabled = true,
    adFrame = binding.adFrame,
    showShimmerLayout = true,
    oneTimeUse = true, // One time use means, every time u visit this screen new ad will be loaded
    nativeAdsManager = nativeAdsManager
  )
  // Show Banner Ad 

  showBannerAd(
    key = "MainBanner",
    bannerType = BannerAdSizes.MediumRectangle,
    enabled = true,
    adFrame = binding.adFrame,
    showShimmerLayout = true,
    bannersManager = bannerAdsManager
  )
```
Each paramteres is self descriptive.

## Interstitial Ads Implementation
At First create an instance of IshfaqInterstitialAdsManager.
```
  private val interAdsManager: IshfaqInterstitialAdsManager by inject()

  // Create Controller
  interAdsManager.addNewController(
    adKey = "MainInter",
    adId = IshfaqConfigs.TestInterId
  )
```
**Loading Interstitials**
```
  val controller = interAdsManager.getAdController("MainInter")
  controller?.loadAd(context = this@MainActivity, callback = null)
```
**Showing Interstitials**<br>
We can show an interstitial ad like that
```
  val controller = interAdsManager.getAdController("MainInter")
  val interAd = controller?.getAvailableAd() as? IshfaqInterstitialAd
  interAd?.showInter(
      context = this@MainActivity,
      callBack = object : FullScreenAdsShowListener {
          override fun onAdShownFailed() {
              super.onAdShownFailed()
          }

          override fun onAdDismiss() {
              super.onAdDismiss()
          }

          override fun onAdShown() {
              super.onAdShown()
          }

          override fun onAdClick() {
              super.onAdClick()
          }
      }
  )
```
This is good when you want all callbacks, But this is kind of lengthy way, so for that reason there
is a function to show interstitial ad called **tryShowingInterstitialAd()**.
```
   interAdsManager.tryShowingInterstitialAd(
     enable = true,
     key = "MainInter",
     context = this@MainActivity,
     requestNewIfNotAvailable = true,
     requestNewIfAdShown = true,     
     onAdDismiss = { adShown: Boolean ->
     }
   )
```
This function will try to show an interstitial but if ad is not available this will call onDismiss() so your app flow 
didn't disturb.<br>
There are some extra paramters like **requestNewIfNotAvailable** it means when ad is not available , this will call load ad
against that key so next time ad must be ready to show, second interesting param is **requestNewIfAdShown**, this means 
that if ad shown then also this will load a new ad so after showing first ad there will be another ad ready on that key.
## App Open Ads Implementation
At First create an instance of IshfaqAppOpenAdsManager.
```
  private val appOpenAdsManager: IshfaqAppOpenAdsManager by inject()
  // Create Controller
  appOpenAdsManager.addNewController(
    adKey = "MainAppOpen",
    adId = IshfaqConfigs.TestAppOpenId
  )
```
**Loading AppOpen**
```
  val controller = appOpenAdsManager.getAdController("MainAppOpen")
  controller?.loadAd(context = activity, callback = null)
```
**Showing AppOpen**<br>
As App Opens Are Commonly Shown at When User Opens App From Pause State,implementing app open adds is kind of complex,
So for that we made the whole integration easy.

Extend Your App Class With **IshfaqBaseApp**
```
  class BaseApp : IshfaqBaseApp() {
      // I Used Koin Di to inject it , you can use hilt too, But keep instance as Singelton
      private val appOpenAdsManager: IshfaqAppOpenAdsManager by inject()

      override fun onCreate() {
           super.onCreate()
           // Must Call This , it will attach listeners.
           initAppOpenAds(appOpenAdsManager)
      }
      override fun onShowAppOpenAd(activity: Activity) {

      }
      override fun canShowAppOpenAd(activity: Activity): Boolean {
         return true // Your Condition, like if you dont want to show ad when user is on Splash -> activity !is SplashActivity
      }  
  }
```
This will override two functions. But Also remeber to call initAppOpenAds(appOpenAdsManager), this will attach to process lifecycle of Application<br>
**onShowAppOpenAd**<br><br>
This is called after approval of **canShowAppOpenAd**.<br>
So here in this function we have to write actual code for showing App Open Ad

```
override fun onShowAppOpenAd(activity: Activity) {
    val controller = appOpenAdsManager.getAdController("MainAppOpen")
    controller?.let { controller ->
        if (controller.isAdAvailable()) {
            (controller.getAvailableAd() as? AdmobAppOpenAd)?.showAppOpen(
                context = activity,
                callBack = object : FullScreenAdsShowListener {
                    override fun onAdShown() {

                    }

                    override fun onAdDismiss() {
                        // Destroy Shown Ad
                        controller.destroyAd(activity)
                    }
                }
            )
        } else {
            controller.loadAd(activity, null)
        }
    }
}
```
This will show ad when ad is available , in other case this will request an ad.



