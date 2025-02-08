package com.padelium.data.repositoriesImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.domain.dto.SearchListResponse
import com.padelium.domain.repositories.ISearchListRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import javax.inject.Inject

class SearchListRepositoryImp @Inject constructor(
    private val api: PadeliumApi
) : ISearchListRepository {

    override suspend fun searchlist(key: String): Response<List<SearchListResponse>> {
        return api.SearchList(key)

    }
}

