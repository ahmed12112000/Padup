package com.padelium.data.repositoriesImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.mappers.ProfileMapper
import com.padelium.domain.dto.ProfileRequest
import com.padelium.domain.repositories.IProfileRepository
import retrofit2.Response
import javax.inject.Inject


class ProfileRepositoryImp @Inject constructor(
    private val api: PadeliumApi,
    private val mapper: ProfileMapper,

    ) : IProfileRepository {
    override suspend fun Profile(profileRequest: ProfileRequest): Response<Void> {
        return api.Profile(mapper.ProfileRequestToProfileRequestDto(profileRequest))
    }
}