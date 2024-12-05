package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ambassadorpass.repository.ViewPartiesRepository

class ViewPartiesViewModelFactory(private val repository: ViewPartiesRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ViewPartiesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ViewPartiesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
