package com.padelium.domain.dto

data class UpddateProfileRequest(
    val name: String,
    val email: String,
    val phone: String,
    val lastname: String
)
