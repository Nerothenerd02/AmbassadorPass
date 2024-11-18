package com.example.ambassadorpass.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ambassadorpass.Model.Admin
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference

class AdminRepository {

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getAdminData(adminId: String): LiveData<Admin> {
        val adminData = MutableLiveData<Admin>()
        val documentReference: DocumentReference = firestore.collection("admins").document(adminId)

        documentReference.get()
            .addOnSuccessListener { document ->
                val admin = document.toObject(Admin::class.java)
                if (admin != null) {
                    adminData.value = admin
                }
            }
            .addOnFailureListener { e ->
                // Log the error or handle the failure here if needed
                e.printStackTrace()
            }

        return adminData
    }
}
