package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ambassadorpass.repository.KeycodeRepository

class KeycodeViewModel(private val repository: KeycodeRepository) : ViewModel() {

    // Function to validate the keycode
    fun validateKeycode(keycode: String, callback: (Boolean) -> Unit) {
        repository.validateKeycode(keycode, callback)
    }
}

// ViewModelFactory to provide an instance of the ViewModel with a repository
class KeycodeViewModelFactory(private val repository: KeycodeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(KeycodeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return KeycodeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
