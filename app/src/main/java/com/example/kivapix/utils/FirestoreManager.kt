package com.example.kivapix.utils

import android.net.Uri
import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.component1
import com.google.firebase.storage.component2

object FireStoreProvider {
    val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
}

object StorageManager {
    private val storage: FirebaseStorage by lazy {
        FirebaseStorage.getInstance()
    }
    private val storageRef: StorageReference by lazy {
        storage.reference
    }

    // Upload File to Firebase Storage
    fun uploadFile(
        fileUri: Uri,
        folderName: String? ,
        onProgress: (Float) -> Unit,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val fileRef = storageRef.child("$folderName/${fileUri.lastPathSegment}")
        fileRef.putFile(fileUri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString()) // Return the download URL
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
            .addOnProgressListener { (bytesTransferred, totalByteCount) ->
                val progress = ((100.0 * bytesTransferred) / totalByteCount).toFloat()
                onProgress(progress)
                Log.d("TAG", "Upload is $progress% done")
            }

    }

    // Download File from Firebase Storage
    fun retrieveFile(
        folderName: String?,
        onSuccess: (List<String>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val folderRef = storageRef.child("$folderName")
        folderRef.listAll()
            .addOnSuccessListener { result ->
                val urls = mutableListOf<String>()
                val tasks = result.items.map { fileRef ->
                    fileRef.downloadUrl
                        .addOnSuccessListener { url -> urls.add(url.toString()) }
                }
                // Wait for all URLs to be fetched
                Tasks.whenAllComplete(tasks)
                    .addOnSuccessListener { onSuccess(urls) }
            }
            .addOnFailureListener { exception -> onFailure(exception) }
    }

    // Delete File from Firebase Storage
    fun deleteFile(
        fileName: String,
        folderName: String = "uploads",
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val fileRef = storageRef.child("$folderName/$fileName")
        fileRef.delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }
}

data class Event(
    val id : String = "",
    val description : String = "",
    val name: String = "",
    val location: String = "",
    val type : String = "",
    val participants : Int = 0,
    val date: Timestamp = Timestamp.now()
)
