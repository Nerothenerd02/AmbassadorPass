package com.example.ambassadorpass.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ViewPartiesRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val TAG = "ViewPartiesRepository"

    // Fetch all parties
    fun getParties(): LiveData<List<Map<String, Any>>> {
        val partiesList = MutableLiveData<List<Map<String, Any>>>()
        firestore.collection("parties")
            .get()
            .addOnSuccessListener { snapshot ->
                val parties = snapshot.documents.map { document ->
                    val data = document.data ?: emptyMap<String, Any>()
                    val partyDate = (document.get("partyDate") as? Timestamp)?.toDate() // Get Date from Timestamp
                    data + mapOf("partyDate" to (partyDate ?: "No Date"))
                }
                partiesList.value = parties
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching parties: ${exception.message}")
                partiesList.value = emptyList()
            }
        return partiesList
    }

    // Fetch attendees and their respective ambassador names for a selected party
    fun getAttendeesWithAmbassadors(partyId: String): LiveData<List<Map<String, String>>> {
        val result = MutableLiveData<List<Map<String, String>>>()

        // Step 1: Query attendees by partyId
        firestore.collection("attendees")
            .whereEqualTo("partyId", partyId)
            .get()
            .addOnSuccessListener { attendeesSnapshot ->
                val attendees = attendeesSnapshot.documents.mapNotNull { attendeeDoc ->
                    val name = attendeeDoc.getString("name") ?: "Unknown"
                    val partyLink = attendeeDoc.getString("partyLink") ?: ""
                    mapOf("attendeeName" to name, "partyLink" to partyLink)
                }

                if (attendees.isNotEmpty()) {
                    val attendeesWithAmbassadors = mutableListOf<Map<String, String>>()

                    // Step 2: Cross-check partyLinks with ambassadors
                    attendees.forEach { attendee ->
                        val partyLink = attendee["partyLink"] ?: ""
                        firestore.collection("ambassadors")
                            .whereArrayContains("partyLinks", partyLink) // Match partyLink in array
                            .get()
                            .addOnSuccessListener { ambassadorsSnapshot ->
                                val ambassadorName = ambassadorsSnapshot.documents.firstOrNull()
                                    ?.getString("name") ?: "Unknown"
                                attendeesWithAmbassadors.add(
                                    mapOf(
                                        "attendeeName" to attendee["attendeeName"]!!,
                                        "partyLink" to partyLink,
                                        "ambassadorName" to ambassadorName
                                    )
                                )

                                // Update LiveData when all attendees are processed
                                if (attendeesWithAmbassadors.size == attendees.size) {
                                    result.value = attendeesWithAmbassadors
                                }
                            }
                            .addOnFailureListener {
                                // Handle individual failures gracefully
                                attendeesWithAmbassadors.add(
                                    mapOf(
                                        "attendeeName" to attendee["attendeeName"]!!,
                                        "partyLink" to partyLink,
                                        "ambassadorName" to "Error Fetching Ambassador"
                                    )
                                )

                                if (attendeesWithAmbassadors.size == attendees.size) {
                                    result.value = attendeesWithAmbassadors
                                }
                            }
                    }
                } else {
                    result.value = emptyList()
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "Error fetching attendees: ${exception.message}")
                result.value = emptyList()
            }

        return result
    }

    fun getPartyStats(partyId: String): LiveData<Map<String, Any>> {
        val result = MutableLiveData<Map<String, Any>>()
        val stats = mutableMapOf<String, Any>()

        firestore.collection("parties").document(partyId).get()
            .addOnSuccessListener { partySnapshot ->
                val ticketPrice = partySnapshot.getDouble("ticketPrice") ?: 0.0
                val ticketsSold = partySnapshot.getLong("ticketsSold")?.toInt() ?: 0
                val ambassadorMarkup = partySnapshot.getDouble("ambassadorMarkup") ?: 0.0

                val totalEarnings = ticketPrice * ticketsSold
                stats["totalEarnings"] = totalEarnings
                stats["ticketPrice"] = ticketPrice
                stats["ambassadorMarkup"] = ambassadorMarkup

                firestore.collection("attendees").whereEqualTo("partyId", partyId).get()
                    .addOnSuccessListener { attendeesSnapshot ->
                        val attendees = attendeesSnapshot.documents.map { it.data }
                        stats["totalAttendees"] = attendees.size

                        val attendeesPerAmbassador = mutableMapOf<String, Int>()
                        val commissionsPerAmbassador = mutableMapOf<String, Double>()

                        attendees.forEach { attendee ->
                            val partyLink = attendee?.get("partyLink") as? String ?: ""
                            firestore.collection("ambassadors").whereArrayContains("partyLinks", partyLink).get()
                                .addOnSuccessListener { ambassadorSnapshot ->
                                    val ambassadorName = ambassadorSnapshot.documents.firstOrNull()
                                        ?.getString("name") ?: "Unknown"

                                    attendeesPerAmbassador[ambassadorName] =
                                        attendeesPerAmbassador.getOrDefault(ambassadorName, 0) + 1

                                    val commissionPerTicket = (ambassadorMarkup / 100) * ticketPrice
                                    val totalCommission = commissionPerTicket * attendeesPerAmbassador[ambassadorName]!!
                                    commissionsPerAmbassador[ambassadorName] = totalCommission
                                }.addOnCompleteListener {
                                    stats["attendeesPerAmbassador"] = attendeesPerAmbassador
                                    stats["commissionsPerAmbassador"] = commissionsPerAmbassador
                                    result.value = stats
                                }
                        }
                    }
            }.addOnFailureListener {
                Log.e(TAG, "Error fetching party stats: ${it.message}")
                result.value = emptyMap()
            }
        return result
    }
}
