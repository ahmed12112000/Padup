package com.padelium.data.repositoriesImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.domain.repositories.IGetManagerRepository
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject


class GetManagerRepositoryImp @Inject constructor(
    private val api: PadeliumApi
) : IGetManagerRepository {

    override suspend fun GetManager(bookingIds:List<Long>): Response<Unit> {
        return api.GetManager(bookingIds)
    }
}
