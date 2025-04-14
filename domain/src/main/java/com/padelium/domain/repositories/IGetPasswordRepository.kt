package com.padelium.domain.repositories

import retrofit2.Response


interface IGetPasswordRepository {
       suspend fun GetPassword (email: String): Response<Boolean>
}


interface IResetPasswordRepository {
       suspend fun ResetPassword (email: String): Response<Void>
}

