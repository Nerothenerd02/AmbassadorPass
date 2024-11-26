package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ambassadorpass.repository.PartyRegistrationRepository

class PageTwoViewModelFactory(
    private val repository: PartyRegistrationRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PageTwoViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PageTwoViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
