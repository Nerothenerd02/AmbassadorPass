package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ambassadorpass.repository.PartyRegistrationRepository

class PageFourViewModelFactory(
    private val repository: PartyRegistrationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PageFourViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PageFourViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
