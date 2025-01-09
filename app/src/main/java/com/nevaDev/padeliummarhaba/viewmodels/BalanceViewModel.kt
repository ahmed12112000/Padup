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
import javax.inject.Inject


@HiltViewModel
class BalanceViewModel @Inject constructor(
    private val balanceUseCase: BalanceUseCase,
    private val getProfileUseCase: GetProfileUseCase // Inject the GetProfileUseCase
) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()

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
                // Now call Balance with the fetched id
                balanceUseCase.Balance(userId).let {
                    dataResult.value = it
                }
            } catch (ex: Exception) {
                dataResult.value = DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred")
            }
        }
    }
}



