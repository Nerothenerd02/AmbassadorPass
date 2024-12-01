package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ambassadorpass.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class ForgotPasswordViewModel(private val userRepository: UserRepository) : ViewModel() {

    fun sendPasswordResetEmail(email: String): LiveData<Boolean> = liveData(Dispatchers.IO) {
        val result = userRepository.sendPasswordResetEmail(email)
        emit(result)
    }
}
