package com.example.kivapix.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarEventScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 8.dp,  // Shadow effect
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .height(50.dp)
            ) {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "My Events",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047E2D),
                                modifier = Modifier.weight(1f)
                            )
                            Icon(Icons.Default.CalendarMonth,
                                contentDescription = "Calender",
                                tint = Color(0xFF047E2D),
                                modifier = Modifier.padding(end = 12.dp))
                        }
                    }
                )
            }
        },
        bottomBar = {
            BottomAppBar(navController)
        }
    ) { padding ->
        /*LazyColumn(modifier = Modifier.padding(padding)) {
            items(events.size) { i ->
                EventCard(events[i], navController)
            }
        }*/
        Column(modifier = Modifier.padding(padding)) {

        }
    }

}