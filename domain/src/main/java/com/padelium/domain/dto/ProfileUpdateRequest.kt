package com.padelium.domain.dto

data class ProfileUpdateRequest(
    val firstName: String,
    val lastName:String,
    val email: String,
    val activated: Boolean,
    val langKey: String = "fr",
    val login: String = email,
    val imageUrl: String,
    val phone: String,
    val authorities: List<Unit>


)
