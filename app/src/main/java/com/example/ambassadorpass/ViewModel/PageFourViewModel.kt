package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ambassadorpass.repository.PartyRegistrationRepository

class PageFourViewModel(private val repository: PartyRegistrationRepository) : ViewModel() {

    private val _attendeeDetails = MutableLiveData<Map<String, Any?>>()
    val attendeeDetails: LiveData<Map<String, Any?>> get() = _attendeeDetails

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun fetchAttendeeDetails(partyLink: String, identification: String) {
        repository.fetchAttendeeDetails(partyLink, identification) { details, error ->
            if (details != null) {
                _attendeeDetails.postValue(details)
            } else {
                _errorMessage.postValue(error)
            }
        }
    }
}
