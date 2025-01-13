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
                    val event = document.toObject(Event::class.java).copy(id = document.id)
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

    fun getEventByDocumentId(eventId: String, callback: (Event?) -> Unit) {
        db.collection("Event")
            .document(eventId)  // Get the document by its ID
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val event = document.toObject(Event::class.java)  // Map Firestore document to event object
                    Log.d("TAG", "Event fetched successfully: ${event?.name}")
                    callback(event)  // Pass the event object to the callback
                } else {
                    Log.w("TAG", "No event found with ID: $eventId")
                    callback(null)  // Pass null if the event is not found
                }
            }
            .addOnFailureListener { exception ->
                Log.w("TAG", "Error getting event by ID", exception)
                callback(null)  // Pass null in case of error
            }
    }
}