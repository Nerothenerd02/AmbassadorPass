package com.example.ambassadorpass.repository

import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.android.gms.tasks.Task
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class KeycodeRepositoryTest {

    @MockK
    private lateinit var firestore: FirebaseFirestore

    @MockK
    private lateinit var collectionReference: CollectionReference

    private lateinit var keycodeRepository: KeycodeRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        keycodeRepository = KeycodeRepository(firestore)
    }

    @Test
    fun `validateKeycode should return true for valid keycode`() = runBlocking {
        val validKeycode = "12345"

        val mockQuerySnapshot = mockk<QuerySnapshot>()
        val successfulTask = mockk<Task<QuerySnapshot>>()

        every { successfulTask.isComplete } returns true
        every { successfulTask.isSuccessful } returns true
        every { successfulTask.isCanceled } returns false
        every { successfulTask.result } returns mockQuerySnapshot
        every { mockQuerySnapshot.isEmpty } returns false

        // Mock the Firestore chain
        every { firestore.collection("parties") } returns collectionReference
        every { collectionReference.whereArrayContains("partyLinks", validKeycode).get() } returns successfulTask

        // Mock addOnSuccessListener to immediately invoke the success callback
        every { successfulTask.addOnSuccessListener(any()) } answers {
            val listener = firstArg<OnSuccessListener<QuerySnapshot>>()
            listener.onSuccess(mockQuerySnapshot)
            successfulTask
        }

        // Add a no-op for addOnFailureListener since it won't be called in this scenario
        every { successfulTask.addOnFailureListener(any()) } returns successfulTask

        var result = false
        keycodeRepository.validateKeycode(validKeycode) { isValid ->
            result = isValid
        }

        assertTrue("Expected valid keycode to return true", result)
    }

    @Test
    fun `validateKeycode should return false for invalid keycode`() = runBlocking {
        val invalidKeycode = "98765"

        val mockQuerySnapshot = mockk<QuerySnapshot>()
        val successfulTask = mockk<Task<QuerySnapshot>>()

        every { successfulTask.isComplete } returns true
        every { successfulTask.isSuccessful } returns true
        every { successfulTask.isCanceled } returns false
        every { successfulTask.result } returns mockQuerySnapshot
        every { mockQuerySnapshot.isEmpty } returns true

        every { firestore.collection("parties") } returns collectionReference
        every { collectionReference.whereArrayContains("partyLinks", invalidKeycode).get() } returns successfulTask

        // For invalid keycode but still "successful" Firestore result, we call addOnSuccessListener
        every { successfulTask.addOnSuccessListener(any()) } answers {
            val listener = firstArg<OnSuccessListener<QuerySnapshot>>()
            listener.onSuccess(mockQuerySnapshot)
            successfulTask
        }

        every { successfulTask.addOnFailureListener(any()) } returns successfulTask

        var result = false
        keycodeRepository.validateKeycode(invalidKeycode) { isValid ->
            result = isValid
        }

        assertFalse("Expected invalid keycode to return false", result)
    }

    @Test
    fun `validateKeycode should return false for Firestore exception`() = runBlocking {
        val validKeycode = "12345"

        val failedTask = mockk<Task<QuerySnapshot>>()

        every { failedTask.isComplete } returns true
        every { failedTask.isSuccessful } returns false
        every { failedTask.isCanceled } returns false
        every { failedTask.exception } returns Exception("Firestore error")

        every { firestore.collection("parties") } returns collectionReference
        every { collectionReference.whereArrayContains("partyLinks", validKeycode).get() } returns failedTask

        // Since isSuccessful is false, we should trigger addOnFailureListener
        every { failedTask.addOnFailureListener(any()) } answers {
            val listener = firstArg<OnFailureListener>()
            listener.onFailure(failedTask.exception!!)  // Force unwrap the exception
            failedTask
        }


        // Add a no-op for addOnSuccessListener since it won't be called here
        every { failedTask.addOnSuccessListener(any()) } returns failedTask

        var result = false
        keycodeRepository.validateKeycode(validKeycode) { isValid ->
            result = isValid
        }

        assertFalse("Expected Firestore exception to return false", result)
    }

    @Test
    fun `validateKeycode should return false for empty keycode`() = runBlocking {
        val emptyKeycode = ""

        var result = false
        // This scenario doesn't call Firestore at all, so we don't need to mock listeners.
        keycodeRepository.validateKeycode(emptyKeycode) { isValid ->
            result = isValid
        }

        assertFalse("Expected empty keycode to return false", result)
    }
}
