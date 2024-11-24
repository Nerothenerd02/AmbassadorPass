package com.example.ambassadorpass.ViewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ambassadorpass.Repository.AssignAdminRepository

class AssignAdminViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AssignAdminViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AssignAdminViewModel(AssignAdminRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
