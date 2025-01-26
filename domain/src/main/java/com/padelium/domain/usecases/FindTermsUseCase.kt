package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IFindTermsRepository
import okhttp3.RequestBody
import javax.inject.Inject


class FindTermsUseCase @Inject constructor(
    private val findTermsRepository: IFindTermsRepository
) {

    suspend fun FindTerms(term: RequestBody): DataResult {
        return try {
            // Call the repository to get the response
            val response = findTermsRepository.FindTerms(term)

            // Check if the response is successful
            if (response.isSuccessful) {
                Log.e("TAG", "GetManager result: HTTP ${response.code()}")

                // Extract the body and ensure it's a List<FindTermsResponse>
                val body = response.body()
                if (body != null) {
                    DataResult.Success(body) // Return the successful result with the body
                } else {
                    // Handle the case where the body is null
                    Log.e("TAG", "Response body is null")
                    DataResult.Failure(null, response.code(), "Response body is null")
                }
            } else {
                // Handle the error response
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                Log.e("TAG", "Error in GetManager: $errorMessage")
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            // Handle any exceptions that occur during the request
            Log.e("TAG", "Exception in GetManager: ${ex.localizedMessage}", ex)
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during the request")
        }
    }
}
