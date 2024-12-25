package com.padelium.domain.dto

data class GetPacksResponse(
    val id: Long,
    val name: String,
    val description: String,
    val code: String,
    val amount: Double,
    val isonline:Boolean,
    val title: String,
    val currency: currency,


    )
