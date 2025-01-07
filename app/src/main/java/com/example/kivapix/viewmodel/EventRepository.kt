package com.example.kivapix.viewmodel

import android.util.Log
import com.example.kivapix.utils.Event
import com.example.kivapix.utils.FireStoreProvider

class EventRepository {
    private val db = FireStoreProvider.db

    fun getAllEvents(callback: (List<Event>) -> Unit) {
        db.collection("Event")
            .get()  // Get all documents in the "event" collection
            .addOnSuccessListener { documents ->
                val eventList = mutableListOf<Event>()

                for (document in documents) {
                    val event = document.toObject(Event::class.java)  // Map Firestore document to event object
                    eventList.add(event)
                }
                // Pass the list of event to the callback
                Log.d("TAG", "Events fetched successfully: ${eventList.size} events found.")
                callback(eventList)
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting events", exception)
            }
    }
}