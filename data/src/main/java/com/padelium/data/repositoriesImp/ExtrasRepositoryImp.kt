package com.padelium.data.repositoriesImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.mappers.ExtrasMapper
import com.padelium.domain.dto.ExtrasRequest
import com.padelium.domain.dto.ExtrasResponse
import com.padelium.domain.repositories.IExtrasRepository
import retrofit2.Response
import javax.inject.Inject


class ExtrasRepositoryImp @Inject constructor(
    private val api: PadeliumApi,
    private val mapper: ExtrasMapper,

    ) : IExtrasRepository {
    override suspend fun Extras (extrasRequest: List<ExtrasRequest>): Response<List<ExtrasResponse>> {
        return api.Extras(mapper.ExtrasRequestToExtrasResponseDTO(extrasRequest))
    }


}
