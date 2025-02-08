package com.padelium.domain.repositories

import com.padelium.domain.dto.PrivateExtrasResponse

interface IPrivateExtrasRepository {

    suspend fun PrivateExtras(): List<PrivateExtrasResponse>
}