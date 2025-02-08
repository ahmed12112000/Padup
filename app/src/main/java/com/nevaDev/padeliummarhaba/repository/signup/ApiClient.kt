package com.nevaDev.padeliummarhaba.repository.signup

// ApiClient.kt

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "http://141.94.246.248/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApiService(): UserApi {
        return retrofit.create(UserApi::class.java)
    }
}
