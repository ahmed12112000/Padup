package com.padelium.data.repositoriesImp.RepositoryImp

import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.domain.repositories.IGetPasswordRepository
import com.padelium.domain.repositories.IResetPasswordRepository
import retrofit2.Response
import javax.inject.Inject

class GetPasswordRepositoryImp @Inject constructor(
    private val api: PadeliumApi
) : IGetPasswordRepository {
    override suspend fun GetPassword(email: String): Response<Boolean> {
        return api.GetPassword(email)
    }
}


class ResetPasswordRepositoryImp @Inject constructor(
    private val api: PadeliumApi
) : IResetPasswordRepository {
    override suspend fun ResetPassword(email: String): Response<Void> {
        return api.ResetPassword(email)
    }
}


