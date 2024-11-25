package com.example.ambassadorpass.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class KeycodeRepository {

    private val firestore = FirebaseFirestore.getInstance()

    // Function to validate the keycode from Firestore
    fun validateKeycode(keycode: String, callback: (Boolean) -> Unit) {
        if (keycode.isBlank()) {
            Log.e("KeycodeRepository", "Validation failed: Keycode is empty or blank")
            callback(false)
            return
        }

        firestore.collection("parties")
            .whereArrayContains("partyLinks", keycode)  // Check if keycode is in partyLinks array
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    Log.d("KeycodeRepository", "Keycode is valid")
                    callback(true)
                } else {
                    Log.d("KeycodeRepository", "Keycode is not valid")
                    callback(false)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("KeycodeRepository", "Error fetching keycode data from Firestore", exception)
                callback(false)
            }
    }
}
