package com.padelium.domain.repositories

import okhttp3.RequestBody
import retrofit2.Response

interface IUpdatePhoneRepository {
    suspend fun UpdatePhone (Phone: RequestBody): Response<Unit>

}