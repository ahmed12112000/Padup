package com.padelium.data.repositoriesImp


import com.padelium.domain.dto.LoginRequest
import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.data.dto.FetchKeyResponseDTO
import com.padelium.data.mappers.KeyMapper
import com.padelium.data.mappers.UserMapper
import com.padelium.domain.dto.FetchKeyRequest
import com.padelium.domain.dto.FetchKeyResponse
import com.padelium.domain.dto.SignupRequest
import com.padelium.domain.repositories.IUserRepository
import retrofit2.Response
import javax.inject.Inject

 class UserRepositoryImp @Inject constructor(
    private val api: PadeliumApi,
    private val mapper: UserMapper,

) : IUserRepository {
    override suspend fun loginUser(loginRequest: LoginRequest): Response<Void> {
        return api.login(mapper.loginRequestToLoginRequestDto(loginRequest))
    }
    override suspend fun signupUser(signupRequest: SignupRequest): Response<Void> {
        return api.signup(mapper.signupRequestToSignupRequestDto(signupRequest))
    }

}
