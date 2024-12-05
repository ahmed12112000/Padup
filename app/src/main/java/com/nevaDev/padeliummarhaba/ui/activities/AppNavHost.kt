package com.nevaDev.padeliummarhaba.ui.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.nevaDev.padeliummarhaba.models.ReservationOption
import com.nevaDev.padeliummarhaba.ui.views.*
import com.nevaDev.padeliummarhaba.viewmodels.GetBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.KeyViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.dto.SaveBookingRequest
import com.padelium.domain.repositories.ISaveBookingRepository
import kotlinx.coroutines.CoroutineScope
import java.time.LocalDate
import java.time.format.DateTimeFormatter

sealed class Screen(val route: String)

@Composable
fun AppNavHost(
    navController: NavHostController,
    isUserLoggedIn: Boolean,
    onLoginSuccess: () -> Unit,
    onLogout: () -> Unit,
    context: Context,
    sharedPreferences: SharedPreferences,
    drawerState: DrawerState,
    scope: CoroutineScope,

) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
    // Set the TopBar to be shown only on "main_screen"
    Column {
        if (currentRoute == "main_screen") {
            TopBar(
                navController = navController,
                drawerState = drawerState,
                scope = scope
            )        }

        NavHost(
            navController = navController,
            startDestination = if (isUserLoggedIn) "main_screen" else "login_screen")
        {
            // MainScreen with top bar
            composable("main_screen") {
                MainScreen(
                    navController = navController,
                    onReservationClicked = { selectedDate ->
                        val key = "someKey"
                        val activityName = "SomeActivity"
                        val cityName = "SomeCity"
                        val activityId = "1"
                        val cityId = "1"
                        val establishmentId = "1"
                        val time = "10:00"
                        val isCity = false
                        val formattedDate =
                            selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                        val destination =
                            "reservation_screen/$key/$formattedDate/$activityName/$cityName/$activityId/$cityId/$establishmentId/$time/$isCity"
                        navController.navigate(destination)
                    }
                )
            }

            // Reservation Screen
            composable(
                route = "reservation_screen/{key}/{date}/{activityName}/{cityName}/{activityId}/{cityId}/{establishmentId}/{time}/{isCity}",
                arguments = listOf(
                    navArgument("key") { type = NavType.StringType },
                    navArgument("date") { type = NavType.StringType },
                    navArgument("activityName") { type = NavType.StringType },
                    navArgument("cityName") { type = NavType.StringType },
                    navArgument("activityId") { type = NavType.StringType },
                    navArgument("cityId") { type = NavType.StringType },
                    navArgument("establishmentId") { type = NavType.StringType },
                    navArgument("time") { type = NavType.StringType },
                    navArgument("isCity") { type = NavType.BoolType }
                )
            ) { backStackEntry ->
                val viewModel: KeyViewModel = hiltViewModel()
                val getBookingViewModel: GetBookingViewModel = hiltViewModel()

                ReservationScreen(
                    navController = navController,
                    isUserLoggedIn = isUserLoggedIn,
                    context = context,
                    sharedPreferences = sharedPreferences,
                    onFetchSuccess = { /* Handle fetch success */ },
                    viewModel = viewModel,
                    getBookingViewModel = getBookingViewModel
                )
            }

            // Other Screens (without top bar)
            composable("PaymentScreen1") {
                // Obtain instances of the ViewModels
                val saveBookingViewModel: SaveBookingViewModel = hiltViewModel()
                val paymentViewModel: PaymentViewModel = hiltViewModel()

                // Access the current saveBookingRequest and paymentRequest
                val saveBookingRequest by saveBookingViewModel.saveBookingRequest.observeAsState(emptyList())
                val paymentRequest by paymentViewModel.paymentRequest.observeAsState(
                    PaymentRequest(amount = "", orderId = "", currency = "")
                )

                // Sample data for other parameters
                val selectedDate = LocalDate.now()
                val selectedTimeSlot = "10:00 AM"
                val selectedReservation = ReservationOption(
                    time = "1",
                    name = "Standard Reservation",
                    price = "100.0",
                    duration = "Reservation for one hour"
                )
                val totalAmount = "100.0"

                PaymentSection1(
                    selectedDate = selectedDate,
                    selectedTimeSlot = selectedTimeSlot,
                    selectedReservation = selectedReservation,
                    onExtrasUpdate = { extra1, extra2, extra3 ->
                        // Handle extras update logic here
                    },
                    onPayWithCardClick = {
                        // Call Payment function in the ViewModel
                    },
                    totalAmount = totalAmount,
                    navController = navController,
                    viewModel = saveBookingViewModel,
                    saveBookingRequest = saveBookingRequest,
                    viewModel1 = paymentViewModel,
                    paymentRequest = paymentRequest
                )
            }


            composable(
                route = "WebViewScreen?paymentUrl={paymentUrl}",
                arguments = listOf(navArgument("paymentUrl") { type = NavType.StringType })
            ) { backStackEntry ->
                val paymentUrl = backStackEntry.arguments?.getString("paymentUrl") ?: ""
                WebViewScreen(paymentUrl = paymentUrl, navController = navController)
            }

            composable("CreditPayment") { CreditPayment(navController = navController) }
            composable("CreditCharge") { CreditCharge() }
            composable("login_screen") {
                LoginScreen(onLoginSuccess = onLoginSuccess,  navController = navController)
            }
            composable("Profile_screen") {
                ProfileScreen(onLogout = onLogout)
            }
            composable("summary_screen") {
                SummaryScreen() // Your SummaryScreen Composable
            }
            composable("main_screen") {
                MainScreen(
                    navController = navController,
                    onReservationClicked = { selectedDate ->
                        val key = "someKey"
                        val activityName = "SomeActivity"
                        val cityName = "SomeCity"
                        val activityId = "1"
                        val cityId = "1"
                        val establishmentId = "1"
                        val time = "10:00"
                        val isCity = false
                        val formattedDate =
                            selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                        val destination =
                            "reservation_screen/$key/$formattedDate/$activityName/$cityName/$activityId/$cityId/$establishmentId/$time/$isCity"
                        navController.navigate(destination)
                    }
                )
            }
            composable("signup_screen") {
                SignUpScreen(
                    navController = navController,
                    onSignupSuccess = {},
                    viewModel = hiltViewModel()
                )
            }
            composable(
                route = "PayScreen/{totalAmount}",
                arguments = listOf(
                    navArgument("totalAmount") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val totalAmount = backStackEntry.arguments?.getString("totalAmount") ?: "0"

                PayScreen(
                    totalAmount = totalAmount,
                    navController = navController,
                    onPayWithCardClick = {
                    },
                    saveBookingRequest = listOf(),
                    paymentRequest = PaymentRequest(
                        amount = totalAmount,
                        currency = "",
                        orderId = ""
                    )
                )
            }

            composable(
                route = "PaymentSection1/{totalAmount}",
                arguments = listOf(
                    navArgument("totalAmount") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val totalAmount = backStackEntry.arguments?.getString("totalAmount") ?: "0"
                val saveBookingViewModel: SaveBookingViewModel = hiltViewModel()
                val paymentViewModel: PaymentViewModel = hiltViewModel()

                // Access the current saveBookingRequest and paymentRequest
                val saveBookingRequest by saveBookingViewModel.saveBookingRequest.observeAsState(emptyList())
                val paymentRequest by paymentViewModel.paymentRequest.observeAsState(
                    PaymentRequest(amount = "", orderId = "", currency = "")
                )
                val selectedDate = LocalDate.now()
                val selectedTimeSlot = "10:00 AM"
                val selectedReservation = ReservationOption(
                    time = "1",
                    name = "Standard Reservation",
                    price = "100.0",
                    duration = "Reservation for one hour"
                )
                PaymentSection1(
                    selectedDate = selectedDate,
                    selectedTimeSlot = selectedTimeSlot,
                    selectedReservation = selectedReservation,
                    onExtrasUpdate = { extra1, extra2, extra3 ->
                    },
                    onPayWithCardClick = {
                    },
                    totalAmount = totalAmount,
                    navController = navController,
                    viewModel = saveBookingViewModel,
                    saveBookingRequest = saveBookingRequest,
                    viewModel1 = paymentViewModel,
                    paymentRequest = paymentRequest
                )
            }
        }
    }
}
