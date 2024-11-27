package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ambassadorpass.repository.PartyRegistrationRepository

class PageThreeViewModel(private val repository: PartyRegistrationRepository) : ViewModel() {

    private val _ticketPrice = MutableLiveData<Double?>()
    val ticketPrice: LiveData<Double?> get() = _ticketPrice

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _operationStatus = MutableLiveData<String>()
    val operationStatus: LiveData<String> get() = _operationStatus

    // Fetch ticket price from repository
    fun fetchTicketPrice(partyLink: String) {
        repository.fetchTicketPriceByPartyLink(partyLink) { price ->
            if (price != null) {
                _ticketPrice.postValue(price)
            } else {
                _errorMessage.postValue("Error: Could not fetch ticket price.")
            }
        }
    }

    // Process payment: Update tickets and attendee info
    fun processPayment(partyLink: String, identification: String, qrCodeData: String) {
        repository.updateTicketCounts(partyLink) { ticketSuccess, ticketMessage ->
            if (ticketSuccess) {
                repository.updateAttendeePaymentStatus(partyLink, identification, qrCodeData) { attendeeSuccess, attendeeMessage ->
                    if (attendeeSuccess) {
                        _operationStatus.postValue("Success: Payment processed and attendee updated.")
                    } else {
                        _errorMessage.postValue("Error updating attendee: $attendeeMessage")
                    }
                }
            } else {
                _errorMessage.postValue("Error updating tickets: $ticketMessage")
            }
        }
    }
}
