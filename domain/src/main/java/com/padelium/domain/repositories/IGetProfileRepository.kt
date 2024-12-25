package com.padelium.domain.repositories

import com.padelium.domain.dto.GetProfileResponse

interface IGetProfileRepository {
    suspend fun GetProfile(): GetProfileResponse
}