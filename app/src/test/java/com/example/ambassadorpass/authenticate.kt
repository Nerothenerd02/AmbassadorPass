// UserRepositoryTest.kt
package com.example.ambassadorpass.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.Before
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After

@OptIn(ExperimentalCoroutinesApi::class)
class UserRepositoryTest {

    private lateinit var userRepository: UserRepository
    private val mockFirebaseAuth = mockk<FirebaseAuth>(relaxed = true)
    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher) // Set main dispatcher to TestCoroutineDispatcher
        userRepository = UserRepository(firebaseAuth = mockFirebaseAuth) // Pass the mocked FirebaseAuth instance
    }

    @After
    fun teardown() {
        Dispatchers.resetMain() // Reset main dispatcher
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `test valid authentication`() = runBlocking {
        val email = "test@example.com"
        val password = "password123"

        // Mock the sign-in success scenario
        coEvery { mockFirebaseAuth.signInWithEmailAndPassword(email, password).await() } returns mockk()

        val result = userRepository.authenticate(email, password)
        assertTrue(result)
    }

    @Test
    fun `test invalid authentication`() = runBlocking {
        val email = "invalid@example.com"
        val password = "wrongpassword"

        // Mock a failed authentication scenario (throw an exception)
        coEvery { mockFirebaseAuth.signInWithEmailAndPassword(email, password).await() } throws FirebaseAuthException("ERROR", "Invalid credentials")

        val result = userRepository.authenticate(email, password)
        assertFalse(result)
    }

    @Test
    fun `test empty credentials authentication`() = runBlocking {
        val email = ""
        val password = ""

        // Mock a failed authentication scenario due to empty credentials
        coEvery { mockFirebaseAuth.signInWithEmailAndPassword(email, password).await() } throws FirebaseAuthException("ERROR", "Invalid credentials")

        val result = userRepository.authenticate(email, password)
        assertFalse(result)
    }
}
