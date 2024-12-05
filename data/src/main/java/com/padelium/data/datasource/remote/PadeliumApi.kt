package com.padelium.data.datasource.remote

import com.nevaDev.padeliummarhaba.repository.LoginRequestDto
import com.padelium.data.dto.ConfirmBookingRequestDTO
import com.padelium.data.dto.ExtrasRequestDTO
import com.padelium.data.dto.FetchKeyRequestDTO
import com.padelium.data.dto.GetPaymentRequestDTO
import com.padelium.data.dto.InitBookingRequestDTO
import com.padelium.data.dto.PaymentRequestDTO
import com.padelium.data.dto.SaveBookingRequestDTO
import com.padelium.data.dto.SignupRequestDTO
import com.padelium.domain.dto.ConfirmBookingResponse
import com.padelium.domain.dto.ExtrasRequest
import com.padelium.domain.dto.ExtrasResponse
import com.padelium.domain.dto.FetchKeyResponse
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.GetInitResponse
import com.padelium.domain.dto.GetPaymentResponse
import com.padelium.domain.dto.InitBookingResponse
import com.padelium.domain.dto.PaymentResponse
import com.padelium.domain.dto.SaveBookingResponse
import com.padelium.domain.dto.SearchListResponse
import retrofit2.http.Headers
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response

class PadeliumApi @Inject constructor(private val endPoint: PadeliumEndPoint) {


    suspend fun  login(loginRequest: LoginRequestDto): Response<Void>{
        return endPoint.loginUser(loginRequest)
    }


    suspend fun  signup(signupRequest: SignupRequestDTO): Response<Void>{
        return endPoint.signup(signupRequest)
    }

    suspend fun  getReservationKey(fetchKeyRequest: FetchKeyRequestDTO): Response<FetchKeyResponse>{
        return endPoint.getReservationKey(fetchKeyRequest)
    }


    suspend fun GetInit (key: String): Response<GetInitResponse> {
        val requestBody = key.toRequestBody("application/json".toMediaTypeOrNull())
        return endPoint.GetInit(requestBody)
    }



    suspend fun SearchList (key: String): Response<List<SearchListResponse>> {
        val requestBody = key.toRequestBody("application/json".toMediaTypeOrNull())
        return endPoint.SearchList(requestBody)
    }



    suspend fun InitBooking (initBookingRequest: InitBookingRequestDTO): Response<List<InitBookingResponse>> {
    return endPoint.InitBooking(initBookingRequest)
    }


    suspend fun GetBooking (key: String): Response<List<GetBookingResponse>> {
            val requestBody = key.toRequestBody("application/json".toMediaTypeOrNull())
            return endPoint.GetBooking(requestBody)
    }


    suspend fun SaveBooking (saveBookingRequest: List<SaveBookingRequestDTO>): Response<List<SaveBookingResponse>> {
        return endPoint.SaveBooking(saveBookingRequest)
    }


    suspend fun Extras (extrasRequest: List<ExtrasRequestDTO>): Response<List<ExtrasResponse>> {
        return endPoint.Extras(extrasRequest)
    }


    suspend fun Payment (paymentRequest: PaymentRequestDTO): Response<PaymentResponse> {
        return endPoint.Payment(paymentRequest)
    }


    suspend fun GetPayment (getPaymentRequest: GetPaymentRequestDTO): Response<List<GetPaymentResponse>> {
        return endPoint.GetPayment(getPaymentRequest)
    }


    suspend fun ConfirmBooking (confirmBookingRequest: ConfirmBookingRequestDTO): Response<ConfirmBookingResponse> {
        return endPoint.ConfirmBooking(confirmBookingRequest)
    }

//    hamma2574@gmail.com     HibA98821607



}