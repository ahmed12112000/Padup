package com.padelium.domain.repositories

import com.padelium.domain.dto.GetPacksResponse

interface IGetPacksRepository {

        suspend fun GetPacks(): List<GetPacksResponse>

}