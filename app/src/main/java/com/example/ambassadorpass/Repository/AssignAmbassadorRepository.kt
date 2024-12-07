package com.example.ambassadorpass.Repository

import android.content.Context
import android.util.Log
import com.example.ambassadorpass.Model.Party
import com.example.ambassadorpass.service.EmailService
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID

class AssignAmbassadorRepository(private val context: Context) {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val emailService = EmailService(context)

    fun fetchParties(callback: (List<Party>) -> Unit) {
        db.collection("parties")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val parties = querySnapshot.documents.mapNotNull { it.toObject(Party::class.java) }
                callback(parties)
            }
            .addOnFailureListener { exception ->
                Log.e("FetchPartiesError", "Failed to fetch parties: ${exception.message}")
                callback(emptyList()) // Return an empty list on failure
            }
    }


    // Function to handle ambassador submission
    fun handleAmbassadorSubmission(
        email: String,
        name: String,
        partyId: String,
        callback: (Boolean, String?) -> Unit
    ) {
        // Check if the email exists in the ambassadors collection
        db.collection("ambassadors")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Case: Existing ambassador
                    Log.d("AmbassadorCheck", "Ambassador found in collection: $email")
                    val document = querySnapshot.documents.first()
                    val uid = document.getString("uid")
                    val ambassadorID = document.getString("ambassadorID") ?: generateAmbassadorID()

                    if (uid != null) {
                        processExistingAmbassador(uid, name, email, partyId, ambassadorID, callback)
                    } else {
                        Log.e("AmbassadorError", "UID is null for existing ambassador: $email")
                        callback(false, "Error processing existing ambassador.")
                    }
                } else {
                    // Case: New user, check Firebase Authentication
                    checkFirebaseAuth(email, name, partyId, callback)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("AmbassadorCheckError", "Error checking ambassadors collection: ${exception.message}")
                callback(false, "Error checking ambassador details.")
            }
    }

    // Handle an existing ambassador
    private fun processExistingAmbassador(
        uid: String,
        name: String,
        email: String,
        partyId: String,
        ambassadorID: String,
        callback: (Boolean, String?) -> Unit
    ) {
        val partyLink = generatePartyLink(partyId)
        Log.d("AmbassadorCheck", "Processing existing ambassador: $email with UID: $uid")

        updateFirestoreWithAmbassadorDetails(
            uid,
            name,
            email,
            partyId,
            partyLink,
            null, // No password needed for existing ambassadors
            ambassadorID,
            { success ->
                if (success) {
                    Log.d("AmbassadorCheck", "Successfully updated Firestore for ambassador: $email")
                    // Notify ambassador about the new party assignment
                    try {
                        emailService.sendAmbassadorPartyUpdateEmail(email, name, partyId, partyLink)
                        Log.d("AmbassadorCheck", "Email sent successfully to: $email")
                        callback(true, null)
                    } catch (e: Exception) {
                        Log.e("EmailError", "Failed to send email to ambassador: $email", e)
                        callback(false, "Failed to send email.")
                    }
                } else {
                    Log.e("FirestoreUpdateError", "Failed to update Firestore for ambassador: $email")
                    callback(false, "Failed to update ambassador details.")
                }
            },
            isFirstTimeUser = false
        )
    }

    // Check Firebase Authentication for user existence
    private fun checkFirebaseAuth(
        email: String,
        name: String,
        partyId: String,
        callback: (Boolean, String?) -> Unit
    ) {
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val signInMethods = task.result?.signInMethods
                    if (signInMethods.isNullOrEmpty()) {
                        // Case: Completely new user
                        createNewUser(email, name, partyId, callback)
                    } else {
                        // Case: Existing user in FirebaseAuth but not in ambassadors collection
                        processNewAmbassador(email, name, partyId, callback)
                    }
                } else {
                    Log.e("AuthError", "Error checking Firebase Authentication: ${task.exception?.message}")
                    callback(false, "Error verifying user existence.")
                }
            }
    }

    // Handle new user creation
    private fun createNewUser(
        email: String,
        name: String,
        partyId: String,
        callback: (Boolean, String?) -> Unit
    ) {
        val password = generatePassword()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { createTask ->
                if (createTask.isSuccessful) {
                    val uid = createTask.result?.user?.uid
                    if (uid != null) {
                        updateFirestoreWithAmbassadorDetails(
                            uid,
                            name,
                            email,
                            partyId,
                            generatePartyLink(partyId),
                            password,
                            generateAmbassadorID(),
                            { success ->
                                if (success) {
                                    emailService.sendAmbassadorEmail(email, name, partyId, password, generatePartyLink(partyId))
                                    callback(true, null)
                                } else {
                                    callback(false, "Failed to update Firestore for new user.")
                                }
                            },
                            isFirstTimeUser = true
                        )
                    } else {
                        callback(false, "UID is null for new user.")
                    }
                } else {
                    Log.e("CreateUserError", "Error creating new user: ${createTask.exception?.message}")
                    callback(false, "Error creating new user.")
                }
            }
    }

    // Handle existing Firebase Authentication user as a new ambassador
    private fun processNewAmbassador(
        email: String,
        name: String,
        partyId: String,
        callback: (Boolean, String?) -> Unit
    ) {
        val partyLink = generatePartyLink(partyId)
        val ambassadorID = generateAmbassadorID()
        auth.fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                val uid = task.result?.signInMethods?.firstOrNull()
                if (uid != null) {
                    updateFirestoreWithAmbassadorDetails(
                        uid,
                        name,
                        email,
                        partyId,
                        partyLink,
                        null,
                        ambassadorID,
                        { success ->
                            if (success) {
                                emailService.sendAmbassadorPartyUpdateEmail(email, name, partyId, partyLink)
                                callback(true, null)
                            } else {
                                callback(false, "Failed to update ambassador details.")
                            }
                        },
                        isFirstTimeUser = false
                    )
                } else {
                    callback(false, "UID not found for existing user.")
                }
            }
    }

    // Update Firestore with ambassador details (same as original code, no changes here)
    private fun updateFirestoreWithAmbassadorDetails(
        uid: String?,
        name: String,
        email: String,
        partyId: String,
        partyLink: String,
        password: String?,
        ambassadorID: String,
        callback: (Boolean) -> Unit,
        isFirstTimeUser: Boolean
    ) {
        if (uid == null) {
            Log.e("FirestoreUpdateError", "UID is null, cannot update Firestore")
            callback(false)
            return
        }

        val ambassadorRef = db.collection("ambassadors").document(uid)
        ambassadorRef.get()
            .addOnSuccessListener { document ->
                val existingPartyIds = document.get("partyIds") as? MutableList<String> ?: mutableListOf()
                val existingPartyLinks = document.get("partyLinks") as? MutableList<String> ?: mutableListOf()

                if (existingPartyIds.contains(partyId)) {
                    Log.d("FirestoreUpdate", "Party already assigned to this ambassador: $partyId")
                    callback(true) // Already assigned, considered successful
                    return@addOnSuccessListener
                }

                existingPartyIds.add(partyId)
                existingPartyLinks.add(partyLink)

                val updateData = mapOf(
                    "partyIds" to existingPartyIds,
                    "partyLinks" to existingPartyLinks
                )

                ambassadorRef.update(updateData)
                    .addOnSuccessListener {
                        Log.d("FirestoreUpdate", "Successfully updated ambassador with new party link.")
                        callback(true)
                    }
                    .addOnFailureListener { e ->
                        Log.e("FirestoreUpdateError", "Failed to update ambassador in Firestore", e)
                        callback(false)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("FirestoreFetchError", "Failed to fetch ambassador document: $uid", e)
                callback(false)
            }
    }


    // Generate party link
    private fun generatePartyLink(partyId: String): String {
        val randomString = (1..6).map { ('A'..'Z').random() }.joinToString("")
        return "party-$partyId-$randomString"
    }

    // Generate password
    private fun generatePassword(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#\$%^&*"
        return (1..10).map { chars.random() }.joinToString("")
    }

    // Generate unique ambassador ID
    private fun generateAmbassadorID(): String {
        return UUID.randomUUID().toString()
    }
}
