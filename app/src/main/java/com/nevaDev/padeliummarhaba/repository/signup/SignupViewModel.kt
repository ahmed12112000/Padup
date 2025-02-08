package com.nevaDev.padeliummarhaba.repository.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nevaDev.padeliummarhaba.retrofit.RetrofitClient
import com.nevaDev.padeliummarhaba.retrofit.ServerApi
import kotlinx.coroutines.launch

class SignupViewModel : ViewModel() {
    private val userApi: ServerApi = RetrofitClient.serverApi

    fun signup(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        onResult: (Boolean, String) -> Unit
    ) {
        // Validate input fields
        if (email.isBlank() || password.isBlank() || firstName.isBlank() || lastName.isBlank()) {
            onResult(false, "Please fill all required fields.")
            return
        }

        // Additional validation (e.g., password strength, email format) can be added here.

        // Create the signup request
        val signupRequest = SignupRequest(email, password, firstName, lastName)

        // Proceed with the signup API call
        viewModelScope.launch {
            try {
                val response = userApi.signup(signupRequest)
                if (response.isSuccessful) {
                    onResult(true, "Signup successful!")
                } else {
                    onResult(false, "Signup failed: ${response.message()}")
                }
            } catch (e: Exception) {
                onResult(false, "An error occurred: ${e.message}")
            }
        }
    }
}







