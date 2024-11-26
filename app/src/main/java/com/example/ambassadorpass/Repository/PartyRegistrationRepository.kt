package com.example.ambassadorpass.repository

import com.google.firebase.firestore.FirebaseFirestore
import android.util.Log
import java.util.UUID

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
            .addOnFailureListener { exception ->
                Log.e("PartyRegistrationRepository", "Error fetching party: ", exception)
                callback(null)
            }
    }

    // Fetch party details by partyLink
    fun fetchPartyDetails(
        partyLink: String,
        callback: (partyId: String?, ambassadorId: String?) -> Unit
    ) {
        if (partyLink.isBlank()) {
            Log.e("PartyRegistrationRepository", "PartyLink is empty or blank")
            callback(null, null)
            return
        }
        Log.d("PartyRegistrationRepository", "Fetching party details for partyLink: $partyLink")
        firestore.collection("parties")
            .whereArrayContains("partyLinks", partyLink)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents.first()
                    val partyId = document.getString("partyId")
                    val ambassadorId = document.getString("ambassadorId")
                    callback(partyId, ambassadorId)
                } else {
                    Log.d("PartyRegistrationRepository", "No document found for partyLink: $partyLink")
                    callback(null, null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("PartyRegistrationRepo", "Error fetching party details", exception)
                callback(null, null)
            }
    }

    // Add attendee to Firestore
    fun addAttendee(
        name: String,
        gender: String,
        age: String,
        email: String,
        phone: String,
        identification: String,
        partyLink: String,
        callback: (success: Boolean, error: String?) -> Unit
    ) {
        // Step 1: Fetch the partyId using the partyLink
        firestore.collection("parties")
            .whereArrayContains("partyLinks", partyLink) // Query using partyLink
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val partyDocument = querySnapshot.documents.first()
                    val partyId = partyDocument.getString("partyId") ?: ""
                    val ambassadorId = partyDocument.getString("ambassadorId") ?: ""

                    if (partyId.isNotEmpty()) {
                        // Step 2: Check if the identification is already registered for this partyId
                        firestore.collection("attendees")
                            .whereEqualTo("partyId", partyId)
                            .whereEqualTo("identification", identification)
                            .get()
                            .addOnSuccessListener { attendeeSnapshot ->
                                if (!attendeeSnapshot.isEmpty) {
                                    // Identification already exists for the party
                                    callback(false, "This identification is already registered for this party.")
                                } else {
                                    // Step 3: Proceed to add attendee
                                    val attendeeData = hashMapOf(
                                        "name" to name,
                                        "gender" to gender,
                                        "age" to age,
                                        "email" to email,
                                        "phone" to phone,
                                        "identification" to identification,
                                        "partyId" to partyId,
                                        "partyLink" to partyLink,
                                        "ambassadorId" to ambassadorId,
                                        "paymentStatus" to "pending",
                                        "qrCode" to "" // Placeholder for future updates
                                    )

                                    val uniqueId = UUID.randomUUID().toString()

                                    firestore.collection("attendees").document(uniqueId)
                                        .set(attendeeData)
                                        .addOnSuccessListener {
                                            Log.d("PartyRegistrationRepo", "Attendee added successfully: $attendeeData")
                                            callback(true, null)
                                        }
                                        .addOnFailureListener { exception ->
                                            Log.e("PartyRegistrationRepo", "Error adding attendee", exception)
                                            callback(false, exception.message)
                                        }
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.e("PartyRegistrationRepo", "Error checking identification", exception)
                                callback(false, "Error checking identification: ${exception.message}")
                            }
                    } else {
                        callback(false, "Invalid partyId retrieved from partyLink.")
                    }
                } else {
                    Log.e("PartyRegistrationRepo", "No party found for the given partyLink.")
                    callback(false, "Invalid partyLink: No matching party found.")
                }
            }
            .addOnFailureListener { exception ->
                Log.e("PartyRegistrationRepo", "Error fetching party details", exception)
                callback(false, "Error fetching party details: ${exception.message}")
            }
    }}
