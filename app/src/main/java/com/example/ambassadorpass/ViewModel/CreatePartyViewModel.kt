package com.example.ambassadorpass.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ambassadorpass.repository.CreatePartyRepository

class CreatePartyViewModel(private val repository: CreatePartyRepository) : ViewModel() {

    var partyName: String? = null
    var partyDate: String? = null
    var partyDescription: String? = null
    var partyLocation: String? = null
    var ticketsAvailable: Int? = null
    var ticketPrice: Double? = null
    var ambassadorMarkup: Double? = null

    // To store the clicked ImageView ID
    var selectedImageViewId: Int? = null

    // Map to store selected images with their respective image view IDs
    val selectedImages: MutableMap<Int, Uri> = mutableMapOf()

    private val _partyCreationStatus = MutableLiveData<Boolean>()
    val partyCreationStatus: LiveData<Boolean> get() = _partyCreationStatus

    // Function to update the selected image
    fun updateSelectedImage(imageViewId: Int, uri: Uri) {
        selectedImages[imageViewId] = uri
    }

    fun createParty() {
        val name = partyName ?: run {
            Log.e("CreatePartyViewModel", "Party name is null")
            return
        }
        val date = partyDate ?: run {
            Log.e("CreatePartyViewModel", "Party date is null")
            return
        }
        val description = partyDescription ?: run {
            Log.e("CreatePartyViewModel", "Party description is null")
            return
        }
        val location = partyLocation ?: run {
            Log.e("CreatePartyViewModel", "Party location is null")
            return
        }
        val tickets = ticketsAvailable ?: run {
            Log.e("CreatePartyViewModel", "Tickets available is null")
            return
        }
        val price = ticketPrice ?: run {
            Log.e("CreatePartyViewModel", "Ticket price is null")
            return
        }
        val markup = ambassadorMarkup ?: run {
            Log.e("CreatePartyViewModel", "Ambassador markup is null")
            return
        }

        Log.d("CreatePartyViewModel", "Creating party with name: $name, date: $date, description: $description, location: $location, tickets: $tickets, price: $price, markup: $markup")

        repository.createParty(
            partyName = name,
            partyDescription = description,
            partyDate = date,
            partyLocation = location,
            ticketsAvailable = tickets,
            ticketPrice = price,
            ambassadorMarkup = markup,
            selectedImages = selectedImages.values.toList(),
            callback = { success ->
                Log.d("CreatePartyViewModel", "Party creation status: $success")
                _partyCreationStatus.postValue(success)
            }
        )
    }}
