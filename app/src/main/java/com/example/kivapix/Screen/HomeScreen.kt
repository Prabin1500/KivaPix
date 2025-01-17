package com.example.kivapix.Screen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.kivapix.R
import com.example.kivapix.utils.Event
import com.example.kivapix.viewmodel.EventRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(navController: NavHostController) {
    val robotoFontFamily = FontFamily(
        Font(R.font.robotomono_bold),
    )
    val eventRepository = EventRepository()
    val eventState = remember { mutableStateOf<List<Event>>(emptyList()) }
    var isloading  by remember { mutableStateOf(true) }

    // Fetch events from FireStore
    LaunchedEffect(Unit) {
        eventRepository.getAllEvents { e ->
            eventState.value = e
            isloading = false
        }
    }
    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 12.dp,
                modifier = Modifier
                    .height(40.dp)
            ) {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize(),
                        ) {
                            Text(
                                text = "KivaPix",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                fontFamily = robotoFontFamily,
                                color = Color(0xFF047E2D),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF8F9CFA)
                    )
                )
            }
        },
        bottomBar = {
            BottomAppBar(navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF8F9CFA),
                            Color(0xFFDBBDE7)
                        )
                    )
                )
        ) {
            SearchBar()
            PopularEvents()
            if(isloading){
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Events Loading...",
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }
            }else{
                LazyColumn(
                    modifier = Modifier
                        .padding(top = 16.dp, start = 16.dp, end = 16.dp)
                ) {
                    items(eventState.value.size) { event ->
                        HomeScreenEventCard(navController, eventState.value[event])
                    }
                }
            }
        }
    }
}

@Composable
fun PopularEvents() {
    val robotoFontFamily = FontFamily(
        Font(R.font.robotomono_bold),
    )
    Text(
        text = "Events Nearby",
        style = MaterialTheme.typography.titleLarge,
        fontFamily = robotoFontFamily,
        fontWeight = FontWeight.Bold,
        color = Color.Black,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp)
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun BottomAppBar(navController: NavHostController) {
    Row(
        modifier = Modifier
            .border(
                border = BorderStroke(
                    width = 0.5.dp, // Border width
                    color = Color.LightGray // Border color
                ),
            )
            .background(Color.White)
            .fillMaxWidth()
            .height(45.dp)
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        NavigationItem(navController, Screen.Home, painter = painterResource(id = R.drawable.home_icon), "Home")
        NavigationItem(navController, Screen.Calender, painter = painterResource(id = R.drawable.upcoming_event), "Calender")
        NavigationItem(navController, Screen.Profile, painter = painterResource(id = R.drawable.profile_icon), "Profile")
    }
}

@Composable
fun NavigationItem(
    navController: NavHostController,
    screen: Screen,
    painter: Painter,
    //icon: ImageVector,
    label: String
) {
    IconButton(
        onClick = { navController.navigate(screen.route) }
    ) {
        Icon(painter = painter, contentDescription = label)
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object Calender : Screen("Calender")
    object Profile : Screen("Profile")
}