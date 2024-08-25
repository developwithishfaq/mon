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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import com.example.debug.R
import com.example.debug.SdkDebugHelper.capFirstWord
import com.example.debug.presentation.DebugState
import com.example.debug.presentation.main_screen.components.RequestCard
import com.monetization.core.ad_units.core.AdType

@Composable
fun RecentRequestsScreen(
    state: DebugState
) {

    var selectedAdType by remember {
        mutableStateOf<AdType?>(AdType.NATIVE)
    }

    val list = if (selectedAdType == null) {
        state.items
    } else {
        state.items.filter {
            it.adType.name == selectedAdType?.name
        }
    }
    Column {
        Spacer(modifier = Modifier.height(20.dp))
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
                            if (selectedAdType == type) {
                                selectedAdType = null
                            } else {
                                selectedAdType = type
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
        Spacer(modifier = Modifier.height(10.dp))
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
        ) {
            LazyColumn {
                items(list.reversed()) {
                    RequestCard(it)
                }
            }
        }
    }
}