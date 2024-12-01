package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.ambassadorpass.Model.Attendee
import com.example.ambassadorpass.Model.Party
import com.example.ambassadorpass.repository.AmbassadorRepository

class AmbassadorViewModel(private val repository: AmbassadorRepository) : ViewModel() {

    fun getParties(): LiveData<List<Party>> {
        return repository.getParties()
    }

    fun getAmbassadorName(partyId: String): LiveData<List<String>> {
        return repository.getAmbassadorName(partyId)
    }
    fun getAttendeesByAmbassadorId(ambassadorId: String): LiveData<List<String>> {
        return repository.getAttendeesByAmbassadorId(ambassadorId)
    }
    fun calculateTotalCommission(ambassadorId: String, partyId: String): LiveData<Pair<List<String>, Double>> {
        return repository.calculateTotalCommission(ambassadorId, partyId)
    }

}


