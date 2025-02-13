package com.nevaDev.padeliummarhaba.ui.activities

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
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
import com.nevaDev.padeliummarhaba.viewmodels.UserViewModel
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.LoginRequest
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity : ComponentActivity() {
    //sharedViewModel1
    private val viewModel: UserViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val getProfileViewModel: GetProfileViewModel by viewModels()
    private val sharedViewModel1: com.nevaDev.padeliummarhaba.ui.activities.SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Restore isLoggedIn state from savedInstanceState
        val isLoggedIn = savedInstanceState?.getBoolean("isLoggedIn") ?: false
        sharedViewModel.setLoggedIn(isLoggedIn)

        // Retrieve the destination route from intent
        val destinationRoute = intent?.getStringExtra("destination_route") ?: "main_screen"

        setContent {
            val navController = rememberNavController()
            val isLoggedInState by sharedViewModel.isLoggedIn.observeAsState(false)

            // If the user is logged in, navigate directly to the intended screen
            LaunchedEffect(isLoggedInState) {
                if (isLoggedInState) {
                    Log.d("LoginCheck", "isLoggedInState: $isLoggedInState")

                    val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                        putExtra("navigate_to", destinationRoute)
                    }
                    startActivity(intent)
                    finish() // Close LoginActivity
                }
            }

            NavHost(
                navController = navController,
                startDestination = if (isLoggedInState) destinationRoute else "login_screen"
            ) {
                composable("login_screen") {
                    LoginScreen(
                        onLoginSuccess = {
                            sharedViewModel.setLoggedIn(true) // Update login state

                            val intent = Intent(this@LoginActivity, MainActivity::class.java).apply {
                                putExtra("navigate_to", destinationRoute) // Pass the correct destination
                            }
                            startActivity(intent)
                            finish() // Close LoginActivity and return to the correct screen

                        },
                        viewModel = viewModel,
                        getProfileViewModel = getProfileViewModel,
                        navController = navController,
                        loginRequest = LoginRequest(username = "", password = ""),
                        destinationRoute = destinationRoute,
                        sharedViewModel = sharedViewModel1
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
                composable("CreditCharge") {
                    CreditCharge(navController = navController)
                }
                composable("CreditPayment") { CreditPayment(navController = navController) }
                composable("summary_screen") {
                    SummaryScreen(navController = navController)
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
                        //  navArgument("date") { type = NavType.StringType },
                        navArgument("price") { type = NavType.StringType },
                        navArgument("mappedBookings") { type = NavType.StringType },

                        )
                ) { backStackEntry ->
                    val name = backStackEntry.arguments?.getString("name").orEmpty()
                    val time = backStackEntry.arguments?.getString("time").orEmpty()
                    // val date = backStackEntry.arguments?.getString("date").orEmpty()
                    val price = backStackEntry.arguments?.getString("price").orEmpty()
                    val adjustedAmounte = backStackEntry.arguments?.getDouble("adjustedAmounte") ?: 0.0
                    val totalExtrasCost = backStackEntry.arguments?.getDouble("totalExtrasCost") ?: 0.0
                    val totalAmountSelected = backStackEntry.arguments?.getDouble("totalAmountSelected") ?: 0.0

                    // Get mappedBookingsJson from arguments
                    val mappedBookingsJson = backStackEntry.arguments?.getString("mappedBookings").orEmpty()

                    // Deserialize JSON into List<GetBookingResponse>
                    val type = object : TypeToken<List<GetBookingResponse>>() {}.type
                    val mappedBookings: List<GetBookingResponse> = Gson().fromJson(mappedBookingsJson, type)
                    val selectedParts = backStackEntry.arguments?.getInt("selectedParts") ?: 1

                    // Use the deserialized mappedBookings in your UI
                    PaymentSection1(
                        selectedDate = LocalDate.now(),
                        selectedReservation = ReservationOption(name, time, price, mappedBookingsJson),
                        onExtrasUpdate = { _, _, _ -> },
                        navController = navController,
                        bookingViewModel = hiltViewModel(),
                        selectedTimeSlot = time,
                        mappedBookingsJson = mappedBookingsJson,
                        price = price,
                        onTotalAmountCalculated = { totalAmount, currency ->
                        },
                        viewModel9 = hiltViewModel(),
                        //totalExtrasCost = totalExtrasCost,

                    )
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


class SharedViewModel(application: Application) : AndroidViewModel(application) {
    private val userPreferences = UserPreferences(application)

    val isLoggedIn: LiveData<Boolean> = userPreferences.isLoggedIn.asLiveData()

    fun setLoggedIn(isLoggedIn: Boolean) {
        viewModelScope.launch {
            userPreferences.saveLoginState(isLoggedIn)
        }
    }
}







