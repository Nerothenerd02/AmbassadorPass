package com.example.ambassadorpass.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ambassadorpass.Model.Party
import com.google.firebase.firestore.FirebaseFirestore

class AmbassadorRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val TAG = "AmbassadorRepository"

    // Fetch all parties
    fun getParties(): LiveData<List<Party>> {
        val partyList = MutableLiveData<List<Party>>()
        Log.d(TAG, "Fetching all parties from Firestore...")
        firestore.collection("parties")
            .get()
            .addOnSuccessListener { querySnapshot ->
                Log.d(
                    TAG,
                    "Successfully fetched parties: ${querySnapshot.documents.size} documents."
                )
                val parties = querySnapshot.documents.mapNotNull { document ->
                    try {
                        document.toObject(Party::class.java)?.also {
                            Log.d(TAG, "Mapped document to Party: $it")
                        }
                    } catch (e: Exception) {
                        Log.e(
                            TAG,
                            "Failed to map document to Party. Document: ${document.id}, Error: ${e.message}"
                        )
                        null
                    }
                }
                partyList.value = parties
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to fetch parties. Error: ${exception.message}")
                partyList.value = emptyList()
            }
        return partyList
    }

    fun getPartiesForAmbassador(ambassadorId: String): LiveData<List<Party>> {
        val filteredParties = MutableLiveData<List<Party>>()

        // Fetch the partyIds for the ambassador
        firestore.collection("ambassadors")
            .document(ambassadorId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val partyIds = document["partyIds"] as? List<String> ?: emptyList()

                    if (partyIds.isEmpty()) {
                        Log.w(TAG, "No partyIds found for ambassadorId: $ambassadorId")
                        filteredParties.value = emptyList()
                        return@addOnSuccessListener
                    }

                    // Fetch parties matching these partyIds
                    firestore.collection("parties")
                        .whereIn("partyId", partyIds)
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            val parties = querySnapshot.documents.mapNotNull { it.toObject(Party::class.java) }
                            Log.d(TAG, "Filtered parties: $parties")
                            filteredParties.value = parties
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Failed to fetch filtered parties. Error: ${exception.message}")
                            filteredParties.value = emptyList()
                        }
                } else {
                    Log.w(TAG, "No ambassador document found for ID: $ambassadorId")
                    filteredParties.value = emptyList()
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to fetch ambassador document. Error: ${exception.message}")
                filteredParties.value = emptyList()
            }

        return filteredParties
    }


    // Fetch ambassador names associated with a party
    fun getAmbassadorName(ambassadorId: String): LiveData<List<String>> {
        val ambassadorNames = MutableLiveData<List<String>>()

        Log.d(TAG, "Fetching ambassador name for ambassadorID: $ambassadorId...")
        firestore.collection("ambassadors")
            .document(ambassadorId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val name = document.getString("name")
                    if (name != null) {
                        Log.d(TAG, "Fetched ambassador name: $name")
                        ambassadorNames.value = listOf(name) // Wrap name in a List<String>
                    } else {
                        Log.w(TAG, "No name found for ambassadorID: $ambassadorId")
                        ambassadorNames.value = emptyList()
                    }
                } else {
                    Log.w(TAG, "No document found for ambassadorID: $ambassadorId")
                    ambassadorNames.value = emptyList()
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to fetch ambassador name. Error: ${exception.message}")
                ambassadorNames.value = emptyList()
            }

        return ambassadorNames
    }
    fun getAttendeesByAmbassadorId(ambassadorId: String): LiveData<List<String>> {
        val attendees = MutableLiveData<List<String>>()
        val TAG = "AmbassadorRepository"

        // Step 1: Fetch the ambassador's partyLinks
        firestore.collection("ambassadors")
            .document(ambassadorId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val partyLinks = document["partyLinks"] as? List<String> ?: emptyList()
                    Log.d(TAG, "Fetched partyLinks: $partyLinks")

                    if (partyLinks.isEmpty()) {
                        Log.w(TAG, "No partyLinks found for ambassadorId: $ambassadorId")
                        attendees.value = emptyList()
                        return@addOnSuccessListener
                    }

                    // Step 2: Query attendees based on these partyLinks
                    firestore.collection("attendees")
                        .whereIn("partyLink", partyLinks) // This works because partyLink is a single field in the attendees collection
                        .whereEqualTo("paymentStatus", "paid")
                        .get()
                        .addOnSuccessListener { querySnapshot ->
                            val attendeeNames = querySnapshot.documents.mapNotNull { it.getString("name") }
                            Log.d(TAG, "Fetched attendees: $attendeeNames")
                            attendees.value = attendeeNames
                        }
                        .addOnFailureListener { exception ->
                            Log.e(TAG, "Failed to fetch attendees. Error: ${exception.message}")
                            attendees.value = emptyList()
                        }
                } else {
                    Log.w(TAG, "Ambassador document not found for ID: $ambassadorId")
                    attendees.value = emptyList()
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to fetch ambassador document. Error: ${exception.message}")
                attendees.value = emptyList()
            }

        return attendees
    }
    fun getAttendeesByPartyAndAmbassador(ambassadorId: String, partyId: String): LiveData<Pair<List<String>, Double>> {
        val result = MutableLiveData<Pair<List<String>, Double>>()

        firestore.collection("ambassadors")
            .document(ambassadorId)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val partyLinks = document["partyLinks"] as? List<String> ?: emptyList()
                    val targetPartyLink = partyLinks.find { it.contains(partyId) } // Find matching partyLink for this partyId

                    if (targetPartyLink != null) {
                        // Fetch attendees for the partyLink
                        firestore.collection("attendees")
                            .whereEqualTo("partyLink", targetPartyLink)
                            .whereEqualTo("paymentStatus", "paid")
                            .get()
                            .addOnSuccessListener { attendeesSnapshot ->
                                val attendeeNames = attendeesSnapshot.documents.mapNotNull { it.getString("name") }

                                // Fetch party details to calculate commission
                                firestore.collection("parties")
                                    .document(partyId)
                                    .get()
                                    .addOnSuccessListener { partyDoc ->
                                        val ticketPrice = partyDoc.getDouble("ticketPrice") ?: 0.0
                                        val ambassadorMarkup = partyDoc.getDouble("ambassadorMarkup") ?: 0.0
                                        val ticketsSold = attendeesSnapshot.documents.size

                                        val commissionPerTicket = (ambassadorMarkup / 100) * ticketPrice
                                        val totalCommission = commissionPerTicket * ticketsSold

                                        result.value = Pair(attendeeNames, totalCommission)
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e(TAG, "Failed to fetch party details. Error: ${exception.message}")
                                        result.value = Pair(emptyList(), 0.0)
                                    }
                            }
                            .addOnFailureListener { exception ->
                                Log.e(TAG, "Failed to fetch attendees. Error: ${exception.message}")
                                result.value = Pair(emptyList(), 0.0)
                            }
                    } else {
                        Log.w(TAG, "No matching partyLink found for partyId: $partyId")
                        result.value = Pair(emptyList(), 0.0)
                    }
                } else {
                    Log.w(TAG, "Ambassador document not found for ID: $ambassadorId")
                    result.value = Pair(emptyList(), 0.0)
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Failed to fetch ambassador document. Error: ${exception.message}")
                result.value = Pair(emptyList(), 0.0)
            }

        return result
    }

    fun calculateTotalCommission(ambassadorId: String, partyId: String): LiveData<Pair<List<String>, Double>> {
        val result = MutableLiveData<Pair<List<String>, Double>>()

        firestore.collection("ambassadors")
            .document(ambassadorId)
            .get()
            .addOnSuccessListener { document ->
                val partyLinks = document["partyLinks"] as? List<String> ?: emptyList()

                firestore.collection("attendees")
                    .whereIn("partyLink", partyLinks)
                    .whereEqualTo("paymentStatus", "paid")
                    .get()
                    .addOnSuccessListener { attendeeSnapshot ->
                        val attendees = attendeeSnapshot.documents.mapNotNull { it.getString("name") }

                        firestore.collection("parties")
                            .document(partyId)
                            .get()
                            .addOnSuccessListener { partyDoc ->
                                val ticketPrice = partyDoc.getDouble("ticketPrice") ?: 0.0
                                val ambassadorMarkup = partyDoc.getDouble("ambassadorMarkup") ?: 0.0

                                val commissionPerTicket = (ambassadorMarkup / 100) * ticketPrice
                                val totalCommission = commissionPerTicket * attendees.size

                                result.value = Pair(attendees, totalCommission)
                            }
                            .addOnFailureListener { e ->
                                Log.e(TAG, "Failed to fetch party details: ${e.message}")
                                result.value = Pair(emptyList(), 0.0)
                            }
                    }
                    .addOnFailureListener { e ->
                        Log.e(TAG, "Failed to fetch attendees: ${e.message}")
                        result.value = Pair(emptyList(), 0.0)
                    }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Failed to fetch ambassador document: ${e.message}")
                result.value = Pair(emptyList(), 0.0)
            }

        return result
    }

}
