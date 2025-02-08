// LoginViewModel.kt
package com.nevaDev.padeliummarhaba.repository.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.padelium.domain.dto.LoginRequest
import com.nevaDev.padeliummarhaba.retrofit.RetrofitClient
import com.nevaDev.padeliummarhaba.retrofit.ServerApi
import kotlinx.coroutines.launch

class LoginViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val serverApi: ServerApi by lazy {
        RetrofitClient.serverApi // Ensure this is properly initialized
    }

    val isUserLoggedIn: Boolean
        get() = userRepository.isUserLoggedIn

    fun login(username: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = serverApi.loginUser(LoginRequest(username, password))

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        // Save token or handle login response
                        userRepository.isUserLoggedIn = true // Update the login state
                        onResult(true, null)
                    } else {
                        onResult(false, "Login failed: No response body")
                    }
                } else {
                    onResult(false, "Error: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                onResult(false, "Error: ${e.message}")
            }
        }
    }

    fun logout() {
        userRepository.isUserLoggedIn = false
    }
}

