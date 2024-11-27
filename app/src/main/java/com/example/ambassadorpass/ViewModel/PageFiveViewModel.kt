package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ambassadorpass.repository.PartyRegistrationRepository

class PageFiveViewModel(private val repository: PartyRegistrationRepository) : ViewModel() {

    private val _partyDetails = MutableLiveData<Map<String, Any?>>()
    val partyDetails: LiveData<Map<String, Any?>> get() = _partyDetails

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun fetchPartyDetails(partyLink: String) {
        repository.fetchPartyDetailsForPageFive(partyLink) { details, error ->
            if (details != null) {
                _partyDetails.postValue(details)
            } else {
                _errorMessage.postValue(error)
            }
        }
    }
}
