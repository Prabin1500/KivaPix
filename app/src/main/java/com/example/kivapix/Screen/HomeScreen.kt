package com.example.kivapix.Screen

import android.annotation.SuppressLint
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.kivapix.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(navController: NavHostController) {
    Scaffold(
        topBar = {
            Surface(
                shadowElevation = 8.dp,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .height(50.dp)
            ) {
                TopAppBar(
                    title = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                        ) {
                            Text(
                                text = "KIVAPIX",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF047E2D),
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
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
        ) {
            SearchBar()

        }
    }
}

@Composable
fun SearchBar() {
    var searchText by remember { mutableStateOf("") }
    val currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("'Today,' d MMMM"))
    Column {
        Row(modifier = Modifier.padding(end = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ){
            Column {
                Text(
                    text = "$currentDate",
                    fontSize = 14.sp,
                    color = Color(0xFF047E2D),
                    modifier = Modifier.padding(start = 16.dp, top = 16.dp)
                )
                Text(
                    text = "Good Morning, {User}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 16.dp, top = 10.dp, bottom = 10.dp)
                )
            }
            Spacer(Modifier.weight(1f))
            Image(
                painter = painterResource(id = R.drawable.profile),
                contentDescription = "Event Image",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(45.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(width = 1.dp, Color.LightGray, RoundedCornerShape(50.dp)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = Color.Gray,
                modifier = Modifier.padding(12.dp)
            )
            BasicTextField(
                value = searchText,
                onValueChange = { searchText = it },
                singleLine = true,
                textStyle = TextStyle(
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Start // Align the text to the start of the field
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp, end = 12.dp)
                    .fillMaxHeight(),
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.fillMaxSize()) {
                        if (searchText.isEmpty()) {
                            Text(
                                text = "Search events", // This is the hint text
                                color = Color.Gray,
                                style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                                    .align(Alignment.CenterStart)
                                    .padding(start = 4.dp)
                            )
                        }
                        Box(modifier = Modifier.align(Alignment.CenterStart)) {
                            innerTextField()
                        }
                    }
                }
            )
        }
    }
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
            .padding(5.dp)
            .clip(RoundedCornerShape(16.dp)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        NavigationItem(navController, Screen.Home, Icons.Default.Home, "Home")
        NavigationItem(navController, Screen.Calender, Icons.Default.Event, "Calender")
        NavigationItem(navController, Screen.Profile, Icons.Default.AccountCircle, "Profile")
    }
}

@Composable
fun NavigationItem(
    navController: NavHostController,
    screen: Screen,
    icon: ImageVector,
    label: String
) {
    IconButton(
        onClick = { navController.navigate(screen.route) }
    ) {
        Icon(imageVector = icon, contentDescription = label)
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("Home")
    object Calender : Screen("Calender")
    object Profile : Screen("Profile")
}