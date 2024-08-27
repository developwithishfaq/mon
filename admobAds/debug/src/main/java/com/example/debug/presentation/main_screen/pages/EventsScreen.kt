package com.example.debug.presentation.main_screen.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.debug.formatMillisToTime
import com.example.debug.presentation.DebugState

@Composable
fun EventsScreen(state: DebugState) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn {
            items(state.events) { event ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth(),
                    colors = CardDefaults.cardColors(Color.White),
                    shape = RectangleShape,
                    elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        Text(
                            text = event.event,
                            color = Color.Black,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = event.recorderAt.formatMillisToTime(),
                            color = Color.Black,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

    }
}