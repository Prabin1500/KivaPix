package com.example.kivapix.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.kivapix.R
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition

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
            NoEvent()
        }
    }

}

@Composable
fun NoEvent(){
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        elevation = CardDefaults.cardElevation(6.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
        ) {
            Icon(Icons.Default.CalendarToday, contentDescription = "Location", tint = Color.Black)
            Spacer(modifier = Modifier.width(16.dp))

            Column{
                Text(
                    text = "No upcoming events",
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleLarge,
                )
                Text(
                    text = "You have not accepted any events yet. Events that you have accepted as a photographer will appear here.",
                    fontWeight = FontWeight.Light,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "Select an event",
                    fontWeight = FontWeight.Medium,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 8.dp),
                    color = Color(0xFF047E2D),
                )
            }
        }
    }
}