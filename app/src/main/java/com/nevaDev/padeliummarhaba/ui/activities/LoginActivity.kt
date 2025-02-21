package com.nevaDev.padeliummarhaba.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.compiler.plugins.kotlin.ComposeCallableIds.remember
import androidx.compose.compiler.plugins.kotlin.ComposeFqNames.remember
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nevaDev.padeliummarhaba.di.SessionManager
import com.nevaDev.padeliummarhaba.models.ReservationOption
import com.nevaDev.padeliummarhaba.ui.auth.UserPreferences
import com.nevaDev.padeliummarhaba.ui.views.CreditCharge
import com.nevaDev.padeliummarhaba.ui.views.CreditPayment
import com.nevaDev.padeliummarhaba.ui.views.LoginScreen
import com.nevaDev.padeliummarhaba.ui.views.PaymentSection1
import com.nevaDev.padeliummarhaba.ui.views.ProfileScreen
import com.nevaDev.padeliummarhaba.ui.views.SignUpScreen
import com.nevaDev.padeliummarhaba.ui.views.SummaryScreen
import com.nevaDev.padeliummarhaba.viewmodels.BalanceViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetReservationViewModel
import com.nevaDev.padeliummarhaba.viewmodels.UserViewModel
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.LoginRequest
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject



@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    private val viewModel: UserViewModel by viewModels()
    private val getProfileViewModel: GetProfileViewModel by viewModels()
    private val sessionManager by lazy { SessionManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val destinationRoute = intent.getStringExtra("destination_route") ?: "main_screen"

        setContent {
            val navController = rememberNavController()
            var loginSuccess by remember { mutableStateOf(false) }

            val isLoggedInState by rememberUpdatedState(sessionManager.isLoggedIn())
            Log.d("LoginActivity", "isLoggedInState: $isLoggedInState, destinationRoute: $destinationRoute")

            val getReservationViewModel: GetReservationViewModel = hiltViewModel()

            val navigateToMainActivity: () -> Unit = {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP // This will clear any activities on top of MainActivity
                startActivity(intent)
                finish() // Close LoginActivity
            }

            LaunchedEffect(isLoggedInState) {
                Log.d("LoginActivity", "LaunchedEffect triggered with isLoggedInState: $isLoggedInState")

                if (isLoggedInState) {
                    if (destinationRoute == "main_screen") {
                        navigateToMainActivity()
                    } else {
                        navController.navigate(destinationRoute) {
                            popUpTo("login_screen") { inclusive = true }
                        }
                        val intent = Intent().apply {
                            putExtra("navigate_back_to", destinationRoute)
                        }
                        setResult(Activity.RESULT_OK, intent)
                        finish()
                    }
                } else {
                    Log.d("LoginActivity", "Not logged in, showing LoginScreen")
                    navController.popBackStack("login_screen", inclusive = false) // Clear back stack
                    navController.navigate("login_screen") { // Force navigation to login screen
                        popUpTo("login_screen") { inclusive = true } // Clear any previous instance
                    }
                }
            }

            Scaffold(
                bottomBar = {
                    AnimatedBottomBar(
                        navController = navController,
                        getReservationViewModel = getReservationViewModel,
                        navigateToLogin = { route ->
                            Log.d("LoginActivity", "Navigating to Login screen with route: $route")
                            navController.navigate(route) {
                                popUpTo("login_screen") { inclusive = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                },
                content = { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        NavHost(
                            navController = navController,
                            startDestination = "login_screen"
                        ) {
                            composable("login_screen") {
                                LoginScreen(
                                    onLoginSuccess = {
                                        Log.d("LoginActivity", "Login success")

                                        // Save the authentication token inside LoginScreen itself
                                        val token = sessionManager.getAuthToken()
                                        if (token != null) {
                                            sessionManager.saveAuthToken(token)
                                        }

                                        // Update session activity time
                                        sessionManager.updateLastActiveTime()

                                        // Update the state to trigger navigation
                                        loginSuccess = true

                                        // Navigate to the appropriate screen
                                        if (destinationRoute == "main_screen") {
                                            navigateToMainActivity()
                                        } else {
                                            navController.navigate(destinationRoute) {
                                                popUpTo("login_screen") { inclusive = true }
                                            }
                                            val intent = Intent().apply {
                                                putExtra("navigate_back_to", destinationRoute)
                                            }
                                            setResult(Activity.RESULT_OK, intent)
                                            finish()
                                        }
                                    },
                                    viewModel = viewModel,
                                    getProfileViewModel = getProfileViewModel,
                                    navController = navController,
                                    loginRequest = LoginRequest(username = "", password = ""),
                                    destinationRoute = destinationRoute
                                )
                                LaunchedEffect(loginSuccess) {
                                    if (loginSuccess) {
                                        delay(200) // Small delay to allow session update
                                        navigateToMainActivity()
                                    }
                                }
                            }
                            composable("signup_screen") {
                                SignUpScreen(
                                    navController = navController,
                                    onSignupSuccess = {},
                                    viewModel = hiltViewModel()
                                )
                            }
                            composable("Profile_screen") {
                                ProfileScreen(navController = navController,)
                            }
                            composable("CreditCharge") {
                                CreditCharge(navController = navController)
                            }
                            composable("CreditPayment") {
                                CreditPayment(navController = navController)
                            }
                            composable("summary_screen") {
                                SummaryScreen(navController = navController)
                            }
                            composable("main_screen") {
                                navigateToMainActivity()
                            }
                            composable(
                                "payment_section1/{name}/{time}/{price}/{mappedBookings}",
                                deepLinks = listOf(
                                    navDeepLink {
                                        uriPattern = "android-app://androidx.navigation/payment_section1/{name}/{time}/{price}/{mappedBookings}"
                                    }
                                ),
                                arguments = listOf(
                                    navArgument("name") { type = NavType.StringType },
                                    navArgument("time") { type = NavType.StringType },
                                    navArgument("price") { type = NavType.StringType },
                                    navArgument("mappedBookings") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->
                                val name = backStackEntry.arguments?.getString("name").orEmpty()
                                val time = backStackEntry.arguments?.getString("time").orEmpty()
                                val price = backStackEntry.arguments?.getString("price").orEmpty()
                                val mappedBookingsJson = backStackEntry.arguments?.getString("mappedBookings").orEmpty()

                                val type = object : TypeToken<List<GetBookingResponse>>() {}.type
                                val mappedBookings: List<GetBookingResponse> = Gson().fromJson(mappedBookingsJson, type)

                                PaymentSection1(
                                    selectedDate = LocalDate.now(),
                                    selectedReservation = ReservationOption(name, time, price, mappedBookingsJson),
                                    onExtrasUpdate = { _, _, _ -> },
                                    navController = navController,
                                    bookingViewModel = hiltViewModel(),
                                    selectedTimeSlot = time,
                                    mappedBookingsJson = mappedBookingsJson,
                                    price = price,
                                    onTotalAmountCalculated = { totalAmount, currency -> },
                                    viewModel9 = hiltViewModel()
                                )
                            }

                        }
                    }
                }
            )
        }
    }
    @SuppressLint("MissingSuperCall")
    override fun onBackPressed() {
        Log.d("LoginActivity", "Back pressed, finishing activity")
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d("LoginActivity", "Saving instance state, isLoggedIn: ${sessionManager.isLoggedIn()}")
        outState.putBoolean("isLoggedIn", sessionManager.isLoggedIn())
    }
}



class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences(application)

    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean> = _isLoggedIn

    init {
        viewModelScope.launch {
            _isLoggedIn.value = userPreferences.isLoggedIn.first() // Read from DataStore
        }
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        viewModelScope.launch {
            userPreferences.saveLoginState(isLoggedIn)
            _isLoggedIn.postValue(isLoggedIn)
        }
    }
}









