package com.example.kivapix.Screen.Gallery

import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.kivapix.R
import com.example.kivapix.Screen.BottomAppBar
import com.example.kivapix.utils.StorageManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(navController: NavHostController, documentId : String?) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }
    val robotoFontFamily = FontFamily(
        Font(R.font.robotomono_bold),
    )
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var uploadStatus by remember { mutableStateOf<String?>(null) }
    var progress by remember { mutableStateOf(0f) }
    var isUploading by remember { mutableStateOf(false) }
    var imageUrls by remember { mutableStateOf<List<String>>(emptyList()) }
    var isDownloading  by remember { mutableStateOf(true) }

    LaunchedEffect(documentId) {
        StorageManager.retrieveFile(
            folderName = documentId,
            onSuccess = { urls ->
                imageUrls = urls
                isDownloading = false
            },
            onFailure = {
                isDownloading = false
            }
        )
    }
    // Image Picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            isUploading = true // Start uploading
            uploadImageToFirebase(uri, documentId,
                onProgress = { progressValue ->
                    progress = progressValue
                },
                onComplete = { status ->
                    isUploading = false // Stop uploading
                    uploadStatus = status
                }
            )
        }
    }

    Scaffold(
        bottomBar = { BottomAppBar(navController) },
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
                                text = "Photo Gallery",
                                style = MaterialTheme.typography.titleLarge,
                                fontFamily = robotoFontFamily,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                modifier = Modifier.padding(start = 5.dp)
                            )
                        }
                    }
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showBottomSheet = true },
                containerColor = Color(0xFF2AC9BA)
            ) {
                Icon(Icons.Filled.PhotoCamera, contentDescription = "Camera Icon")
            }
        }
    ) {padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)) {
            // Modal Bottom Sheet
            if (showBottomSheet) {
                OptionBottomSheet(
                    onDismiss = { showBottomSheet = false },
                    onOptionSelected = { option ->
                        selectedOption = option
                        if (option == "Gallery") {
                            imagePickerLauncher.launch("image/*")
                        }
                        showBottomSheet = false
                    }
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                UploadProgressIndicator(isUploading = isUploading, progress = progress)
                GalleryView(isDownloading = isDownloading, imageUrls = imageUrls)
            }
            if (uploadStatus != null) {
                Snackbar(
                    action = {
                        Text(
                            "Dismiss",
                            color = Color.White,
                            modifier = Modifier.clickable { uploadStatus = null }
                        )
                    },
                    modifier = Modifier.padding(16.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Text(text = "Image uploaded successfully", color = Color.White)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OptionBottomSheet(onDismiss: () -> Unit, onOptionSelected: (String) -> Unit) {
    val robotoFontFamily = FontFamily(
            Font(R.font.robotomono_bold),
    )
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Choose an Action",
                fontFamily = robotoFontFamily,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            OptionCard(
                text = "Open Camera",
                icon = Icons.Filled.PhotoCamera,
                iconTint = Color(0xFF6200EE),
                onClick = { onOptionSelected("Camera") }
            )

            OptionCard(
                text = "Select from Gallery",
                icon = Icons.Filled.PhotoLibrary,
                iconTint = Color(0xFF4CAF50),
                onClick = { onOptionSelected("Gallery") }
            )
        }
    }
}

@Composable
fun OptionCard(
    text: String,
    icon: ImageVector,
    iconTint: Color,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = text,
                tint = iconTint,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = text,
                fontWeight = FontWeight.Medium,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}




