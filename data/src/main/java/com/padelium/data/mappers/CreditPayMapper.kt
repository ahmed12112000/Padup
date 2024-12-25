package com.padelium.data.mappers

import com.padelium.data.dto.CreditPayResponseDTO
import com.padelium.domain.dto.CreditPayResponse
import javax.inject.Inject


class CreditPayMapper @Inject constructor() {

    fun CreditPayResponseToCreditPayResponseDTO(creditPayResponseList: List<CreditPayResponseDTO>): List<CreditPayResponse> {
        return creditPayResponseList.map { response ->
            CreditPayResponse(
                id = response.id,
                amount = response.amount,
                created = response.created ?: "", // Provide a default value for nullable fields
                updated = response.updated ?: "", // Default to empty string if null
                createdBy = response.createdBy,
                updatedBy = response.updatedBy ?: 0, // Default to 0 if null
                description = response.description ?: "", // Default to empty string
                userId = response.userId,
                userLogin = response.userLogin ?: "", // Default to empty string
                userFirstName = response.userFirstName ?: "", // Default to empty string
                userLastName = response.userLastName ?: "", // Default to empty string
                bookingId = response.bookingId ?: 0, // Default to 0 if null
                bookingReference = response.bookingReference ?: "", // Default to empty string
                bookingSellAmount = response.bookingSellAmount ?: "", // Default to empty string
                bookingCreation = response.bookingCreation ?: "", // Default to empty string
                bookingEstablishmentName = response.bookingEstablishmentName ?: "", // Default to empty string
                createdByFirstName = response.createdByFirstName ?: "", // Default to empty string
                createdByLastName = response.createdByLastName ?: "", // Default to empty string
                createdByLogin = response.createdByLogin ?: "", // Default to empty string
                token = response.token ?: "", // Default to empty string
                transactionId = response.transactionId ?: "", // Default to empty string
                userAvoirTypeId = response.userAvoirTypeId,
                userAvoirTypeName = response.userAvoirTypeName ?: "", // Default to empty string
                createdStr = response.createdStr ?: "", // Default to empty string
                buyerId = response.buyerId ?: "", // Default to empty string
                packId = response.packId ?: "", // Default to empty string
                bookingEstablishmentCode = response.bookingEstablishmentCode ?: "" // Default to empty string
            )
        }
    }

}
