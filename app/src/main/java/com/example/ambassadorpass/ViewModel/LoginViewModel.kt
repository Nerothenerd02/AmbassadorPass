package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.ambassadorpass.Model.User
import com.example.ambassadorpass.repository.UserRepository
import kotlinx.coroutines.Dispatchers

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    // Authenticate the user
    fun login(email: String, password: String): LiveData<Boolean> = liveData(Dispatchers.IO) {
        val result = userRepository.authenticate(email, password)
        emit(result)
    }

    // Retrieve user data by email
    fun getUserByEmail(email: String): LiveData<User?> = liveData(Dispatchers.IO) {
        val user = userRepository.getUserByEmail(email)
        emit(user)
    }
}
