package com.example.ambassadorpass.Repository

import android.content.Context
import android.util.Log
import com.example.ambassadorpass.service.EmailService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AssignAdminRepository(private val context: Context) {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val emailService = EmailService(context)

    // Function to create or update admin account and update Firestore
    fun createAdminAccount(
        email: String,
        name: String,
        callback: (Boolean, String) -> Unit
    ) {
        Log.d("CreateAdmin", "Starting admin creation for email: $email")
        val password = generatePassword()

        // Check if the admin already exists in Firebase Authentication
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (signInMethods.isNullOrEmpty()) {
                        // User does not exist, create new user in Authentication
                        auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener { createTask ->
                                if (createTask.isSuccessful) {
                                    val uid = createTask.result?.user?.uid
                                    Log.d("CreateAdmin", "User created with UID: $uid")
                                    updateFirestoreWithAdminDetails(uid, name, email, password, callback, isFirstTimeUser = true)
                                } else {
                                    Log.e("CreateUserError", "Error creating user: ${createTask.exception?.message}")
                                    callback(false, "Error creating user: ${createTask.exception?.message}")
                                }
                            }
                    } else {
                        // User already exists, fetch UID from Firestore and proceed
                        db.collection("ambassadors")
                            .whereEqualTo("email", email)
                            .get()
                            .addOnSuccessListener { querySnapshot ->
                                if (!querySnapshot.isEmpty) {
                                    val document = querySnapshot.documents.first()
                                    val uid = document.getString("uid")
                                    if (uid != null) {
                                        Log.d("CreateAdmin", "User already exists, proceeding to Firestore update with UID: $uid")
                                        callback(false, "User is already an admin.")
                                    } else {
                                        Log.e("FetchUserError", "UID is null for email: $email")
                                        callback(false, "UID is null for email: $email")
                                    }
                                } else {
                                    Log.e("FetchUserError", "No admin found with email: $email")
                                    callback(false, "No admin found with email: $email")
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e("FetchUserError", "Failed to fetch user from Firestore: ${e.message}")
                                callback(false, "Failed to fetch user from Firestore: ${e.message}")
                            }
                    }
                } else {
                    Log.e("FetchSignInMethodsError", "Error fetching sign-in methods: ${task.exception?.message}")
                    callback(false, "Error fetching sign-in methods: ${task.exception?.message}")
                }
            }
    }

    private fun updateFirestoreWithAdminDetails(
        uid: String?,
        name: String,
        email: String,
        password: String,
        callback: (Boolean, String) -> Unit,
        isFirstTimeUser: Boolean
    ) {
        if (uid == null) {
            Log.e("UpdateFirestoreError", "UID is null, cannot update Firestore")
            callback(false, "UID is null, cannot update Firestore")
            return
        }

        val adminData = hashMapOf(
            "name" to name,
            "email" to email,
            "tier" to "gold", // Distinguish admins by tier
            "uid" to uid
        )

        Log.d("Firestore", "Updating ambassadors collection with admin data: $adminData")
        db.collection("ambassadors").document(uid)
            .set(adminData)
            .addOnSuccessListener {
                Log.d("Firestore", "Successfully updated admin in ambassadors collection.")

                // Send an email to the new admin
                if (isFirstTimeUser) {
                    emailService.sendAdminEmail(email, name, password)
                }

                callback(true, "Successfully updated admin in Firestore.")
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreError", "Failed to update admin in Firestore.", e)
                callback(false, "Failed to update admin in Firestore: ${e.message}")
            }
    }

    // Helper function to generate password
    private fun generatePassword(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#\$%^&*"
        val password = (1..10).map { chars.random() }.joinToString("")
        Log.d("GeneratePassword", "Generated password: $password")
        return password
    }
}
