package com.nevaDev.padeliummarhaba.repository

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("code")
    val code: Int,

    @SerializedName("data")
    val data: Data,

    @SerializedName("id")
    val id: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("csrfToken")
    val csrfToken: String? = null
) {
    data class Data(
        @SerializedName("Email")
        val email: String,

        @SerializedName("id")
        val id: String,

        @SerializedName("Id")
        val id2: Int,

        @SerializedName("Name")
        val name: String,

        @SerializedName("Token")
        val token: String
    )
}