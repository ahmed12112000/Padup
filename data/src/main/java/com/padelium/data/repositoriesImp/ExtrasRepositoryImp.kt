package com.padelium.data.repositoriesImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.datasource.remote.PadeliumEndPoint
import com.padelium.data.mappers.ExtrasMapper
import com.padelium.domain.dto.Extra
import com.padelium.domain.dto.ExtrasRequest
import com.padelium.domain.dto.ExtrasResponse
import com.padelium.domain.repositories.IExtrasRepository
import com.padelium.domain.repositories.IGetPacksRepository
import retrofit2.Call
import retrofit2.Response
import java.math.BigDecimal
import javax.inject.Inject


class ExtrasRepositoryImp @Inject constructor(
    private val api: PadeliumEndPoint,
    private val mapper: ExtrasMapper
) : IExtrasRepository {

    override suspend fun Extras(): List<ExtrasResponse> {
        // Call API to get the packs
        val response = api.Extras()

        // Check if the response is successful
        if (response.isSuccessful) {
            response.body()?.let { extrasResponse ->
                return mapper.ExtrasRequestToExtrasResponseDTO(extrasResponse)
            } ?: throw Exception("Extras data is empty")
        } else {
            throw Exception("Error fetching Extras: ${response.errorBody()?.string()}")
        }
    }
}




