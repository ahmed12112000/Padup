package com.padelium.domain.repositories

import com.padelium.domain.dto.BalanceResponse
import com.padelium.domain.dto.UserAvoirPayResponse
import retrofit2.Response
import java.math.BigDecimal


interface IBalanceRepository {
       suspend fun Balance (Id: Long): Response<BigDecimal>
}

