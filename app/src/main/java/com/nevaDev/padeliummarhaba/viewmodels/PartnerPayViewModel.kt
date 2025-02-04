package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PartnerPayResponse
import com.padelium.domain.usecases.GetProfileByIdUseCase
import com.padelium.domain.usecases.PartnerPayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PartnerPayViewModel @Inject constructor(
    private val partnerPayUseCase: PartnerPayUseCase,
    private val getProfileByIdUseCase: GetProfileByIdUseCase
) : ViewModel() {

    val dataResult = MutableLiveData<DataResult>()
    private val _selectedBookings = MutableLiveData<List<PartnerPayResponse>>(emptyList())

    val selectedBookings: LiveData<List<PartnerPayResponse>> get() = _selectedBookings
    /**
     * Start processing balance after fetching the profile
     */
    fun partnerPay(userId: Long) {
        Log.d("PartnerPayViewModel", "partnerPay called with User ID: $userId")
        dataResult.value = DataResult.Loading // Show loading state

        viewModelScope.launch {
            try {
                // Fetch user profile using GetProfileByIdUseCase
                val profileResponse = getProfileByIdUseCase.execute(userId)
                Log.d("GetProfileeeee", "Profile Response: $profileResponse")

                // Extract the first available partnerId from the profileResponse
                val partnerId = profileResponse.id
                Log.d("GettttttttttProfile", "Extracted Partner ID: $partnerId")

                if (partnerId != null) {
                    // Call PartnerPay with the extracted partnerId
                    val result = partnerPayUseCase.PartnerPay(partnerId)
                    // Handle the result of the network call
                    when (result) {
                        is DataResult.Success -> {
                            // Assuming response contains the necessary reservation details directly
                            val response = result.data // The successful response data
                            Log.d("PartnerPayViewModel", "PartnerPay Response: $response")

                            // Assuming the response contains properties directly like:
                            // bookingEstablishmentName, bookingDateStr, amount, etc.
                            if (response is PartnerPayResponse) {
                                // You can now map the response to a list of booking details or update the state
                                _selectedBookings.value = listOf(response) // Store the response in LiveData (assuming it's a single booking)
                                dataResult.value = DataResult.Success(response) // Set success state with response
                            } else {
                                dataResult.value = DataResult.Failure(
                                    null, null, "Invalid data structure"
                                )
                            }
                        }
                        is DataResult.Failure -> {
                            // Handle failure state, log the error message
                            Log.e("PartnerPayViewModel", "Error: ${result.errorMessage}")
                            dataResult.value = DataResult.Failure(
                                result.exception,
                                result.errorCode,
                                result.errorMessage
                            )
                        }
                        is DataResult.Loading -> {
                            // Loading state, you can handle this if needed
                            dataResult.value = DataResult.Loading
                        }
                    }
                } else {
                    dataResult.value = DataResult.Failure(
                        Exception("No valid partner ID found"),
                        null,
                        "No valid partner ID found"
                    )
                }
            } catch (ex: Exception) {
                Log.d("PartnerPayViewModel", "Error: ${ex.localizedMessage}")
                dataResult.value = DataResult.Failure(
                    ex,
                    null,
                    ex.localizedMessage ?: "An error occurred"
                )
            }
        }
    }

}

