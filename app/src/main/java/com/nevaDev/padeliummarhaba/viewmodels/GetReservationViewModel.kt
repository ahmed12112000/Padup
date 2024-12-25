package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.nevaDev.padeliummarhaba.ui.views.Reservation
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.GetReservationResponse
import com.padelium.domain.repositories.IGetReservationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class GetReservationViewModel @Inject constructor(private val repository: IGetReservationRepository) : ViewModel() {

    private val _ReservationsData =
        MutableLiveData<DataResultBooking<List<GetReservationResponse>>>()
    val ReservationsData: LiveData<DataResultBooking<List<GetReservationResponse>>> get() = _ReservationsData

    fun GetReservation() {
        _ReservationsData.postValue(DataResultBooking.Loading)

        viewModelScope.launch {
            try {
                val response = repository.GetReservation()
                Log.d("GetReservation", "Fetched Reservations: $response")

                if (response.isNotEmpty()) {
                    _ReservationsData.postValue(DataResultBooking.Success(response))
                } else {
                    _ReservationsData.postValue(
                        DataResultBooking.Failure(
                            exception = null,
                            errorCode = null,
                            errorMessage = "No reservations found."
                        )
                    )
                }
            } catch (e: Exception) {
                _ReservationsData.postValue(
                    DataResultBooking.Failure(
                        exception = e,
                        errorCode = null,
                        errorMessage = "Exception occurred: ${e.message}"
                    )
                )
                Log.e("GetReservation", "Error fetching reservations", e)
            }
        }
    }
}






