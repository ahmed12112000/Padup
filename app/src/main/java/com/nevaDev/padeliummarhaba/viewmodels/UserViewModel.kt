package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevaDev.padeliummarhaba.repository.LoginRequestDto
import com.padelium.domain.dto.LoginRequest
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.Resulta
import com.padelium.domain.dto.SignupRequest
import com.padelium.domain.usecases.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userUseCase: UserUseCase) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()
    val dataResult1 = MutableLiveData<Resulta>()

    /**
     * Start getting data
     */
    fun loginUser(loginRequest: LoginRequest) {
        viewModelScope.launch {
            dataResult1.postValue(Resulta.Loading)
            val result = userUseCase.loginUser(loginRequest)
            dataResult1.postValue(result)
        }
    }
    /**
     * Start signup
     */
    fun signupUser(signupRequest: SignupRequest) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            dataResult.value = userUseCase.signupUser(signupRequest)
        }
    }
}
