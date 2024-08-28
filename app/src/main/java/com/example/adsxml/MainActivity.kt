package com.example.adsxml

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.adsxml.databinding.ActivityMainBinding
import com.monetization.core.firebase.Sdk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        CoroutineScope(Dispatchers.IO).launch {
            var count = 0
            while (true) {
                count += 1
                delay(1000)
                Sdk.sendEvent(this@MainActivity, "Hi Hello $count")
            }
        }


    }
}