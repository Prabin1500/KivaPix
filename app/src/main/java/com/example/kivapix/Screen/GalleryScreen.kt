package com.example.kivapix.Screen

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.size.Size
import com.example.kivapix.R
import com.example.kivapix.utils.StorageManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GalleryScreen(navController: NavHostController, eventName : String?) {
    var showBottomSheet by remember { mutableStateOf(false) }
    var selectedOption by remember { mutableStateOf("") }
    val robotoFontFamily = FontFamily(
        Font(R.font.robotomono_bold),
    )
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var uploadStatus by remember { mutableStateOf<String?>(null) }
    var progress by remember { mutableStateOf(0f) }
    var isUploading by remember { mutableStateOf(false) }

    // Image Picker
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            imageUri = uri
            isUploading = true // Start uploading
            uploadImageToFirebase(uri,
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
                                modifier = Modifier.padding(start = 16.dp)
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
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
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

fun uploadImageToFirebase(fileUri: Uri, onProgress: (Float) -> Unit, onComplete: (String) -> Unit) {
    StorageManager.uploadFile(
        fileUri,
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
fun UploadProgressIndicator(isUploading: Boolean, progress: Float) {
    if (isUploading) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Uploading image: ${progress.toInt()}%",
                    style = MaterialTheme.typography.bodyLarge,
                )
                Spacer(modifier = Modifier.height(16.dp))
                CircularProgressIndicator(
                    progress = progress / 100f,
                    modifier = Modifier.size(80.dp)
                )
            }
        }
    }
}


