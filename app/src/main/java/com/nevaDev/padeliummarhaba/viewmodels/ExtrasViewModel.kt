package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.ExtrasRequest
import com.padelium.domain.usecases.ExtrasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ExtrasViewModel @Inject constructor(private val extrasUseCase: ExtrasUseCase): ViewModel() {


    val dataResult = MutableLiveData<DataResult>()


    /**
     * Start getting data
     */
    fun Extras(extrasRequest: List<ExtrasRequest>) {
        dataResult.value = DataResult.Loading
        viewModelScope.launch {
            dataResult.value = extrasUseCase.Extras(extrasRequest)
        }
    }
}


