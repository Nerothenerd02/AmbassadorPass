package com.example.ambassadorpass.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.util.Log

class PartyRegistrationRepository {
    private val firestore = FirebaseFirestore.getInstance()


    // Fetch party name by keycode (partyLink)
    fun getPartyNameByLink(partyLink: String, callback: (String?) -> Unit) {
        if (partyLink.isBlank()) {
            Log.e("PartyRegistrationRepository", "PartyLink is empty or blank")
            callback(null)
            return
        }
        Log.d("PartyRegistrationRepository", "Searching for partyLink: $partyLink")
        firestore.collection("parties")
            .whereArrayContains("partyLinks", partyLink)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val partyDocument = querySnapshot.documents.first()
                    val partyName = partyDocument.getString("partyName")
                    callback(partyName)
                } else {
                    Log.d("PartyRegistrationRepository", "No document found with the given partyLink")
                    callback(null)
                }
            }
            .addOnFailureListener {
                Log.e("PartyRegistrationRepository", "Error fetching party: ", it)
                callback(null)
            }
    }
}
