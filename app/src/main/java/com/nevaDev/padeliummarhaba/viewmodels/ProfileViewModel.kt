package com.nevaDev.padeliummarhaba.viewmodels

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.Resulta
import com.padelium.domain.dto.GetProfileResponse
import com.padelium.domain.dto.ProfileRequest
import com.padelium.domain.usecases.ProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.FileInputStream
import javax.inject.Inject



@HiltViewModel
class ProfileViewModel @Inject constructor(private val profileUseCase: ProfileUseCase): ViewModel(){


    val dataResult = MutableLiveData<DataResult>()


    /**
     * Start getting data
     */
    fun Profile(profileRequest: ProfileRequest) {
        dataResult.value=  DataResult.Loading
        viewModelScope.launch {
            dataResult.value=profileUseCase.Profile(profileRequest)
        }
    }
}





