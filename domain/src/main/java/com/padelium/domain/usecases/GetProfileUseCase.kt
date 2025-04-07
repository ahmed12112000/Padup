package com.padelium.domain.usecases

import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.GetProfileResponse
import com.padelium.domain.repositories.IGetProfileRepository
import javax.inject.Inject

class GetProfileUseCase @Inject constructor(private val repository: IGetProfileRepository) {
    suspend fun execute(): DataResultBooking<GetProfileResponse> {
        return try {
            val getProfileResponse = repository.GetProfile()

            // Perform internal validation on the returned response
            // For example, if you want to consider null/invalid fields as failure
            if (getProfileResponse != null) {
                DataResultBooking.Success(getProfileResponse)
            } else {
                DataResultBooking.Failure(null, null, "Empty response body")
            }
        } catch (e: Exception) {
            DataResultBooking.Failure(
                e,
                null,
                e.localizedMessage ?: "An error occurred during the GetProfile request"
            )
        }
    }
}

