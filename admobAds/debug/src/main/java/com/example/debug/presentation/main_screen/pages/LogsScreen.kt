package com.example.debug.presentation.main_screen.pages

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.debug.formatMillisToTime
import com.example.debug.presentation.DebugState

@Composable
fun LogsScreen(state: DebugState) {

    var searchText by rememberSaveable {
        mutableStateOf("")
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        var sorted by remember {
            mutableStateOf(false)
        }

        TextField(
            value = searchText, onValueChange = {
                searchText = it
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp)
        )
        val color = if (sorted) {
            Color.Green
        } else {
            Color.Gray
        }
        Box(
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 8.dp)
                .border(
                    1.dp, color, RoundedCornerShape(10)
                )
                .padding(horizontal = 18.dp, vertical = 8.dp)
                .clickable {
                    sorted = sorted.not()
                },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Sorted",
                fontSize = 12.sp,
                color = color
            )
        }

        val list = state.logs.filter {
            it.log.contains(searchText, true) || it.tag.contains(searchText, true)
        }

        LazyColumn {
            items(
                if (sorted) {
                    list.sortedBy {
                        it.recordedAt
                    }
                } else {
                    list.sortedByDescending {
                        it.recordedAt
                    }
                }
            ) { event ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    colors = CardDefaults.cardColors(Color.White),
                    shape = RectangleShape,
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 10.dp)
                    ) {
                        Text(
                            text = event.log,
                            color = if (event.isError) {
                                Color.Red
                            } else {
                                Color.Black
                            },
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = event.recordedAt.formatMillisToTime(),
                                color = Color.Black,
                                fontSize = 11.sp
                            )
                            Text(
                                text = event.tag,
                                color = Color.Black,
                                fontSize = 11.sp,
                                modifier = Modifier
                                    .padding(horizontal = 20.dp)
                            )
                        }
                    }
                }
            }
        }

    }
}