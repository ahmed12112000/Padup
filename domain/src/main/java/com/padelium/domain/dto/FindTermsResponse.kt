package com.padelium.domain.dto

data class FindTermsResponse(

    val userFirstName: String,
    val userLastName: String,
    val maskedEmail: String,
    val fullName: String,
    val id: Long

)
