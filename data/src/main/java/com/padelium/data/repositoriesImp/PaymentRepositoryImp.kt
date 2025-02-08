package com.padelium.data.repositoriesImp

import android.util.Log
import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.dto.PaymentRequestDTO
import com.padelium.data.mappers.PaymentMapper
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.dto.PaymentResponse
import com.padelium.domain.repositories.IPaymentRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject


class PaymentRepositoryImp @Inject constructor(
    private val api: PadeliumApi,
    private val mapper: PaymentMapper
) : IPaymentRepository {

    override suspend fun Payment(paymentRequest: PaymentRequest): Response<PaymentResponse?> {
        val paymentRequestDTO = mapper.PaymentRequestToPaymentRequestDTO(paymentRequest)
        val response = api.Payment(paymentRequestDTO)

        return if (response.isSuccessful) {
            val body = response.body()

            if (body != null) {
                Log.d("PaymentRepositoryImp", "Response Body: $body")
                response
            } else {
                Log.e("PaymentRepositoryImp", "Empty response body")
                Response.success(null) // Treat as a successful response with no body
            }
        } else {
            val errorBody = response.errorBody()?.string()
            Log.e("PaymentRepositoryImp", "Error response: $errorBody")
            Response.error(response.code(), response.errorBody() ?: ResponseBody.create(null, "Unknown error"))
        }
    }

}



