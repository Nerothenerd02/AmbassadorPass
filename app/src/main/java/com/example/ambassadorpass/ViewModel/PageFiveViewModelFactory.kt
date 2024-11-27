package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ambassadorpass.repository.PartyRegistrationRepository

class PageFiveViewModelFactory(
    private val repository: PartyRegistrationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PageFiveViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PageFiveViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
