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
import com.padelium.data.dto.UserAvoirRequestDTO
import com.padelium.data.dto.UserAvoirResponseDTO
import com.padelium.domain.dto.ConfirmBookingResponse
import com.padelium.domain.dto.FetchKeyResponse
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.GetInitResponse
import com.padelium.domain.dto.GetPaymentResponse
import com.padelium.domain.dto.InitBookingResponse
import com.padelium.domain.dto.PaymentResponse
import com.padelium.domain.dto.SaveBookingRequest
import com.padelium.domain.dto.SaveBookingResponse
import com.padelium.domain.dto.SearchListResponse
import javax.inject.Inject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response

class PadeliumApi @Inject constructor(private val endPoint: PadeliumEndPoint) {


    suspend fun loginUser(username: String, password: String): Response<ResponseBody> {
        return endPoint.loginUser(username, password)
    }


    suspend fun GetPacks(): Response<List<GetPacksResponseDTO>> {
        return endPoint.GetPacks()
    }

    suspend fun GetCreditPay(): Response<List<CreditPayResponseDTO>> {
        return endPoint.GetCreditPay()
    }

    suspend fun GetReservation(): Response<List<GetReservationResponseDTO>> {
        return endPoint.GetReservation()
    }

    suspend fun GetProfile(): Response<GetProfileResponseDTO> {
        return endPoint.GetProfile()
    }

    suspend fun  signup(signupRequest: SignupRequestDTO): Response<Void>{
        return endPoint.signup(signupRequest)
    }

    suspend fun  Profile(profileRequest: ProfileRequestDTO): Response<Void>{
        return endPoint.Profile(profileRequest)
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


    suspend fun SaveBooking (saveBookingRequest: List<GetBookingResponse>): Response<List<SaveBookingResponse>> {
        return endPoint.SaveBooking(saveBookingRequest)
    }

    suspend fun Extras(): Response<List<ExtrasResponseDTO>> {
        return endPoint.Extras()
    }

    suspend fun PaymentAvoir (userAvoirRequest: UserAvoirRequestDTO):  Response<UserAvoirResponseDTO> {
        return endPoint.PaymentAvoir(userAvoirRequest)
    }
//   UserAvoirRequestDTO
    suspend fun Payment (paymentRequest: PaymentRequestDTO): Response<PaymentResponse?> {
        return endPoint.Payment(paymentRequest)
    }


    suspend fun GetPayment (getPaymentRequest: GetPaymentRequestDTO): Response<GetPaymentResponse> {
        return endPoint.GetPayment(getPaymentRequest)
    }


    suspend fun ConfirmBooking (confirmBookingRequest: ConfirmBookingRequestDTO): Response<ConfirmBookingResponse> {
        return endPoint.ConfirmBooking(confirmBookingRequest)
    }

//    hamma2574@gmail.com     HibA98821607



}