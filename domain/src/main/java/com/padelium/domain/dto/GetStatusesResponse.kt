package com.padelium.domain.dto

data class GetStatusesResponse(

    val id: Long,
    val name: String,
    val code: String,
    val created: String,
    val updated: String,
    val isshow: Boolean,

    )
