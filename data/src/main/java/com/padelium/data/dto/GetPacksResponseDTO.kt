package com.padelium.data.dto

import com.padelium.domain.dto.currency

data class GetPacksResponseDTO(

    val id: Long,
    val name: String,
    val description: String,
    val code: String,
    val amount: Double,
    val isonline:Boolean,
    val title: String,
    val currency: currency,


    )
