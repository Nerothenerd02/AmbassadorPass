package com.example.ambassadorpass.viewmodel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ambassadorpass.repository.CreatePartyRepository

class CreatePartyViewModel(private val repository: CreatePartyRepository) : ViewModel() {

    var partyName: String? = null
    var partyDate: String? = null
    val selectedImages: MutableList<Uri> = mutableListOf()

    private val _partyCreationStatus = MutableLiveData<Boolean>()
    val partyCreationStatus: LiveData<Boolean> get() = _partyCreationStatus

    fun createParty() {
        val name = partyName ?: return
        val date = partyDate ?: return

        repository.createParty(
            partyName = name,
            partyDate = date,
            partyDescription = "",
            partyLocation = "",
            ticketsAvailable = 0,
            selectedImages = selectedImages,
            callback = { success ->
                _partyCreationStatus.postValue(success)
            }
        )
    }
}

