package com.padelium.domain.repositories

import com.padelium.domain.dto.ExtrasRequest
import com.padelium.domain.dto.ExtrasResponse
import retrofit2.Response

interface IExtrasRepository {

        suspend fun Extras (extrasRequest: List<ExtrasRequest>): Response<List<ExtrasResponse>>


}