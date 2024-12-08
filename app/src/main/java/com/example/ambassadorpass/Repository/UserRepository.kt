package com.example.ambassadorpass.repository

import com.example.ambassadorpass.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) {

    suspend fun authenticate(email: String, password: String): Boolean {
        return try {
            firebaseAuth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Get user data from Firestore based on UID
    suspend fun getUserByEmail(email: String): User? {
        return try {
            val querySnapshot = firestore.collection("ambassadors")
                .whereEqualTo("email", email)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                val user = document.toObject(User::class.java)
                user?.apply {
                    tier = document.getString("tier")?.lowercase() ?: "unknown"
                    isFirstTimeUser = document.getBoolean("isFirstTimeUser") ?: true // Default to true if not set
                }
                user
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    // Update first-time user status
    suspend fun updateFirstTimeUserStatus(email: String): Boolean {
        return try {
            val querySnapshot = firestore.collection("ambassadors")
                .whereEqualTo("email", email)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                firestore.collection("ambassadors")
                    .document(document.id)
                    .update("isFirstTimeUser", false)
                    .await()
                true
            } else {
                false
            }
        } catch (e: Exception) {
            false
        }
    }

    // Update user password
    suspend fun updatePassword(newPassword: String): Boolean {
        return try {
            val currentUser = FirebaseAuth.getInstance().currentUser
            currentUser?.updatePassword(newPassword)?.await()
            true
        } catch (e: Exception) {
            false
        }
    }

    // Get current user's tier
    suspend fun getCurrentUserTier(): String {
        return try {
            val currentUser = FirebaseAuth.getInstance().currentUser
            val email = currentUser?.email ?: return "unknown"

            val querySnapshot = firestore.collection("ambassadors")
                .whereEqualTo("email", email)
                .get()
                .await()

            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                document.getString("tier")?.lowercase() ?: "unknown"
            } else {
                "unknown"
            }
        } catch (e: Exception) {
            "unknown"
        }
    }

    // Send password reset email
    suspend fun sendPasswordResetEmail(email: String): Boolean {
        return try {
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
