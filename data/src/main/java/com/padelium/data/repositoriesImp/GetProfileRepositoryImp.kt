package com.padelium.data.repositoriesImp

import com.padelium.data.datasource.remote.PadeliumEndPoint
import com.padelium.data.dto.GetProfileResponseDTO
import com.padelium.data.mappers.GetProfileMapper
import com.padelium.domain.dto.GetProfileResponse
import com.padelium.domain.repositories.IGetProfileRepository
import javax.inject.Inject


class GetProfileRepositoryImp @Inject constructor(
    private val api: PadeliumEndPoint,
    private val mapper: GetProfileMapper
) : IGetProfileRepository {

    override suspend fun GetProfile(): GetProfileResponse {
        val response = api.GetProfile()

        if (response.isSuccessful) {
            response.body()?.let { getProfileResponseList ->
                return mapper.GetProfileResponseDTOToGetProfileResponse(getProfileResponseList)
            } ?: throw Exception("Profile data is empty")
        } else {
            throw Exception("Error fetching Profile: ${response.errorBody()?.string()}")
        }
    }
}





