package com.example.ambassadorpass.Model

data class User(
    val id: String = "",
    var tier: String = "user", // Change 'val' to 'var' to allow reassignment
    val name: String = "",
    var isFirstTimeUser: Boolean = true
)
