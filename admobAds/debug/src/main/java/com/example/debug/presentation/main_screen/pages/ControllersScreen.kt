package com.example.debug.presentation.main_screen.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.debug.R
import com.example.debug.SdkDebugHelper.capFirstWord
import com.example.debug.presentation.DebugState
import com.example.debug.presentation.calculateMatchRates
import com.example.debug.presentation.calculateShowRates
import com.example.debug.presentation.getLoadedAdsCount
import com.example.debug.presentation.getNoFillsAdsCount
import com.example.debug.presentation.getTotalActiveAdUnits
import com.example.debug.presentation.getTotalImpressions
import com.example.debug.presentation.getTotalRequests
import com.example.debug.presentation.main_screen.components.ControllerItem
import com.example.debug.presentation.main_screen.components.TwoItemsRow
import com.monetization.core.ad_units.core.AdType

@Composable
fun ControllersScreen(state: DebugState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {

        var selectedAdType by remember {
            mutableStateOf<AdType?>(null)
        }
        Spacer(modifier = Modifier.height(30.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .horizontalScroll(rememberScrollState())
        ) {
            AdType.entries.forEach { type ->
                val isSelected = type == selectedAdType
                val modifier = if (isSelected) {
                    Modifier
                        .padding(horizontal = 8.dp)
                        .defaultMinSize(100.dp, 40.dp)
                        .background(
                            color = colorResource(id = R.color.green),
                            RoundedCornerShape(10)
                        )

                } else {
                    Modifier
                        .padding(horizontal = 8.dp)
                        .defaultMinSize(100.dp, 40.dp)
                        .border(
                            1.dp,
                            color = colorResource(id = R.color.black),
                            RoundedCornerShape(10)
                        )
                }
                Box(
                    modifier = modifier
                        .clickable {
                            selectedAdType = if (selectedAdType == type) {
                                null
                            } else {
                                type
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = type.name.capFirstWord(),
                        modifier = Modifier
                            .padding(10.dp),
                        color = if (isSelected) {
                            colorResource(id = R.color.white)
                        } else {
                            colorResource(id = R.color.black)
                        }
                    )
                }
            }
        }


        val list = if (selectedAdType == null) {
            state.controllers
        } else {
            state.controllers.filter {
                it.controller.getAdType().name == selectedAdType?.name
            }
        }.filter {
            it.controller.getAdKey() != "Test" && if (selectedAdType == null) {
                true
            } else {
                selectedAdType == it.controller.getAdType()
            }
        }
            .sortedByDescending {
                it.noOfRequest
            }
        Spacer(modifier = Modifier.height(30.dp))
        LazyColumn {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 6.dp, vertical = 6.dp),
                    colors = CardDefaults.cardColors(colorResource(id = R.color.white)),
                    shape = RectangleShape,
                    elevation = CardDefaults.cardElevation(0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        TwoItemsRow(
                            key = "Active Units",
                            value = list.getTotalActiveAdUnits().toString() + "/" + list.size
                        )
                        TwoItemsRow(key = "Requests", value = list.getTotalRequests().toString())
                        TwoItemsRow(key = "Matched", value = list.getLoadedAdsCount().toString())
                        TwoItemsRow(key = "Failed", value = list.getNoFillsAdsCount().toString())
                        TwoItemsRow(
                            key = "Impressions",
                            value = list.getTotalImpressions().toString()
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        TwoItemsRow(key = "Show Rates", value = "${list.calculateShowRates()}%")
                        TwoItemsRow(key = "Match Rates", value = "${list.calculateMatchRates()}%")
                    }
                }
            }
            item {
                Text(
                    text = "Controllers",
                    fontSize = 18.sp,
                    color = colorResource(id = R.color.green),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                )
            }
            items(list) {
                ControllerItem(it)
            }
        }
    }
}