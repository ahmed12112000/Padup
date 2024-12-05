package com.padelium.domain.repositories

import com.padelium.domain.dto.GetInitResponse
import com.padelium.domain.dto.SearchListResponse
import retrofit2.Response

interface ISearchListRepository {

    suspend fun searchlist (key: String): Response<List<SearchListResponse>>


}
//       suspend fun GetBooking (key: String): Response<List<GetBookingResponse>>
