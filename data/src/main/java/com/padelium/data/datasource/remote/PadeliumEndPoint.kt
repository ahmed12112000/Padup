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
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PadeliumEndPoint {

    @Headers("Accept: application/json", "Content-Type: application/json","Accept: XSRF-TOKEN")
    @POST("/api/authentication/")
    suspend fun loginUser(@Body loginRequest: LoginRequestDto): Response<Void>

    @Headers("Accept: XSRF-TOKEN")
    @POST("/api/register")
    suspend fun signup(@Body request: SignupRequestDTO): Response<Void>


    @POST("/api/establishments/search/init")
    suspend fun getReservationKey(@Body request: FetchKeyRequestDTO): Response<FetchKeyResponse>

    @Headers("Content-Type: application/json")
    @POST("/api/establishments/search/get/init")
    suspend fun GetInit(@Body key: RequestBody): Response<GetInitResponse>


    @Headers("Content-Type: application/json")
    @POST("/api/establishments/search/list")
    suspend fun SearchList(@Body key: RequestBody): Response<List<SearchListResponse>>

    @Headers("Content-Type: application/json")
    @POST("api/establishments/search/init/booking")
    suspend fun InitBooking(@Body request: InitBookingRequestDTO): Response<List<InitBookingResponse>>

    @Headers("Content-Type: application/json")
    @POST("api/establishments/search/get/booking")
    suspend fun GetBooking(@Body key: RequestBody): Response<List<GetBookingResponse>>

       @Headers("Content-Type: application/json")
    @POST("api/establishments/search/save/booking")
    suspend fun SaveBooking(@Body saveBookingRequest: List<SaveBookingRequestDTO>): Response<List<SaveBookingResponse>>

    @Headers("Content-Type: application/json")
    @POST("api/extras")
    suspend fun Extras(@Body extrasRequest: List<ExtrasRequestDTO>): Response<List<ExtrasResponse>>

        @Headers("Content-Type: application/json")
    @POST("api/payment")
    suspend fun Payment(@Body paymentRequest: PaymentRequestDTO): Response<PaymentResponse>

        @Headers("Content-Type: application/json")
    @POST("api/payment/get")
    suspend fun GetPayment(@Body getPaymentRequest: GetPaymentRequestDTO): Response<List<GetPaymentResponse>>

    @Headers("Content-Type: application/json")
    @POST("api/establishments/search/confirm/booking")
    suspend fun ConfirmBooking (@Body confirmBookingRequest: ConfirmBookingRequestDTO): Response<ConfirmBookingResponse>

}