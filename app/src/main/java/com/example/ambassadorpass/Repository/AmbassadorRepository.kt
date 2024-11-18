package com.example.ambassadorpass.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ambassadorpass.Model.Ambassador
import com.google.firebase.firestore.FirebaseFirestore

class AmbassadorRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getAmbassadorData(ambassadorId: String): LiveData<Ambassador> {
        val ambassadorData = MutableLiveData<Ambassador>()
        firestore.collection("ambassadors").document(ambassadorId)
            .get()
            .addOnSuccessListener { document ->
                val ambassador = document.toObject(Ambassador::class.java)
                if (ambassador != null) {
                    ambassadorData.value = ambassador
                }
            }
        return ambassadorData
    }

    fun updateInviteCount(ambassadorId: String, newCount: Int) {
        firestore.collection("ambassadors").document(ambassadorId)
            .update("inviteCount", newCount)
    }
}