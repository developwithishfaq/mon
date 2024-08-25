package com.example.debug.presentation.main_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.debug.R
import com.example.debug.SdkDebugHelper.capFirstWord
import com.example.debug.formatMillisToTime
import com.monetization.core.models.AdmobAdInfo
import com.monetization.core.models.Failed
import com.monetization.core.models.Loaded

@Composable
fun RequestCard(admobAdInfo: AdmobAdInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        colors = CardDefaults.cardColors(colorResource(id = R.color.white)),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = admobAdInfo.adKey,
                        fontSize = 15.sp,
                        color = colorResource(id = R.color.black)
                    )
                    Text(text = " (Try: ${admobAdInfo.requestCount}) - ")
                    Text(
                        text = admobAdInfo.adType.toString().capFirstWord(),
                        color = colorResource(id = R.color.green)
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Requested At ${admobAdInfo.adRequestTime.formatMillisToTime()}")
                Spacer(modifier = Modifier.height(15.dp))
                when (admobAdInfo.adFinalTime) {
                    is Loaded -> {
                        Text(text = "Results In  ${((admobAdInfo.adFinalTime as Loaded).time - admobAdInfo.adRequestTime) / 1000} Seconds")
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Ad Loaded",
                            fontSize = 15.sp,
                            color = colorResource(id = R.color.green)
                        )
                        Spacer(modifier = Modifier.height(25.dp))
                        admobAdInfo.adImpressionTime?.let {
                            Text(
                                text = "Impression At ${it.formatMillisToTime()}",
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Gap After Loaded : ${(it - (admobAdInfo.adFinalTime as Loaded).time) / 1000} Seconds",
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "Gap After Request : ${(it - admobAdInfo.adRequestTime) / 1000} Seconds",
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                    }

                    is Failed -> {
                        val info = admobAdInfo.adFinalTime as Failed
                        Text(text = "Results In  ${((info).time - admobAdInfo.adRequestTime) / 1000} Seconds")
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Failed: message=${info.error},code=${info.code}",
                            color = colorResource(id = R.color.red)
                        )
                    }

                    else -> {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth(),
                            color = colorResource(id = R.color.green)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .padding(horizontal = 10.dp)
            ) {
                when (admobAdInfo.adFinalTime) {
                    is Loaded -> {
                        ColorBox(color = R.color.green)
                    }

                    is Failed -> {
                        ColorBox(color = R.color.red)
                    }

                    null -> {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

@Composable
fun ColorBox(color: Int) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .background(color = colorResource(id = color), RoundedCornerShape(10)),
        contentAlignment = Alignment.Center
    ) {

    }
}