package com.example.kivapix

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.kivapix.Screen.CalendarEventScreen
import com.example.kivapix.Screen.EventDetailScreen
import com.example.kivapix.Screen.EventScreen
import com.example.kivapix.Screen.Gallery.GalleryScreen
import com.example.kivapix.ui.theme.KivaPixTheme
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    private lateinit var db: FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        db = FirebaseFirestore.getInstance()
        setContent {
            val navController = rememberNavController ()
            KivaPixTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = "Home"
                    ) {
                        composable("Home") {
                            EventScreen(navController)
                        }
                        composable("Calender") {
                            CalendarEventScreen(navController)
                        }
                        composable("Details/{documentId}") {navBackStackEntry ->
                            val eventId = navBackStackEntry.arguments?.getString("documentId")
                            EventDetailScreen(navController, eventId)
                        }
                        composable("Gallery/{eventName}") {navBackStackEntry ->
                            val name = navBackStackEntry.arguments?.getString("eventName")
                            GalleryScreen(navController, name)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KivaPixTheme {
        Greeting("Android")
    }
}