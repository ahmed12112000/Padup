package com.padelium.domain.repositories

import com.padelium.domain.dto.FetchKeyRequest
import com.padelium.domain.dto.FetchKeyResponse
import com.padelium.domain.dto.LoginRequest
import com.padelium.domain.dto.SignupRequest
import okhttp3.RequestBody
import retrofit2.Response


interface IUserRepository {

    suspend fun loginUser(loginRequest: LoginRequest): Response<Void>
    suspend fun signupUser(loginRequest: SignupRequest): Response<Void>

}