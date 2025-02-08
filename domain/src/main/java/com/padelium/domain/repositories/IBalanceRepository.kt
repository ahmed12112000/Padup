package com.padelium.domain.repositories

import com.padelium.domain.dto.BalanceResponse
import com.padelium.domain.dto.UserAvoirPayResponse
import retrofit2.Response


interface IBalanceRepository {
       suspend fun Balance (Id: Long): Response<BalanceResponse>
}

