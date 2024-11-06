package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dto.LoginRequest
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.SignupRequest
import com.padelium.domain.usecases.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userUseCase: UserUseCase):ViewModel(){


    val dataResult = MutableLiveData<DataResult>()


    /**
     * Start getting data
     */
    fun loginUser(loginRequest: LoginRequest) {
        dataResult.value=  DataResult.Loading
        viewModelScope.launch {
            dataResult.value=userUseCase.loginUser(loginRequest)
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