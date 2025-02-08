package com.padelium.data.dto

data class SignupRequestDTO(
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String,
    val langKey: String = "fr" // Default value for langKey
) {
    val login: String = email // Assign login to be the same as email
}
