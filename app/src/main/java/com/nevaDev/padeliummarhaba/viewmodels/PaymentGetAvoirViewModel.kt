package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.DataResult2
import com.padelium.domain.dto.PaymentGetAvoirRequest
import com.padelium.domain.usecases.PaymentGetAvoirUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class PaymentGetAvoirViewModel @Inject constructor(
    private val paymentGetAvoirUseCase: PaymentGetAvoirUseCase
) : ViewModel() {

    val paymentStatus = MutableLiveData<Boolean>()


    suspend fun PaymentGetAvoir(paymentGetAvoirRequest: PaymentGetAvoirRequest, navController: NavController) : Boolean {
        return try {
            val result = paymentGetAvoirUseCase.PaymentGetAvoir(paymentGetAvoirRequest, navController)
            when (result) {
                is DataResult2.Success -> result.data // Return true or false
                else -> false // Return false on failure
            }
        } catch (e: Exception) {
            false // Return false if an exception occurs
        }
    }
}



