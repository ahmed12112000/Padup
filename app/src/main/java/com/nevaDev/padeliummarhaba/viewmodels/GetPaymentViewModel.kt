package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.DataResult2
import com.padelium.domain.dto.GetPaymentRequest
import com.padelium.domain.usecases.GetPaymentUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class GetPaymentViewModel @Inject constructor(
    private val getPaymentUseCase: GetPaymentUseCase
) : ViewModel() {

    suspend fun GetPayment2(getPaymentRequest: GetPaymentRequest, navController: NavController): Boolean {
        return try {
            val result = getPaymentUseCase.GetPayment(getPaymentRequest, navController)
            when (result) {
                is DataResult2.Success -> result.data // Return true or false
                else -> false // Return false on failure
            }
        } catch (e: Exception) {
            false // Return false if an exception occurs
        }
    }
}
