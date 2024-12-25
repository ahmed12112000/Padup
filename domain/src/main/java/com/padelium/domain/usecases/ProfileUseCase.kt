package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.ProfileRequest
import com.padelium.domain.repositories.IProfileRepository
import javax.inject.Inject


class ProfileUseCase @Inject constructor(private val profileRepository: IProfileRepository) {

    suspend fun Profile(profileRequest: ProfileRequest): DataResult {
        return try {
            val response = profileRepository.Profile(profileRequest)
            if (response.isSuccessful) {
                Log.e("TAG", "Profile result: ${response.code()}")
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during Profile call")
        }
    }
}