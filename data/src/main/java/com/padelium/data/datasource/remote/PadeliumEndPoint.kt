package com.padelium.data.datasource.remote

import com.padelium.data.dto.ConfirmBookingRequestDTO
import com.padelium.data.dto.CreditPayResponseDTO
import com.padelium.data.dto.ExtrasResponseDTO
import com.padelium.data.dto.FetchKeyRequestDTO
import com.padelium.data.dto.GetPacksResponseDTO
import com.padelium.data.dto.GetPaymentRequestDTO
import com.padelium.data.dto.GetProfileResponseDTO
import com.padelium.data.dto.GetReservationResponseDTO
import com.padelium.data.dto.InitBookingRequestDTO
import com.padelium.data.dto.PaymentRequestDTO
import com.padelium.data.dto.ProfileRequestDTO
import com.padelium.data.dto.SignupRequestDTO
import com.padelium.data.dto.UserAvoirPayRequestDTO
import com.padelium.data.dto.UserAvoirPayResponseDTO
import com.padelium.data.dto.UserAvoirRequestDTO
import com.padelium.data.dto.UserAvoirResponseDTO
import com.padelium.domain.dto.BalanceResponse
import com.padelium.domain.dto.ConfirmBookingResponse
import com.padelium.domain.dto.FetchKeyResponse
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.GetInitResponse
import com.padelium.domain.dto.GetPaymentResponse
import com.padelium.domain.dto.InitBookingResponse
import com.padelium.domain.dto.PaymentResponse
import com.padelium.domain.dto.SaveBookingResponse
import com.padelium.domain.dto.SearchListResponse
import com.padelium.domain.dto.UserAvoirPayResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import java.math.BigDecimal

interface PadeliumEndPoint {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/x-www-form-urlencoded"
    )
    @FormUrlEncoded
    @POST("api/authentication")
    suspend fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST("/api/register")
    suspend fun signup(@Body request: SignupRequestDTO): Response<Void>

    @Headers("Content-Type: application/json")
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



    @Headers("Accept: application/json")
    @POST("/api/account")
    suspend fun Profile(@Body profileRequest: ProfileRequestDTO): Response<Void>


    @Headers("Accept: application/json" )
    @GET("api/packs/online")
    suspend fun GetPacks(): Response<List<GetPacksResponseDTO>>

    @Headers("Accept: application/json" )
    @GET("api/user-avoirs")
    suspend fun GetCreditPay(): Response<List<CreditPayResponseDTO>>

    @Headers("Accept: application/json" )
    @GET("api/bookings")
    suspend fun GetReservation(): Response<List<GetReservationResponseDTO>>

    @Headers("Accept: application/json")
    @GET("/api/account")
    suspend fun GetProfile(): Response<GetProfileResponseDTO>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @GET("api/extras")
    suspend fun Extras(): Response<List<ExtrasResponseDTO>>




    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/establishments/search/save/booking")
    suspend fun SaveBooking(@Body saveBookingRequest: List<GetBookingResponse>): Response<List<SaveBookingResponse>>


    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/payment")
    suspend fun Payment(@Body paymentRequest: PaymentRequestDTO): Response<PaymentResponse?>


    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/user-avoirs/pay/from/avoir")
    suspend fun PaymentPayAvoir(@Body amount: BigDecimal): Response<UserAvoirPayResponse>


    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/user-avoirs/balance/userId")
    suspend fun Balance(@Body Id: Long): Response<BalanceResponse>

    @Headers("Accept: application/json", "Content-Type: application/json")
    @POST("api/payment/user/avoir")
    suspend fun PaymentAvoir(@Body userAvoirRequest: UserAvoirRequestDTO): Response<UserAvoirResponseDTO>

    @Headers("Content-Type: application/json")
    @POST("api/payment/get")
    suspend fun GetPayment(@Body getPaymentRequest: GetPaymentRequestDTO): Response<GetPaymentResponse>

    @Headers("Content-Type: application/json")
    @POST("api/establishments/search/confirm/booking")
    suspend fun ConfirmBooking (@Body confirmBookingRequest: ConfirmBookingRequestDTO): Response<ConfirmBookingResponse>

} // git remote add gitlab https://gitlab.com/nevadev/padelium-marhaba-android-app.git
//git push -u -f gitlab NewArchAuthentication