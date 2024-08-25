package com.example.debug.presentation.main_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.debug.R
import com.example.debug.SdkDebugHelper.capFirstWord
import com.example.debug.presentation.ControllersInfo

@Composable
fun ControllerItem(info: ControllersInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 6.dp, vertical = 5.dp),
        colors = CardDefaults.cardColors(colorResource(id = R.color.white)),
        shape = RectangleShape,
        elevation = CardDefaults.cardElevation(0.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp)
        ) {
            val adsController = info.controller
            Text(
                text = adsController.getAdType().name.capFirstWord(),
                fontSize = 14.sp,
                color = colorResource(id = R.color.black)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = adsController.getAdKey(),
                fontSize = 15.sp,
                color = colorResource(id = R.color.black)
            )
            Spacer(modifier = Modifier.height(5.dp))
            Column {
                adsController.getAdIdsList().forEach {
                    Text(
                        text = it,
                        fontSize = 12.sp,
                        color = colorResource(id = R.color.black),
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 3.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(25.dp))
            if (info.noOfRequest > 0) {
                Column {
                    TwoItemsRow(key = "Requests", value = info.noOfRequest.toString())
                    TwoItemsRow(key = "Matched", value = info.noOfLoadedAds.toString())
                    TwoItemsRow(key = "Failed", value = info.noOfNoFills.toString())
                    Spacer(modifier = Modifier.height(8.dp))
                    TwoItemsRow(key = "Impressions", value = info.noOfImpressions.toString())
                    TwoItemsRow(
                        key = "Show Rates",
                        value = info.calculateShowRates().toString() + "%"
                    )
                    TwoItemsRow(
                        key = "Match Rates",
                        value = info.calculateMatchRates().toString() + "%"
                    )
                }
            }
        }
    }
}

@Composable
fun TwoItemsRow(key: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = key,
            color = colorResource(id = R.color.black),
            fontSize = 13.sp
        )
        Text(
            text = value,
            color = colorResource(id = R.color.black),
            fontSize = 15.sp
        )
    }
}