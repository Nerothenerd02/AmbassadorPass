package com.example.ambassadorpass.repository

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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

class UserRepositoryTest {

    @MockK
    private lateinit var firebaseAuth: FirebaseAuth

    @MockK
    private lateinit var firestore: FirebaseFirestore

    private lateinit var userRepository: UserRepository

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        userRepository = UserRepository(firebaseAuth, firestore)
    }

    @Test
    fun `authenticate should return true for successful login`() = runBlocking {
        val email = "test@example.com"
        val password = "secret123"

        val mockAuthResult = mockk<AuthResult>()
        val successfulTask = mockk<Task<AuthResult>>()

        every { successfulTask.isComplete } returns true
        every { successfulTask.isSuccessful } returns true
        every { successfulTask.isCanceled } returns false
        every { successfulTask.exception } returns null
        every { successfulTask.result } returns mockAuthResult

        every { firebaseAuth.signInWithEmailAndPassword(email, password) } returns successfulTask

        val result = userRepository.authenticate(email, password)
        assertTrue(result)
    }

    @Test
    fun `authenticate should return false for a login exception`() = runBlocking {
        val email = "test@example.com"
        val password = "secret123"

        val failedTask = mockk<Task<AuthResult>>()

        every { failedTask.isComplete } returns true
        every { failedTask.isSuccessful } returns false
        every { failedTask.isCanceled } returns false
        every { failedTask.exception } returns Exception("Invalid email or password")

        every { firebaseAuth.signInWithEmailAndPassword(email, password) } returns failedTask

        val result = userRepository.authenticate(email, password)
        assertFalse(result)
    }

    @Test
    fun `authenticate should return false for other exceptions`() = runBlocking {
        val email = "test@example.com"
        val password = "secret123"

        val failedTask = mockk<Task<AuthResult>>()

        every { failedTask.isComplete } returns true
        every { failedTask.isSuccessful } returns false
        every { failedTask.isCanceled } returns false
        every { failedTask.exception } returns Exception("Unexpected error")

        every { firebaseAuth.signInWithEmailAndPassword(email, password) } returns failedTask

        val result = userRepository.authenticate(email, password)
        assertFalse(result)
    }
}
