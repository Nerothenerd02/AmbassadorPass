package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ambassadorpass.repository.PartyRegistrationRepository

class PageTwoViewModel(private val repository: PartyRegistrationRepository) : ViewModel() {

    val isSubmitting = MutableLiveData<Boolean>()
    val submissionSuccess = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    // Store attendee details
    var attendeeName: String = ""
    var attendeeGender: String = ""
    var attendeeAge: String = ""
    var attendeeEmail: String = ""
    var attendeePhone: String = ""
    var attendeeIdentification: String = ""
    var partyLink: String = ""

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

        // Save details to be passed to next activity
        attendeeName = name
        attendeeGender = gender
        attendeeAge = age
        attendeeEmail = email
        attendeePhone = phone
        attendeeIdentification = identification
        this.partyLink = partyLink

        repository.addAttendee(
            name, gender, age, email, phone, identification, partyLink
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


