package com.example.debug

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.debug.presentation.main_screen.MainScreenDebug

var canShowDebugActivity = true

private val LocalNavHostController = compositionLocalOf<NavHostController> {
    error("")
}

class DebugAdsHistoryActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            CompositionLocalProvider(LocalNavHostController provides navController) {
                MainScreenDebug()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        canShowDebugActivity = true
    }
}