package com.example.ambassadorpass.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class PartyPreviewRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // Fetch image URLs for a specific party based on the party link
    fun fetchImageUrls(partyLink: String, callback: (List<String>?, Exception?) -> Unit) {
        firestore.collection("parties")
            .whereArrayContains("partyLinks", partyLink)
            .get()
            .addOnSuccessListener { documents ->
                Log.d("PartyPreviewRepository", "Documents fetched: ${documents.documents}")
                if (!documents.isEmpty) {
                    val imageUrls = documents.documents[0].get("imageUrls") as? List<String>
                    Log.d("PartyPreviewRepository", "Image URLs: $imageUrls")
                    callback(imageUrls, null)
                } else {
                    Log.d("PartyPreviewRepository", "No matching party found for partyLink: $partyLink")
                    callback(null, null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("PartyPreviewRepository", "Error fetching images", exception)
                callback(null, exception)
            }

    }
}
