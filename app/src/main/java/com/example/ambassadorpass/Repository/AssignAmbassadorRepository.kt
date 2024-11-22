package com.example.ambassadorpass.Repository

import android.util.Log
import com.example.ambassadorpass.Model.Party
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore

class AssignAmbassadorRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // Function to fetch parties from Firestore
    fun fetchParties(callback: (List<Party>) -> Unit) {
        firestore.collection("parties").get()
            .addOnSuccessListener { querySnapshot ->
                val parties = querySnapshot.documents.mapNotNull { doc ->
                    try {
                        val data = doc.data ?: return@mapNotNull null
                        Party(
                            partyName = data["partyName"] as? String ?: "",
                            partyId = doc.id,
                            partyDate = data["partyDate"] as? Timestamp, // Expecting Timestamp
                            partyDescription = data["partyDescription"] as? String ?: "",
                            partyLocation = data["partyLocation"] as? String ?: "",
                            ticketPrice = (data["ticketPrice"] as? Number)?.toInt() ?: 0,
                            ticketsAvailable = (data["ticketsAvailable"] as? Number)?.toInt() ?: 0,
                            ticketsSold = (data["ticketsSold"] as? Number)?.toInt() ?: 0
                        )
                    } catch (e: Exception) {
                        Log.e("FirestoreError", "Failed to parse party: ${doc.id}", e)
                        null
                    }
                }
                Log.d("FirestoreSuccess", "Fetched ${parties.size} parties")
                callback(parties)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Failed to fetch parties", exception)
                callback(emptyList())
            }
    }
}
