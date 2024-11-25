package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ambassadorpass.repository.PartyPreviewRepository

class PartyPreviewViewModel(private val repository: PartyPreviewRepository) : ViewModel() {

    private val _imageUrls = MutableLiveData<List<String>>()
    val imageUrls: LiveData<List<String>> get() = _imageUrls

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    // Fetch image URLs for a specific party link
    fun fetchImageUrls(partyLink: String) {
        repository.fetchImageUrls(partyLink) { urls, exception ->
            if (urls != null) {
                _imageUrls.postValue(urls)
            } else if (exception != null) {
                _error.postValue(exception.message)
            } else {
                _error.postValue("Party not found")
            }
        }
    }
}
