package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IBalanceRepository
import javax.inject.Inject


class BalanceUseCase @Inject constructor(private val balanceRepository: IBalanceRepository) {

    suspend fun Balance(Id: Long): DataResult {
        return try {
            val response = balanceRepository.Balance(Id)
            if (response.isSuccessful) {
                Log.e("TAG", "Balance result: ${response.code()}")
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during balance fetching")
        }
    }
}
