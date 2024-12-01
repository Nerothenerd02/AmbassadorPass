package com.example.ambassadorpass.Model

import com.google.firebase.Timestamp

data class Party(
    val partyName: String,
    val partyId: String,
    val partyDate: Timestamp?,
    val partyDescription: String,
    val partyLocation: String,
    val ticketPrice: Int,
    val ticketsAvailable: Int,
    val ticketsSold: Int,
    val partyLinks: List<String> = listOf()
) {

    constructor() : this("", "", null, "", "", 0, 0, 0)
}

