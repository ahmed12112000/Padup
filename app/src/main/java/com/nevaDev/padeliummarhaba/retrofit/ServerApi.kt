package com.nevaDev.padeliummarhaba.retrofit

import com.nevaDev.padeliummarhaba.models.GetBookingList
import com.nevaDev.padeliummarhaba.models.GetEstablishmentDTO
import com.nevaDev.padeliummarhaba.models.Getinitresponse
import com.nevaDev.padeliummarhaba.models.InitBookingList
import com.nevaDev.padeliummarhaba.models.InitBookingRequest
import com.nevaDev.padeliummarhaba.models.ResponseBody
import com.padelium.domain.dto.LoginRequest
import com.nevaDev.padeliummarhaba.repository.signup.SignupRequest
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ServerApi {
    @Headers("Accept: XSRF-TOKEN")
    @POST("/api/authentication/")
    suspend fun loginUser(@Body loginRequest: LoginRequest): Response<Void>

    @Headers("Accept: XSRF-TOKEN")
    @POST("/api/register")
    suspend fun signup(@Body request: SignupRequest): Response<Void>


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


    //getTimeSlots
    companion object {
        fun serverApi(): ServerApi? {
            return ApiClient.client?.create(ServerApi::class.java)
        }
    }
}