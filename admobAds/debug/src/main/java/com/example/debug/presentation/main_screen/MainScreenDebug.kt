package com.example.debug.presentation.main_screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.debug.GenericViewModelFactory
import com.example.debug.R
import com.example.debug.presentation.DebugViewModel
import com.example.debug.presentation.main_screen.pages.ControllersScreen
import com.example.debug.presentation.main_screen.pages.RecentRequestsScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MainScreenDebug(
    viewModel: DebugViewModel = viewModel(
        factory = GenericViewModelFactory(DebugViewModel::class.java) {
            DebugViewModel()
        }
    ),
) {
    val state by viewModel.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    val pagerState = rememberPagerState {
        2
    }
    Column(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
        ) {
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Dashboard",
                    fontSize = 20.sp,
                    color = colorResource(id = R.color.black)
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            HorizontalPager(state = pagerState, userScrollEnabled = false) { page ->
                when (page) {
                    0 -> {
                        RecentRequestsScreen(state)
                    }

                    1 -> {
                        ControllersScreen(state)
                    }
                }
            }

        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = colorResource(id = R.color.white))
                .padding(vertical = 10.dp, horizontal = 10.dp)
        ) {
            Item(text = "Requests") {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(0)
                }
            }
            Item(text = "Controllers") {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(1)
                }
            }
        }
    }
}

@Composable
fun RowScope.Item(text: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            modifier = Modifier
                .padding(horizontal = 10.dp, vertical = 10.dp)
                .clickable {
                    onClick.invoke()
                }
        )
    }
}