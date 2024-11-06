package com.padelium.data.datasource.remote

import com.nevaDev.padeliummarhaba.repository.LoginRequestDto
import com.padelium.data.dto.GetBookingList
import com.padelium.data.dto.GetEstablishmentDTO
import com.padelium.data.dto.Getinitresponse
import com.padelium.data.dto.InitBookingList
import com.padelium.data.dto.InitBookingRequest
import com.padelium.data.dto.SignupRequestDTO
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface PadeliumEndPoint {

    @Headers("Accept: XSRF-TOKEN")
    @POST("/api/authentication/")
    suspend fun loginUser(@Body loginRequest: LoginRequestDto): Response<Void>

    @Headers("Accept: XSRF-TOKEN")
    @POST("/api/register")
    suspend fun signup(@Body request: SignupRequestDTO): Response<Void>


    @POST("/api/establishments/search/init")
    suspend fun getReservationKey(@Body request: RequestBody): Response<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("/api/establishments/search/get/init")
    suspend fun searchinit(@Body key: RequestBody): Response<Getinitresponse>



    @Headers("Content-Type: application/json")
    @POST("/api/establishments/search/list")
    suspend fun searchlist(@Body key: RequestBody): Response<List<GetEstablishmentDTO>>


    @POST("api/establishments/search/init/booking")
    suspend fun initBooking(@Body request: InitBookingRequest): Response<List<InitBookingList>>


    @POST("api/establishments/search/get/booking")
    suspend fun GetBookingResponse(@Body key: RequestBody): Response<List<GetBookingList>>


}