package com.padelium.domain.repositories

import com.padelium.domain.dto.BalanceResponse
import com.padelium.domain.dto.GetEmailResponse
import retrofit2.Response

interface IGetEmailRepository {
    suspend fun GetEmail (bookingIds:List<Long>): Response<Long>

}