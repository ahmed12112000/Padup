package com.nevaDev.padeliummarhaba.viewmodels

import android.app.Application
import android.content.SharedPreferences
import android.service.autofill.UserData
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevaDev.padeliummarhaba.repository.LoginRequestDto
import com.nevaDev.padeliummarhaba.ui.auth.UserPreferences
import com.padelium.domain.dto.LoginRequest
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.Resulta
import com.padelium.domain.dto.SignupRequest
import com.padelium.domain.dto.logoutRequest
import com.padelium.domain.usecases.UserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

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
     * Start InitBooking
     */

    /*
     fun InitBooking(initBookingRequest: InitBookingRequest) {
        viewModelScope.launch {
            dataResult1.postValue(Resulta.Loading)
            val result = initBookingUseCase.InitBooking(initBookingRequest)
            dataResult1.postValue(result)
        }
    }
     */







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




@HiltViewModel
class LogoutViewModel @Inject constructor(
    private val userUseCase: UserUseCase,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    // StateFlow to hold user data
    private val _userData = MutableStateFlow<UserData?>(null)
    val userData: StateFlow<UserData?> get() = _userData

    // StateFlow to track logout state
    private val _logoutState = MutableStateFlow<DataResult>(DataResult.Loading)
    val logoutState: StateFlow<DataResult> get() = _logoutState

    fun logoutUser(logoutRequest: logoutRequest) {
        viewModelScope.launch {
            _logoutState.value = DataResult.Loading
            try {
                // Call the logout API or use case
                val result = userUseCase.logoutUser(logoutRequest)

                // Check if the logout was successful
                if (result is DataResult.Success) {
                    // Clear SharedPreferences
                    sharedPreferences.edit().clear().apply()

                    // Reset user data in ViewModel
                    _userData.value = null

                    // Update logout state to success
                    _logoutState.value = DataResult.Success(Unit)
                } else {
                    // Handle failure
                    _logoutState.value = result
                }
            } catch (e: Exception) {
                // Handle exceptions
                _logoutState.value = DataResult.Failure(
                    exception = e,
                    errorCode = null,
                    errorMessage = "Logout failed"
                )
            }
        }
    }
}