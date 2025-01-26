package com.padelium.data.mappers


import com.padelium.data.dto.ExtrasResponseDTO
import com.padelium.data.dto.SharedExtrasResponseDTO
import com.padelium.domain.dto.Extra
import com.padelium.domain.dto.ExtrasResponse
import com.padelium.domain.dto.SharedExtrasResponse
import javax.inject.Inject


class SharedExtrasMapper @Inject constructor() {

    fun SharedExtrasRequestToSharedExtrasResponseDTO(sharedextrasResponse: List<SharedExtrasResponseDTO>): List<SharedExtrasResponse> {
        return sharedextrasResponse.map { response ->
            SharedExtrasResponse(
                id = response.id,
                name = response.name,
                code = response.code,
                description = response.description ?: "", // Provide default empty string if null
                picture = response.picture ?: "", // Provide default empty string if null
                amount = response.amount, // Assuming you want to convert BigDecimal to Double
                currencyName = response.currencyName.toString(),
                isShared = response.isShared,
                currencyId = response.currencyId,

                )
        }
    }
}
