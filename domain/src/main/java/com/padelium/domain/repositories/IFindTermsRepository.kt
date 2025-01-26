package com.padelium.domain.repositories

import com.padelium.domain.dto.FindTermsResponse
import okhttp3.RequestBody
import retrofit2.Response

interface IFindTermsRepository {
        suspend fun FindTerms (term: RequestBody): Response<List<FindTermsResponse>>
}