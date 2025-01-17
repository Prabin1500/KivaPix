package com.example.kivapix.Screen

import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.kivapix.utils.OpenPhotoFromRemoteURL

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleImageScreen(imageUrl: String?, navController: NavHostController) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    val openPhotoFromRemoteURL = remember(activity) { activity?.let { OpenPhotoFromRemoteURL(it) } }

    Log.d("TAG", "SingleImageScreen Activity is: $activity")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Image Viewer") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) { // Navigate back
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Display the image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f), // Allow the image to take up remaining space
                contentAlignment = Alignment.TopCenter
            ) {
                if (imageUrl != null) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Text(
                        text = "Image not available",
                        color = Color.Gray
                    )
                }
            }

            // Add two buttons at the bottom
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {
                    if (imageUrl != null && openPhotoFromRemoteURL != null) {
                        openPhotoFromRemoteURL.invoke(imageUrl)
                    } else {
                        Toast.makeText(context, "Cannot open editor. Missing activity or image URL.", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Edit")
                }
                Button(onClick = { /* Handle button 2 action */ }) {
                    Text("Print")
                }
            }
        }
    }
}

