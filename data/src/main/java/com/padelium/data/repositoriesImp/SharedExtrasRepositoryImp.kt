package com.padelium.data.repositoriesImp

import com.padelium.data.datasource.remote.PadeliumEndPoint
import com.padelium.data.mappers.SharedExtrasMapper
import com.padelium.domain.dto.SharedExtrasResponse
import com.padelium.domain.repositories.ISharedExtrasRepository
import javax.inject.Inject


class SharedExtrasRepositoryImp @Inject constructor(
    private val api: PadeliumEndPoint,
    private val mapper: SharedExtrasMapper
) : ISharedExtrasRepository {

    override suspend fun SharedExtras(): List<SharedExtrasResponse> {
        // Call API to get the packs
        val response = api.SharedExtras()

        // Check if the response is successful
        if (response.isSuccessful) {
            response.body()?.let { sharedextrasResponse ->
                return mapper.SharedExtrasRequestToSharedExtrasResponseDTO(sharedextrasResponse)
            } ?: throw Exception("Extras data is empty")
        } else {
            throw Exception("Error fetching Extras: ${response.errorBody()?.string()}")
        }
    }
}

