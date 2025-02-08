package com.nevaDev.padeliummarhaba.repository.signup

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface UserApi {
    @POST("api/register")
    fun signup(@Body request: SignupRequest): Call<SignupResponse>
}