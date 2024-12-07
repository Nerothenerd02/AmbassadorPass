package com.example.ambassadorpass.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ambassadorpass.Model.Party
import com.example.ambassadorpass.Repository.AssignAmbassadorRepository

class AssignAmbassadorViewModel(private val context: Context) : ViewModel() {

    private val repository = AssignAmbassadorRepository(context)

    private val _partyList = MutableLiveData<List<Party>>()
    val partyList: LiveData<List<Party>> get() = _partyList

    // Fetch the list of parties from the repository
    fun fetchParties() {
        repository.fetchParties { parties: List<Party> -> // Explicitly define the type
            _partyList.postValue(parties)
        }
    }


    // New function to handle ambassador submission
    fun handleAmbassadorSubmission(
        email: String,
        name: String,
        partyId: String,
        callback: (Boolean, String?) -> Unit
    ) {
        repository.handleAmbassadorSubmission(email, name, partyId, callback)
    }

}
