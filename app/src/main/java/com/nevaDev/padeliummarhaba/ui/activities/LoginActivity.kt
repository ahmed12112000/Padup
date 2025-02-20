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
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.nevaDev.padeliummarhaba.ui.views.ReservationOptions
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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
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
            var isLoggedInState by remember { mutableStateOf(sessionManager.isLoggedIn()) }
            val getReservationViewModel: GetReservationViewModel = hiltViewModel()

            val navigateToMainActivity: () -> Unit = {
                sessionManager.updateLastActiveTime() // ✅ Update session time on successful login
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            }

            // 🔥 Auto Logout Mechanism: Check if the session is still valid
            LaunchedEffect(isLoggedInState) {
                if (!sessionManager.isLoggedIn()) {
                    sessionManager.logout() // ✅ Clear session if inactive for 2 mins
                    isLoggedInState = false
                } else {
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
                    }
                }
            }

            Scaffold(
                bottomBar = {
                    AnimatedBottomBar(
                        navController = navController,
                        getReservationViewModel = getReservationViewModel,
                        navigateToLogin = { route ->
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
                                    onLoginSuccess = { token ->
                                        sessionManager.saveAuthToken(token) // ✅ Save token & reset timer
                                        navigateToMainActivity()
                                    },
                                    viewModel = viewModel,
                                    getProfileViewModel = getProfileViewModel,
                                    navController = navController,
                                    loginRequest = LoginRequest(username = "", password = ""),
                                    destinationRoute = destinationRoute
                                )
                            }
                            composable("signup_screen") {
                                SignUpScreen(
                                    navController = navController,
                                    onSignupSuccess = {},
                                    viewModel = hiltViewModel()
                                )
                            }
                            composable("Profile_screen") {
                                ProfileScreen()
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
                            composable("reservation_options/{selectedDate}/{selectedTimeSlot}") { backStackEntry ->
                                val selectedDate = backStackEntry.arguments?.getString("selectedDate")?.let { LocalDate.parse(it) }
                                val selectedTimeSlot = backStackEntry.arguments?.getString("selectedTimeSlot")
                                val sharedViewModel: SharedViewModel = viewModel()

                                ReservationOptions(
                                    onReservationSelected = { /* Handle reservation selection */ },
                                    key = null, // Pass any required key
                                    navController = navController,
                                    selectedDate = selectedDate ?: LocalDate.now(),
                                    selectedTimeSlot = selectedTimeSlot,
                                    viewModel = hiltViewModel(),
                                    viewModel1 = hiltViewModel(),
                                    viewModel2 = hiltViewModel(),
                                    bookingViewModel = hiltViewModel(),
                                    paymentPayAvoirViewModel = hiltViewModel(),
                                    sharedViewModel = sharedViewModel
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
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
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









