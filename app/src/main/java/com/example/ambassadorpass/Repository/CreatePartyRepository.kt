package com.example.ambassadorpass.repository

import android.net.Uri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

import java.util.*

class CreatePartyRepository {

    // Initialize FirebaseFirestore instance
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Initialize FirebaseStorage instance (assuming Firebase is already configured)
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    fun createParty(
        partyName: String,
        partyDescription: String,
        partyDate: String,
        partyLocation: String,
        ticketsAvailable: Int,
        selectedImages: List<Uri>,
        callback: (Boolean) -> Unit
    ) {
        val partyId = UUID.randomUUID().toString()
        val partyData = hashMapOf(
            "partyId" to partyId,
            "partyName" to partyName,
            "partyDescription" to partyDescription,
            "partyDate" to partyDate,
            "partyLocation" to partyLocation,
            "ticketsAvailable" to ticketsAvailable,
            "ticketsSold" to 0
        )

        firestore.collection("parties").document(partyId)
            .set(partyData)
            .addOnSuccessListener {
                uploadPhotos(partyId, selectedImages, callback)
            }
            .addOnFailureListener {
                callback(false)
            }
    }

    private fun uploadPhotos(partyId: String, selectedImages: List<Uri>, callback: (Boolean) -> Unit) {
        var uploadedCount = 0
        for (i in selectedImages.indices) {
            val storageRef = storage.reference.child("parties/$partyId/photo_$i.jpg")
            storageRef.putFile(selectedImages[i])
                .addOnSuccessListener {
                    uploadedCount++
                    if (uploadedCount == selectedImages.size) {
                        callback(true) // All images uploaded successfully
                    }
                }
                .addOnFailureListener {
                    callback(false)
                }
        }
    }
}