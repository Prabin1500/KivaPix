package com.example.kivapix.utils

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.Timestamp

object FireStoreProvider {
    val db: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }
}

data class Event(
    val id : String = "",
    val name: String = "",
    val location: String = "",
    val type : String = "",
    val participants : Int = 0,
    val date: Timestamp = Timestamp.now()
)