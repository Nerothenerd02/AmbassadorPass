package com.example.ambassadorpass.Model

data class Admin(
    val id: String = "",
    val name: String = "",
    val privileges: List<String> = listOf() // List of privileges for the admin
)