package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ambassadorpass.repository.PartyRegistrationRepository

class PageTwoViewModel(private val repository: PartyRegistrationRepository) : ViewModel() {

    val isSubmitting = MutableLiveData<Boolean>()
    val submissionSuccess = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    fun submitAttendee(
        name: String,
        gender: String,
        age: String,
        email: String,
        phone: String,
        identification: String,
        partyLink: String
    ) {
        isSubmitting.value = true
        repository.addAttendee(
            name = name,
            gender = gender,
            age = age,
            email = email,
            phone = phone,
            identification = identification,
            partyLink = partyLink
        ) { success, error ->
            isSubmitting.value = false
            if (success) {
                submissionSuccess.value = true
            } else {
                errorMessage.value = error ?: "An error occurred during submission."
            }
        }
    }
}
