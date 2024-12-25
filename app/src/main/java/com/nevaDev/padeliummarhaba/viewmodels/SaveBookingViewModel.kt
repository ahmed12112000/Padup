package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.padelium.data.datasource.remote.PadeliumApi
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.SaveBookingRequest
import com.padelium.domain.usecases.SaveBookingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SaveBookingViewModel @Inject constructor(
    private val saveBookingUseCase: SaveBookingUseCase // Use the SaveBookingUseCase instead of calling API directly
) : ViewModel() {

    private val _dataResult = MutableLiveData<DataResult>()
    val dataResult: LiveData<DataResult> = _dataResult

    fun SaveBooking(getBookingResponses: List<GetBookingResponse>) {
        viewModelScope.launch {
            _dataResult.value = DataResult.Loading // Start with loading state

            try {
                // Call the UseCase to save the booking
                val result = saveBookingUseCase.SaveBooking(getBookingResponses)

                _dataResult.value = result // Pass the result from the UseCase to LiveData

                if (result is DataResult.Success) {
                    Log.d("SaveBookingViewModel", "Booking saved successfully")
                } else if (result is DataResult.Failure) {
                    Log.d("SaveBookingViewModel", "Error saving booking: ${result.errorMessage}")
                }
            } catch (e: Exception) {
                // Handle any exception that occurs during the process
                _dataResult.value = DataResult.Failure(
                    exception = e,
                    errorCode = null,
                    errorMessage = e.message ?: "Unknown error"
                )
                Log.e("SaveBookingViewModel", "Exception: ${e.message}")
            }
        }
    }
}











