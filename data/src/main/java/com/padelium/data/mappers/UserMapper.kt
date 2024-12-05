package com.padelium.data.mappers


import com.nevaDev.padeliummarhaba.repository.LoginRequestDto
import com.padelium.data.dto.FetchKeyRequestDTO
import com.padelium.data.dto.SignupRequestDTO
import com.padelium.domain.dto.FetchKeyRequest
import com.padelium.domain.dto.LoginRequest
import com.padelium.domain.dto.SignupRequest
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class UserMapper @Inject constructor(){

    fun loginRequestToLoginRequestDto(loginRequest: LoginRequest): LoginRequestDto {
        return LoginRequestDto(
            username = loginRequest.username,
            password = loginRequest.password
        )
    }


    fun signupRequestToSignupRequestDto(signupRequest: SignupRequest): SignupRequestDTO {
        return SignupRequestDTO(
            firstName = signupRequest.firstName,
            lastName = signupRequest.lastName,
            password = signupRequest.password,
            email = signupRequest.email
        )
    }

}