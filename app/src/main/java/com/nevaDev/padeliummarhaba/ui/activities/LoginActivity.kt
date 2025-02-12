package com.nevaDev.padeliummarhaba.ui.activities

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nevaDev.padeliummarhaba.ui.views.LoginScreen
import com.nevaDev.padeliummarhaba.ui.views.ProfileScreen
import com.nevaDev.padeliummarhaba.ui.views.SignUpScreen
import com.nevaDev.padeliummarhaba.viewmodels.BalanceViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SharedViewModel
import com.nevaDev.padeliummarhaba.viewmodels.UserViewModel
import com.padelium.domain.dto.LoginRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val viewModel: UserViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val getProfileViewModel: GetProfileViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Restore isLoggedIn state from savedInstanceState
        val isLoggedIn = savedInstanceState?.getBoolean("isLoggedIn") ?: false
        sharedViewModel.setLoggedIn(isLoggedIn)

        setContent {
            val navController = rememberNavController()
            val isLoggedInState by sharedViewModel.isLoggedIn.observeAsState(false)
            val lastRequestedRoute by sharedViewModel.lastRequestedRoute.observeAsState()

            // If the user is logged in, navigate directly to the profile screen or last requested route
            LaunchedEffect(isLoggedInState) {
                if (isLoggedInState) {
                    if (lastRequestedRoute == "Profile_screen") {
                        navController.navigate("Profile_screen") {
                            popUpTo("login_screen") { inclusive = true } // Clear the back stack
                        }
                    } else {
                        // Default navigation to Profile_screen
                        navController.navigate("Profile_screen") {
                            popUpTo("login_screen") { inclusive = true } // Clear the back stack
                        }
                    }
                    finish() // Close LoginActivity
                }
            }

            NavHost(navController = navController, startDestination = "login_screen") {
                composable("login_screen") {
                    LoginScreen(
                        onLoginSuccess = {
                            sharedViewModel.setLoggedIn(true) // Update login state
                            val lastRequestedRoute = sharedViewModel.lastRequestedRoute.value
                            if (lastRequestedRoute == "Profile_screen") {
                                navController.navigate("Profile_screen") {
                                    popUpTo("login_screen") { inclusive = true } // Clear the back stack
                                }
                            } else {
                                // Default navigation to Profile_screen
                                navController.navigate("Profile_screen") {
                                    popUpTo("login_screen") { inclusive = true } // Clear the back stack
                                }
                            }
                            finish() // Close LoginActivity
                        },
                        viewModel = viewModel,
                        getProfileViewModel = getProfileViewModel,
                        navController = navController,
                        loginRequest = LoginRequest(username = "", password = "")
                    )
                }
                composable("signup_screen") {
                    SignUpScreen(
                        navController = navController,
                        onSignupSuccess = {
                            // Handle signup success if needed
                        },
                        viewModel = hiltViewModel()
                    )
                }
                composable("Profile_screen") {
                    ProfileScreen()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save isLoggedIn state
        outState.putBoolean("isLoggedIn", sharedViewModel.isLoggedIn.value ?: false)
    }
}



