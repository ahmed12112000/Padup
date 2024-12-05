package com.padelium.data.mappers

import com.padelium.data.dto.ExtrasRequestDTO
import com.padelium.data.dto.ExtrasResponseDTO
import com.padelium.domain.dto.ExtrasRequest
import javax.inject.Inject


class ExtrasMapper @Inject constructor(){

        fun ExtrasRequestToExtrasResponseDTO(extrasRequest: List<ExtrasRequest>): List<ExtrasRequestDTO> {
            return extrasRequest.map { extrasRequest ->
                ExtrasRequestDTO(
                    id = extrasRequest.id,
                    name = extrasRequest.name,
                    code = extrasRequest.code,
                    description = extrasRequest.description,
                    picture = extrasRequest.picture,
                    amount = extrasRequest.amount,
                    currencyId = extrasRequest.currencyId,
                    currencyName = extrasRequest.currencyName,
                    isShared = extrasRequest.isShared
                )
            }
        }
    }
