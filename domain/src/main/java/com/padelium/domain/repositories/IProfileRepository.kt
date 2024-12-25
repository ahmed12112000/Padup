package com.padelium.domain.repositories

import com.padelium.domain.dto.ProfileRequest
import retrofit2.Response

interface IProfileRepository {
    suspend fun Profile (profileRequest: ProfileRequest): Response<Void>

}