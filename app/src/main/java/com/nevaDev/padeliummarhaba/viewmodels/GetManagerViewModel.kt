package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.usecases.GetManagerUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetManagerViewModel @Inject constructor(
    private val getManagerUseCase: GetManagerUseCase
) : ViewModel() {

    private val _dataResult = MutableLiveData<DataResult>()
    val dataResult: LiveData<DataResult> = _dataResult

    // MutableLiveData to handle navigation events
    val navigationEvent = MutableLiveData<String>()

    /**
     * Fetch manager data for the given booking IDs.
     */
    fun GetManager(bookingIds: List<Long>) {
        _dataResult.value = DataResult.Loading
        viewModelScope.launch {
            val result = getManagerUseCase.GetManager(bookingIds)
            _dataResult.value = result

            when (result) {
                is DataResult.Success -> {
                    Log.d("GetManagerViewModel", "GetManager successful with no body expected.")
                }
                is DataResult.Failure -> {
                    Log.e(
                        "GetManagerViewModel",
                        "GetManager failed: Code = ${result.errorCode}, Message = ${result.errorMessage}"
                    )
                    // Check for errorCode and navigate to the error screen if needed
                    if (result.errorCode != 200) {
                        navigationEvent.value = "server_error_screen"
                    }
                }
                else -> {
                    Log.e("GetManagerViewModel", "GetManager encountered an unknown state")
                }
            }
        }
    }
}


