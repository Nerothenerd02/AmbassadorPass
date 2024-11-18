package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ambassadorpass.Model.Ambassador
import com.example.ambassadorpass.repository.AmbassadorRepository

class AmbassadorViewModel(private val repository: AmbassadorRepository) : ViewModel() {

    fun getAmbassadorData(ambassadorId: String): LiveData<Ambassador> {
        return repository.getAmbassadorData(ambassadorId)
    }

    fun updateInviteCount(ambassadorId: String, newCount: Int) {
        repository.updateInviteCount(ambassadorId, newCount)
    }
}