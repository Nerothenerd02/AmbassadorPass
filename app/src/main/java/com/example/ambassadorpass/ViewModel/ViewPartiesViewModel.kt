package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ambassadorpass.repository.ViewPartiesRepository

class ViewPartiesViewModel(private val repository: ViewPartiesRepository) : ViewModel() {

    fun getParties(): LiveData<List<Map<String, Any>>> {
        return repository.getParties()
    }

    fun getAttendeesWithAmbassadors(partyId: String): LiveData<List<Map<String, String>>> {
        return repository.getAttendeesWithAmbassadors(partyId)
    }
}
