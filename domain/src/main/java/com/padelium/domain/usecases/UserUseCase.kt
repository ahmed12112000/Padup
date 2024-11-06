package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dto.LoginRequest
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.SignupRequest
import com.padelium.domain.repositories.IUserRepository
import javax.inject.Inject

class UserUseCase @Inject constructor(private val userRepository: IUserRepository) {

    suspend fun loginUser(loginRequest: LoginRequest): DataResult {
        return try {
            val response = userRepository.loginUser(loginRequest)
            if (response.isSuccessful) {
                Log.e("TAG", "Login result: ${response.code()}")
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during login")
        }
    }

    suspend fun signupUser(signupRequest: SignupRequest): DataResult {
        return try {
            val response = userRepository.signupUser(signupRequest)
            if (response.isSuccessful) {
                Log.e("TAG", "Signup result: ${response.code()}")
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during signup")
        }
    }
}