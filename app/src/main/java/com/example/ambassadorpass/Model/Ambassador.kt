package com.example.ambassadorpass.Model

data class Ambassador(
    val uid: String = "",
    val id: String = "",
    val name: String = "",
    val inviteCount: Int = 0,
    val earnings: Double = 0.0,
    val partyId: String = "",
    val email: String,
    val tier: String,
    val ambassadorID: String,
    val partyLink: String = "",
)