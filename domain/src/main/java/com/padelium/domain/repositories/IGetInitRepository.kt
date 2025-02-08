package com.padelium.domain.repositories

import com.padelium.domain.dto.GetInitResponse
import retrofit2.Response

interface IGetInitRepository {

        suspend fun getinit (key: String): Response<GetInitResponse>
}