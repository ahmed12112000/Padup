package com.padelium.data.mappers

import com.padelium.data.dto.GetPaymentRequestDTO
import com.padelium.data.dto.GetPaymentResponseDTO
import com.padelium.domain.dto.GetPaymentRequest
import com.padelium.domain.dto.GetPaymentResponse
import javax.inject.Inject


class GetPaymentMapper @Inject constructor(){

    fun GetPaymentRequestToGetPaymentRequestDTO (getPaymentRequest: GetPaymentRequest): GetPaymentRequestDTO {
        return GetPaymentRequestDTO(
            couponIds = getPaymentRequest.couponIds,
            numberOfPart = getPaymentRequest.numberOfPart,
            orderId = getPaymentRequest.orderId,
            privateExtrasIds = getPaymentRequest.privateExtrasIds,
            sharedExtrasIds = getPaymentRequest.sharedExtrasIds,
            userIds = getPaymentRequest.userIds,
            bookingIds = getPaymentRequest.bookingIds,


 )
    }

    fun GetPaymentResponseToGetPaymentResponseDTO(GetPaymentResponse: GetPaymentResponse): GetPaymentResponseDTO {
    return GetPaymentResponseDTO(
        data = GetPaymentResponse.data,
)
}

}
