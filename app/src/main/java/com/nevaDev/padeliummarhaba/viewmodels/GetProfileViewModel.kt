package com.nevaDev.padeliummarhaba.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IGetProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class GetProfileViewModel @Inject constructor(private val repository: IGetProfileRepository) : ViewModel() {

    private val _profileData = MutableLiveData<DataResult>()
    val profileData: LiveData<DataResult> get() = _profileData

    private val _firstName = MutableLiveData<String>()
    val firstName: LiveData<String> get() = _firstName

    private val _lastName = MutableLiveData<String>()
    val lastName: LiveData<String> get() = _lastName

    private val _image = MutableLiveData<String>()
    val image: LiveData<String> get() = _image

    private val _hasUserRole = MutableLiveData<Boolean>()

    private val _activated = MutableLiveData<Boolean>()
    val activated: LiveData<Boolean> get() = _activated

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> get() = _email

    private val _authorities = MutableLiveData<Boolean>()
    val authorities: LiveData<Boolean> get() = _authorities

    private val _login = MutableLiveData<String>()
    val login: LiveData<String> get() = _login

    private val _langkey = MutableLiveData<String>()
    val langkey: LiveData<String> get() = _langkey

    fun fetchProfileData() {
        _profileData.postValue(DataResult.Loading)

        viewModelScope.launch {
            try {
                val profile = repository.GetProfile()

                _profileData.postValue(DataResult.Success(profile))

                _firstName.postValue(profile.firstName)
                _lastName.postValue(profile.lastName)
                _image.postValue(profile.image)

                _hasUserRole.postValue(profile.authorities.contains("ROLE_USER"))

            } catch (e: Exception) {
                _profileData.postValue(
                    DataResult.Failure(e, null, "Exception occurred: ${e.message}"
                    )
                )
            }
        }
    }
}

