package com.example.ambassadorpass.repository

import android.net.Uri
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID

class CreatePartyRepository {

    // Initialize FirebaseFirestore instance
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Initialize FirebaseStorage instance
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    companion object {
        private const val TAG = "CreatePartyRepository"
    }

    fun createParty(
        partyName: String,
        partyDescription: String,
        partyDate: Timestamp,
        partyLocation: String,
        ticketsAvailable: Int,
        ticketPrice: Double,
        ambassadorMarkup: Double,
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
            "ticketPrice" to ticketPrice,
            "ambassadorMarkup" to ambassadorMarkup,
            "ticketsSold" to 0,
            "ambassadors" to listOf<String>(),
            "partyLinks" to listOf<String>(),
        )

        Log.d(TAG, "Starting to create party in Firestore with data: $partyData")

        firestore.collection("parties").document(partyId)
            .set(partyData)
            .addOnSuccessListener {
                Log.d(TAG, "Party data saved successfully in Firestore.")
                // Party data saved successfully, now upload photos
                if (selectedImages.isNotEmpty()) {
                    uploadPhotos(partyId, selectedImages, callback)
                } else {
                    Log.d(TAG, "No photos selected for upload.")
                    callback(true)
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to create party data: ${e.message}", e)
                callback(false)
            }
    }

    private fun uploadPhotos(partyId: String, selectedImages: List<Uri>, callback: (Boolean) -> Unit) {
        Log.d(TAG, "Starting to upload photos for party ID: $partyId")
        var uploadedCount = 0
        val imageUrls = mutableListOf<String>()

        for ((index, uri) in selectedImages.withIndex()) {
            val storageRef = storage.reference.child("parties/$partyId/photo_$index.jpg")
            Log.d(TAG, "Uploading photo: $uri to $storageRef")

            storageRef.putFile(uri)
                .addOnSuccessListener {
                    Log.d(TAG, "Photo $index uploaded successfully.")
                    // Get the download URL for the uploaded image
                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        imageUrls.add(downloadUri.toString())
                        uploadedCount++
                        Log.d(TAG, "Download URL for photo $index: $downloadUri")

                        if (uploadedCount == selectedImages.size) {
                            // All images uploaded successfully, save their URLs to Firestore
                            saveImageUrlsToFirestore(partyId, imageUrls, callback)
                        }
                    }.addOnFailureListener { e ->
                        Log.e(TAG, "Failed to get download URL for photo $index: ${e.message}", e)
                        callback(false)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e(TAG, "Failed to upload photo $index: ${e.message}", e)
                    callback(false)
                }
        }
    }

    private fun saveImageUrlsToFirestore(partyId: String, imageUrls: List<String>, callback: (Boolean) -> Unit) {
        Log.d(TAG, "Saving image URLs to Firestore for party ID: $partyId")
        firestore.collection("parties").document(partyId)
            .update("imageUrls", imageUrls)
            .addOnSuccessListener {
                Log.d(TAG, "Image URLs saved successfully in Firestore.")
                callback(true) // Data and images uploaded successfully
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to save image URLs in Firestore: ${e.message}", e)
                callback(false)
            }
    }
}
