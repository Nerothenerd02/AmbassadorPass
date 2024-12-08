package com.example.ambassadorpass.repository

import com.google.firebase.firestore.FirebaseFirestore

class KeycodeRepository(private val firestore: FirebaseFirestore) {

    // Function to validate the keycode from Firestore
    fun validateKeycode(keycode: String, callback: (Boolean) -> Unit) {
        if (keycode.isBlank()) {
            callback(false)
            return
        }

        firestore.collection("parties")
            .whereArrayContains("partyLinks", keycode) // Check if keycode is in partyLinks array
            .get()
            .addOnSuccessListener { documents ->
                callback(!documents.isEmpty)
            }
            .addOnFailureListener {
                callback(false)
            }
    }
}
