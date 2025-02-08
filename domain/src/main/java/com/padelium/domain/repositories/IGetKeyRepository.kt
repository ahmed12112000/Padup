package com.padelium.domain.repositories

import com.padelium.domain.dto.FetchKeyRequest
import com.padelium.domain.dto.FetchKeyResponse
import retrofit2.Response

interface IGetKeyRepository {
    suspend fun getReservationKey(fetchKeyRequest: FetchKeyRequest): Response<FetchKeyResponse>

}