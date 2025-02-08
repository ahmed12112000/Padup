package com.padelium.domain.repositories

import com.padelium.domain.dto.SharedExtrasResponse

interface ISharedExtrasRepository {
    suspend fun SharedExtras(): List<SharedExtrasResponse>

}