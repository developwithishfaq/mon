package com.example.debug

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.debug.databinding.DebugActivityBinding
import com.example.debug.history.DebugHistoryAdapter
import com.example.debug.presentation.main_screen.MainScreenDebug

var canShowDebugActivity = true

private val LocalNavHostController = compositionLocalOf<NavHostController> {
    error("")
}

class DebugAdsHistoryActivity : ComponentActivity() {

    private lateinit var historyAdapter: DebugHistoryAdapter

    private lateinit var binding: DebugActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            CompositionLocalProvider(LocalNavHostController provides navController) {
                MainScreenDebug()
            }
        }
        /*

                binding = DebugActivityBinding.inflate(layoutInflater)
                setContentView(binding.root)

                historyAdapter = DebugHistoryAdapter()
                binding.recycler.apply {
                    adapter = historyAdapter
                    layoutManager = LinearLayoutManager(this@DebugAdsHistoryActivity)
                }

                lifecycleScope.launch {
                    AdsManagerHistoryHelper.getHistory().collectLatest { list ->
                        Log.d("cvv", "onCreate: ${list.size},$list")

                        val pendingAndResults = list.partition {
                            it.adFinalTime == null
                        }
                        binding.requestCount.text = list.size.toString()
                        binding.pendingCount.text = pendingAndResults.first.size.toString()
                        if (pendingAndResults.second.isNotEmpty()) {
                            val loadedAndNoFill = pendingAndResults.second.partition {
                                it.adFinalTime is Loaded
                            }
                            binding.filledCount.text = loadedAndNoFill.first.size.toString()
                            binding.noFillsCount.text = loadedAndNoFill.second.size.toString()
                        }

                        historyAdapter.submitList(list)
                    }
                }
        */

    }

    override fun onDestroy() {
        super.onDestroy()
        canShowDebugActivity = true
    }
}