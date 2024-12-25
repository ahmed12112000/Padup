package com.padelium.domain.dto

data class Extra(
    val id: Long,
    val name: String,
    val code: String,
    val description: String,
    val picture: String,
    val amount: Double,
    val currency: String,
    val isShared: Boolean
)