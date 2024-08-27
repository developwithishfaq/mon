package com.example.adsxml.base

import android.app.Application
import com.example.debug.DebugListener
import com.example.debug.SdkDebugHelper

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()
        SdkDebugHelper.initDebugMode(applicationContext,object : DebugListener {
            override fun canLaunchDebugActivity(): Boolean {
                return true
            }
        })
    }
}