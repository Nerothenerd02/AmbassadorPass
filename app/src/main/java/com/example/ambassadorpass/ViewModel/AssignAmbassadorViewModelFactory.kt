package com.example.ambassadorpass.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ambassadorpass.viewmodel.AssignAmbassadorViewModel

class AssignAmbassadorViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AssignAmbassadorViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AssignAmbassadorViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
