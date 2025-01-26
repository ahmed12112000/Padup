package com.padelium.data.repositoriesImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.domain.dto.BalanceResponse
import com.padelium.domain.dto.UserAvoirPayResponse
import com.padelium.domain.repositories.IBalanceRepository
import retrofit2.Response
import javax.inject.Inject


class BalanceRepositoryImp @Inject constructor(
    private val api: PadeliumApi
) : IBalanceRepository {

    override suspend fun Balance(Id: Long): Response<BalanceResponse> {
        // Call the Balance endpoint of your API with the provided Id
        return api.Balance(Id)
    }
}
