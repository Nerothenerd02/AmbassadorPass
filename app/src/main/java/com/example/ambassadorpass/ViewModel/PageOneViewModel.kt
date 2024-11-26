package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ambassadorpass.repository.PartyRegistrationRepository

class PageOneViewModel(private val repository: PartyRegistrationRepository) : ViewModel() {

    private val _partyName = MutableLiveData<String>()
    val partyName: LiveData<String> get() = _partyName

    fun fetchPartyName(partyLink: String) {
        repository.getPartyNameByLink(partyLink) { name ->
            _partyName.postValue(name ?: "Party Name Not Found")
        }
    }
}
