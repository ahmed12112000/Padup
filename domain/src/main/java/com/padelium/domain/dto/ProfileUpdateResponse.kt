package com.padelium.domain.dto

data class ProfileUpdateResponse(

    val imageUrl: String,
    val phone: String,
    val authorities: List<Unit>
)
