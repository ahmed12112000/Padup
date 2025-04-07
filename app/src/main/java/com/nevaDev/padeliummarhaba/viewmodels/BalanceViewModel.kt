package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.usecases.BalanceUseCase
import com.padelium.domain.usecases.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val balanceUseCase: BalanceUseCase,
    private val getProfileUseCase: GetProfileUseCase // Injected use case
) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()
    val navigationEvent = MutableLiveData<String>() // Used for navigation event

    /**
     * Start processing balance after fetching the profile
     */
    fun fetchAndBalance() {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            try {
                // Call use case and handle result
                when (val profileResult = getProfileUseCase.execute()) {
                    is DataResultBooking.Success -> {
                        val profile = profileResult.data
                        Log.d("GetProfile", "Profile Response: $profile")

                        val userId = profile.id

                        // Fetch balance using userId
                        when (val result = balanceUseCase.Balance(userId)) {
                            is DataResult.Success -> {
                                val balance = result.data as? BigDecimal ?: BigDecimal.ZERO
                                Log.e("BalanceViewModel", "Fetched Balance: $balance")
                                dataResult.value = DataResult.Success(balance)
                            }

                            is DataResult.Failure -> {
                                Log.e("BalanceViewModel", "Error fetching balance: ${result.errorMessage}")
                                if (result.errorCode != 200) {
                                    navigationEvent.value = "server_error_screen"
                                } else {
                                    dataResult.value = DataResult.Failure(
                                        result.exception,
                                        result.errorCode,
                                        result.errorMessage
                                    )
                                }
                            }

                            else -> {
                                Log.e("BalanceViewModel", "Unexpected result")
                                dataResult.value = DataResult.Failure(null, null, "Unexpected result")
                            }
                        }
                    }

                    is DataResultBooking.Failure -> {
                        Log.e("GetProfile", "Failed to fetch profile: ${profileResult.errorMessage}")
                        navigationEvent.value = "server_error_screen"
                    }

                    DataResultBooking.Loading -> TODO()
                }
            } catch (ex: Exception) {
                dataResult.value = DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred")
            }
        }
    }
}




