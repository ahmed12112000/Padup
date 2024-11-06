package com.padelium.data.datasource.remote

import com.nevaDev.padeliummarhaba.repository.LoginRequestDto
import com.padelium.data.dto.SignupRequestDTO
import retrofit2.Response
import retrofit2.http.Headers
import javax.inject.Inject

class PadeliumApi @Inject constructor(private val endPoint: PadeliumEndPoint) {
    @Headers("Accept: application/json", "Content-Type: application/json")

    suspend fun  login(loginRequest: LoginRequestDto): Response<Void>{
        return endPoint.loginUser(loginRequest)
    }
    @Headers("Accept: application/json", "Content-Type: application/json")

    suspend fun  signup(signupRequest: SignupRequestDTO): Response<Void>{
        return endPoint.signup(signupRequest)
    }
}