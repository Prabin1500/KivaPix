package com.example.kivapix.Screen.Gallery

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.kivapix.utils.StorageManager

fun uploadImageToFirebase(fileUri: Uri, folderName : String?, onProgress: (Float) -> Unit, onComplete: (String) -> Unit) {
    StorageManager.uploadFile(
        fileUri,
        folderName,
        onSuccess = { downloadUrl ->
            onComplete("Image uploaded successfully: $downloadUrl")
        },
        onFailure = { exception ->
            onComplete("Image upload failed: ${exception.message}")
        },
        onProgress = { progressValue ->
            onProgress(progressValue)
        }
    )
}

@Composable
fun GalleryView(isDownloading:Boolean, imageUrls: List<String>, navController: NavHostController){
    Log.d("TAG", "Downloading image: $isDownloading")
    if (isDownloading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text("Images Loading...")
            }
        }
    } else if (imageUrls.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Text("No images found")
            }
        }

    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3), // Adjust columns as needed
            modifier = Modifier.padding(5.dp)
        ) {
            items(imageUrls.size) { imageUrl ->
                ImageCard(imageUrl = imageUrls[imageUrl], navController)
            }
        }
    }
}

@Composable
fun ImageCard(imageUrl: String, navController: NavHostController) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable {
                navController.navigate("SingleImage/${Uri.encode(imageUrl)}")
            },
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),

        )
    }
}