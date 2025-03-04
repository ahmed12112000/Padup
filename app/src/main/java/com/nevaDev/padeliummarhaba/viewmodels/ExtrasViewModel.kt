package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.usecases.ExtrasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExtrasViewModel @Inject constructor(private val extrasUseCase: ExtrasUseCase) : ViewModel() {

    private val _extrasState = MutableLiveData<DataResult>()
    val extrasState: LiveData<DataResult> get() = _extrasState

    // Add a MutableLiveData to handle navigation events
    val navigationEvent = MutableLiveData<String>()

    fun Extras() {
        viewModelScope.launch {
            _extrasState.postValue(DataResult.Loading) // Emit loading state

            // Fetch data from the use case
            val result = extrasUseCase.Extras()

            // Emit the result (either success or failure)
            _extrasState.postValue(result)

            // Check if the errorCode is not 200 and navigate to the error screen
            when (result) {
                is DataResult.Failure -> {
                    if (result.errorCode != 200) {
                        navigationEvent.value = "server_error_screen"
                    }
                }
                else -> {
                    // No action required for other cases
                }
            }
        }
    }
}
