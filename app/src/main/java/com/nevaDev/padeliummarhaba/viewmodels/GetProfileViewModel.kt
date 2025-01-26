package com.nevaDev.padeliummarhaba.viewmodels

import android.util.Log
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

    // LiveData for first name, last name, and image URL
    private val _firstName = MutableLiveData<String>()
    val firstName: LiveData<String> get() = _firstName

    private val _lastName = MutableLiveData<String>()
    val lastName: LiveData<String> get() = _lastName

    private val _image = MutableLiveData<String>()
    val image: LiveData<String> get() = _image

    fun GetProfile() {
        _profileData.postValue(DataResult.Loading)

        viewModelScope.launch {
            try {
                val profile = repository.GetProfile()
                Log.d("GetProfile", "profile: $profile")  // Log the response

                // Assuming profile is of type GetProfileResponseDTO
                _profileData.postValue(DataResult.Success(profile))

                // Update first name, last name, and image URL LiveData
                _firstName.postValue(profile.firstName)
                _lastName.postValue(profile.lastName)
                _image.postValue(profile.image)

            } catch (e: Exception) {
                _profileData.postValue(
                    DataResult.Failure(
                        e,
                        null,
                        "Exception occurred: ${e.message}"
                    )
                )
            }
        }
    }



fun fetchProfileData() {
    _profileData.postValue(DataResult.Loading)

    viewModelScope.launch {
        try {
            // Call the repository method which returns GetProfileResponse directly
            val profile = repository.GetProfile()
            // Post success with the fetched profile data
            _profileData.postValue(DataResult.Success(profile))
        } catch (e: Exception) {
            // Handle exceptions during the fetch operation
            _profileData.postValue(DataResult.Failure(e, null, "Exception occurred: ${e.message}"))
        }
    }
}
}
