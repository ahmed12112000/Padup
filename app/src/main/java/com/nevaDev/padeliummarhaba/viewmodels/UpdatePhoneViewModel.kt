package com.nevaDev.padeliummarhaba.viewmodels


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.usecases.UpdatePhoneUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.RequestBody
import javax.inject.Inject


@HiltViewModel
class UpdatePhoneViewModel @Inject constructor(
    private val updatePhoneUseCase: UpdatePhoneUseCase
) : ViewModel() {

    private val _dataResult = MutableLiveData<DataResult>()
    val dataResult: LiveData<DataResult> = _dataResult


    fun UpdatePhone(Phone: RequestBody) {
        _dataResult.value = DataResult.Loading
        viewModelScope.launch {
            val result = updatePhoneUseCase.UpdatePhone(Phone)
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
                }
                else -> {
                    Log.e("GetManagerViewModel", "GetManager encountered an unknown state")
                }
            }
        }
    }
}


