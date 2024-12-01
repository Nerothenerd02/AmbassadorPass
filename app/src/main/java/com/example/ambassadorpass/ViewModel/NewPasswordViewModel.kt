package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ambassadorpass.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class NewPasswordViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Update the password in Firebase Authentication
    fun updatePassword(newPassword: String): LiveData<Boolean> = liveData(Dispatchers.IO) {
        val result = userRepository.updatePassword(newPassword)
        emit(result)
    }

    // Get the user's tier to navigate to the correct page
    fun getUserTier(): LiveData<String> = liveData(Dispatchers.IO) {
        val tier = userRepository.getCurrentUserTier()
        emit(tier)
    }
}
