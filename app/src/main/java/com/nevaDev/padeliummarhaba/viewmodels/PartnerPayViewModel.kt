package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.DataResultBooking
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


    private val _data = MutableLiveData<List<PartnerPayResponse>>()
    val data: LiveData<List<PartnerPayResponse>> = _data

    // Extracted response LiveData
    private val _partnerPayResponse = MutableLiveData<PartnerPayResponse>()
    val partnerPayResponse: LiveData<PartnerPayResponse> get() = _partnerPayResponse
    // Store response as LiveData
    private val _storedPartnerPayResponse = MutableLiveData<PartnerPayResponse?>()
    val storedPartnerPayResponse: LiveData<PartnerPayResponse?> get() = _storedPartnerPayResponse


    fun partnerPay(userId: Long) {
        Log.d("PartnerPayViewModel", "partnerPay called with User ID: $userId")
        dataResult.value = DataResult.Loading

        viewModelScope.launch {
            try {
                val profileResponse = getProfileByIdUseCase.execute(userId)
                val partnerId = profileResponse.id
                Log.d("GetProfile", "Extracted Partner ID: $partnerId")

                if (partnerId != null) {
                    val result = partnerPayUseCase.PartnerPay(partnerId)
                    when (result) {
                        is DataResult.Success -> {
                            val response = result.data
                            Log.d("PartnerPayViewModel", "PartnerPay Response: $response")

                            if (response is PartnerPayResponse) {
                                _partnerPayResponse.postValue(response)

                                // Store response using LiveData

                                dataResult.postValue(DataResult.Success(response))
                            } else {
                                Log.e("PartnerPayViewModel", "Unexpected data structure")
                                dataResult.postValue(DataResult.Failure(null, null, "Invalid data structure"))
                            }
                        }
                        is DataResult.Failure -> {
                            Log.e("PartnerPayViewModel", "Error: ${result.errorMessage}")
                            dataResult.postValue(DataResult.Failure(result.exception, result.errorCode, result.errorMessage))
                        }
                        is DataResult.Loading -> dataResult.postValue(DataResult.Loading)
                    }
                } else {
                    dataResult.postValue(DataResult.Failure(Exception("No valid partner ID found"), null, "No valid partner ID found"))
                }
            } catch (ex: Exception) {
                Log.e("PartnerPayViewModel", "Error: ${ex.localizedMessage}")
                dataResult.postValue(DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred"))
            }
        }
    }

    // Function to get stored response
    // Function to get stored response
    fun getStoredPartnerPayResponse(): PartnerPayResponse? {
        return _storedPartnerPayResponse.value
    }

}



