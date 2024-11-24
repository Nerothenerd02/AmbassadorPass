package com.example.ambassadorpass.ViewModel

import androidx.lifecycle.ViewModel
import com.example.ambassadorpass.Repository.AssignAdminRepository

class AssignAdminViewModel(private val repository: AssignAdminRepository) : ViewModel() {

    fun createAdminAccount(email: String, name: String, callback: (Boolean, String) -> Unit) {
        repository.createAdminAccount(email, name, callback)
    }
}
