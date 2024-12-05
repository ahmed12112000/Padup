package com.padelium.data.repositoriesImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.mappers.SaveBookingMapper
import com.padelium.domain.dto.SaveBookingRequest
import com.padelium.domain.dto.SaveBookingResponse
import com.padelium.domain.repositories.ISaveBookingRepository
import retrofit2.Response
import javax.inject.Inject


class SaveBookingRepositoryImp @Inject constructor(
    private val api: PadeliumApi,
    private val mapper: SaveBookingMapper,

    ) : ISaveBookingRepository {

    override suspend fun SaveBooking(saveBookingRequest: List<SaveBookingRequest>): Response<List<SaveBookingResponse>> {
        val requestDTOList = saveBookingRequest.map { mapper.SaveBookingRequestToSaveBookingRequestDTO(it) }

        return api.SaveBooking(requestDTOList)
    }
}


