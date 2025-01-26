package com.padelium.domain.repositories

import okhttp3.ResponseBody
import retrofit2.Response

interface IGetManagerRepository{

    suspend fun GetManager (bookingIds:List<Long>): Response<Unit>
}