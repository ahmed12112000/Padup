package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.usecases.BalanceUseCase
import com.padelium.domain.usecases.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val balanceUseCase: BalanceUseCase,
    private val getProfileUseCase: GetProfileUseCase // Inject the GetProfileUseCase
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
                // Get user profile using GetProfileUseCase
                val profileResponse = getProfileUseCase.execute() // Use execute() from GetProfileUseCase
                Log.d("GetProfile", "Profile Response: $profileResponse")  // Log the profile response

                // Check if the profile was fetched successfully
                val userId = profileResponse.id

                // Fetch balance and update LiveData
                when (val result = balanceUseCase.Balance(userId)) {
                    is DataResult.Success -> {
                        val balance = result.data as? BigDecimal ?: BigDecimal.ZERO
                        Log.e("BalanceViewModel", "Fetched Balance: $balance")
                        dataResult.value = DataResult.Success(balance) // Ensure data type consistency
                    }
                    is DataResult.Failure -> {
                        Log.e("BalanceViewModel", "Error fetching balance: ${result.errorMessage}")
                        if (result.errorCode != 200) {
                            navigationEvent.value = "server_error_screen" // Expose navigation event to UI
                        } else {
                            dataResult.value = DataResult.Failure(result.exception, result.errorCode, result.errorMessage)
                        }
                    }
                    else -> {
                        Log.e("BalanceViewModel", "Unexpected result")
                        dataResult.value = DataResult.Failure(null, null, "Unexpected result")
                    }
                }
            } catch (ex: Exception) {
                dataResult.value = DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred")
            }
        }
    }
}




