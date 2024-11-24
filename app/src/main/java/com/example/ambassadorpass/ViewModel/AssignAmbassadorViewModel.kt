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

    fun fetchParties() {
        repository.fetchParties { parties ->
            _partyList.postValue(parties)
        }
    }

    fun createAmbassadorAccount(
        email: String,
        name: String,
        partyId: String,
        callback: (Boolean) -> Unit
    ) {
        repository.createAmbassadorAccount(email, name, partyId, callback)
    }
}
