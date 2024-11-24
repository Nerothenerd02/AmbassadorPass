package com.example.ambassadorpass.Repository

import android.content.Context
import android.util.Base64
import android.util.Log
import com.example.ambassadorpass.Model.Party
import com.example.ambassadorpass.service.EmailService
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.security.SecureRandom

class AssignAmbassadorRepository(private val context: Context) {

    private val firestore = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = Firebase.firestore
    private val emailService = EmailService(context)

    // Function to fetch parties from Firestore
    fun fetchParties(callback: (List<Party>) -> Unit) {
        firestore.collection("parties").get()
            .addOnSuccessListener { querySnapshot ->
                val parties = querySnapshot.documents.mapNotNull { doc ->
                    try {
                        val data = doc.data ?: return@mapNotNull null
                        Party(
                            partyName = data["partyName"] as? String ?: "",
                            partyId = doc.id,
                            partyDate = data["partyDate"] as? Timestamp,
                            partyDescription = data["partyDescription"] as? String ?: "",
                            partyLocation = data["partyLocation"] as? String ?: "",
                            ticketPrice = (data["ticketPrice"] as? Number)?.toInt() ?: 0,
                            ticketsAvailable = (data["ticketsAvailable"] as? Number)?.toInt() ?: 0,
                            ticketsSold = (data["ticketsSold"] as? Number)?.toInt() ?: 0
                        )
                    } catch (e: Exception) {
                        Log.e("FirestoreError", "Failed to parse party: ${doc.id}", e)
                        null
                    }
                }
                Log.d("FirestoreSuccess", "Fetched ${parties.size} parties")
                callback(parties)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Failed to fetch parties", exception)
                callback(emptyList())
            }
    }

    // Function to create or update ambassador account, generate password and passcode, and update Firestore
    fun createAmbassadorAccount(
        email: String,
        name: String,
        partyId: String,
        callback: (Boolean) -> Unit
    ) {
        Log.d("CreateAmbassador", "Starting ambassador creation for email: $email")
        val password = generatePassword()
        val partyLink = generatePartyLink(partyId)

        // Check if the ambassador already exists in Firebase Authentication
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
                                    Log.d("CreateAmbassador", "User created with UID: $uid")
                                    updateFirestoreWithAmbassadorDetails(uid, name, email, partyId, partyLink, password, callback, isFirstTimeUser = true)
                                } else {
                                    Log.e("CreateUserError", "Error creating user: ${createTask.exception?.message}")
                                    callback(false)
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
                                        Log.d("CreateAmbassador", "User already exists, proceeding to Firestore update with UID: $uid")
                                        updateFirestoreWithAmbassadorDetails(uid, name, email, partyId, partyLink, password, callback, isFirstTimeUser = false)
                                    } else {
                                        Log.e("FetchUserError", "UID is null for email: $email")
                                        callback(false)
                                    }
                                } else {
                                    Log.e("FetchUserError", "No ambassador found with email: $email")
                                    callback(false)
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e("FetchUserError", "Failed to fetch user from Firestore: ${e.message}")
                                callback(false)
                            }
                    }
                } else {
                    Log.e("FetchSignInMethodsError", "Error fetching sign-in methods: ${task.exception?.message}")
                    callback(false)
                }
            }
    }

    private fun updateFirestoreWithAmbassadorDetails(
        uid: String?,
        name: String,
        email: String,
        partyId: String,
        partyLink: String,
        password: String,
        callback: (Boolean) -> Unit,
        isFirstTimeUser: Boolean
    ) {
        if (uid == null) {
            Log.e("UpdateFirestoreError", "UID is null, cannot update Firestore")
            callback(false)
            return
        }

        db.collection("ambassadors").document(uid).get().addOnSuccessListener { ambassadorSnapshot ->
            val existingPartyIds = ambassadorSnapshot.get("partyIds") as? MutableList<String> ?: mutableListOf()
            val existingPartyLinks = ambassadorSnapshot.get("partyLinks") as? MutableList<String> ?: mutableListOf()

            // Check if this party is already associated with the ambassador
            if (existingPartyIds.contains(partyId)) {
                Log.d("UpdateFirestore", "Ambassador already has a link for this party.")
                callback(false)
                return@addOnSuccessListener
            }

            // Add the new partyId and partyLink
            existingPartyIds.add(partyId)
            existingPartyLinks.add(partyLink)

            val ambassadorData = hashMapOf(
                "name" to name,
                "email" to email,
                "tier" to "silver",
                "uid" to uid,
                "partyIds" to existingPartyIds,
                "partyLinks" to existingPartyLinks
            )

            Log.d("Firestore", "Updating ambassador collection with data: $ambassadorData")
            db.collection("ambassadors").document(uid)
                .set(ambassadorData)
                .addOnSuccessListener {
                    Log.d("Firestore", "Successfully updated ambassador in Firestore.")

                    // Update parties collection with ambassador link and increment ambassador count
                    val partyRef = db.collection("parties").document(partyId)

                    db.runTransaction { transaction ->
                        val snapshot = transaction.get(partyRef)
                        val currentAmbassadors = snapshot.getLong("numberOfAmbassadors") ?: 0
                        Log.d("FirestoreTransaction", "Current number of ambassadors: $currentAmbassadors")
                        transaction.update(partyRef, "numberOfAmbassadors", currentAmbassadors + 1)

                        // Add ambassador link if needed
                        val ambassadorLinks = snapshot.get("partyLinks") as? MutableList<String> ?: mutableListOf()
                        ambassadorLinks.add(partyLink)
                        Log.d("FirestoreTransaction", "Updated ambassador links: $ambassadorLinks")
                        transaction.update(partyRef, "partyLinks", ambassadorLinks)
                    }.addOnSuccessListener {
                        Log.d("FirestoreTransaction", "Successfully updated party with new ambassador link.")

                        // Send appropriate email to the ambassador
                        if (isFirstTimeUser) {
                            // Send a complete email with credentials
                            emailService.sendAmbassadorEmail(email, name, partyName = partyId, password, partyLink)
                        } else {
                            // Send an email with party link and party name only
                            emailService.sendAmbassadorPartyUpdateEmail(email, name, partyName = partyId, partyLink)
                        }

                        callback(true)
                    }.addOnFailureListener { e ->
                        Log.e("FirestoreTransaction", "Failed to update party with new ambassador link.", e)
                        callback(false)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("FirestoreError", "Failed to update ambassador to Firestore.", e)
                    callback(false)
                }
        }
    }

    // Helper function to generate password
    private fun generatePassword(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#\$%^&*"
        val password = (1..10).map { chars.random() }.joinToString("")
        Log.d("GeneratePassword", "Generated password: $password")
        return password
    }

    // Helper function to generate party link
    private fun generatePartyLink(partyId: String): String {
        val randomString = (1..6)
            .map { ('A'..'Z').random() }
            .joinToString("")
        val partyLink = "party-$partyId-$randomString"
        Log.d("GeneratePartyLink", "Generated party link: $partyLink")
        return partyLink
    }
}
