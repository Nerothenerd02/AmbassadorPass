package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ambassadorpass.Model.Admin
import com.example.ambassadorpass.repository.AdminRepository

class AdminViewModel(private val repository: AdminRepository) : ViewModel() {

    fun getAdminData(adminId: String): LiveData<Admin> {
        return repository.getAdminData(adminId)
    }
}