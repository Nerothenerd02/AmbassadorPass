package com.example.ambassadorpass.repository

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class AmbassadorIDUpdater {

    private val firestore = FirebaseFirestore.getInstance()

    fun assignMissingAmbassadorIDs() {
        firestore.collection("ambassadors")
            .get()
            .addOnSuccessListener { querySnapshot ->
                for (document in querySnapshot.documents) {
                    if (!document.contains("ambassadorID")) {
                        val uid = document.id // Document ID as UID
                        val newAmbassadorID = generateAmbassadorID()

                        // Update document with new ambassadorID
                        firestore.collection("ambassadors").document(uid)
                            .update("ambassadorID", newAmbassadorID)
                            .addOnSuccessListener {
                                Log.d("AmbassadorIDUpdater", "Updated document $uid with ambassadorID: $newAmbassadorID")
                            }
                            .addOnFailureListener { e ->
                                Log.e("AmbassadorIDUpdater", "Failed to update document $uid", e)
                            }
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("AmbassadorIDUpdater", "Failed to query ambassadors collection", e)
            }
    }

    private fun generateAmbassadorID(): String {
        return UUID.randomUUID().toString()
    }

}
