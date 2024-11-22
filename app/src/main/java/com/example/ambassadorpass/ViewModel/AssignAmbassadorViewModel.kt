package com.example.ambassadorpass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ambassadorpass.Model.Party
import com.example.ambassadorpass.Repository.AssignAmbassadorRepository

class AssignAmbassadorViewModel : ViewModel() {

    private val repository = AssignAmbassadorRepository()

    private val _partyList = MutableLiveData<List<Party>>()
    val partyList: LiveData<List<Party>> get() = _partyList

    fun fetchParties() {
        repository.fetchParties { parties ->
            _partyList.postValue(parties)
        }
    }
}
