package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IGetPacksRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel

class GetPacksViewModel @Inject constructor(private val repository: IGetPacksRepository) : ViewModel() {

    private val _packsData = MutableLiveData<DataResult>()
    val packsData: LiveData<DataResult> get() = _packsData

    fun GetPacks() {
        _packsData.postValue(DataResult.Loading)

        viewModelScope.launch {
            try {
                val packs = repository.GetPacks()
                Log.d("GetPacks", "Packs: $packs")  // Log the response

                _packsData.postValue(DataResult.Success(packs))
            } catch (e: Exception) {
                _packsData.postValue(
                    DataResult.Failure(
                        e,
                        null,
                        "Exception occurred: ${e.message}"
                    )
                )
            }
        }
    }
}
