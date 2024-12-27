package com.nevaDev.padeliummarhaba.ui.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.nevaDev.padeliummarhaba.models.ReservationOption
import com.nevaDev.padeliummarhaba.repository.Booking.BookingViewModel
import com.nevaDev.padeliummarhaba.ui.views.*
import com.nevaDev.padeliummarhaba.viewmodels.GetBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.KeyViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.padelium.domain.dto.EstablishmentBasicDTO
import com.padelium.domain.dto.EstablishmentPictureBasicDTO
import com.padelium.domain.dto.LoginRequest
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.dto.PlanningBasicDTO
import com.padelium.domain.dto.SaveBookingRequest
import com.padelium.domain.dto.happyHoursBasicDTO
import kotlinx.coroutines.CoroutineScope
import java.math.BigDecimal
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


            composable(
                route = "WebViewScreen?paymentUrl={paymentUrl}",
                arguments = listOf(navArgument("paymentUrl") { type = NavType.StringType })
            ) { backStackEntry ->
                val paymentUrl = backStackEntry.arguments?.getString("paymentUrl") ?: ""
                WebViewScreen(paymentUrl = paymentUrl, navController = navController)
            }

            composable("WebViewScreen?paymentUrl={paymentUrl}") { backStackEntry ->
                val paymentUrl = backStackEntry.arguments?.getString("paymentUrl") ?: ""
                WebViewScreen(paymentUrl = paymentUrl, navController = navController)
            }
            composable("PaymentSuccessScreen") {
                PaymentSuccessScreen(navController = navController)
            }

            composable("CreditPayment") { CreditPayment(navController = navController) }
            composable("CreditCharge") { CreditCharge() }
            composable("login_screen") {
                LoginScreen(
                    onLoginSuccess = onLoginSuccess,
                    navController = navController,
                    loginRequest = LoginRequest("", "") // Provide initial values as empty or pre-filled as needed.
                )
            }
            composable("Profile_screen") {
                ProfileScreen(onLogout = onLogout)
            }
            composable("summary_screen") {
                SummaryScreen()
            }
            /*  composable("PaymentSuccessScreen") {
                  PaymentSuccessScreen(navController = navController)
              }*/
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


            composable("reservation_options") { backStackEntry ->
                ReservationOptions(
                    onReservationSelected = { reservationOption ->
                        navController.navigate("payment_section1/${reservationOption.name}/${reservationOption.time}/${reservationOption.date}/${reservationOption.price}")
                    },
                    isUserLoggedIn = true, // Example value
                    key = null,
                    navController = navController,
                    selectedDate = LocalDate.now(), // Example value
                     selectedTimeSlot = "12:00 PM" // Example value
                )
            }

            composable(
                "PaymentSection1/{amountSelected}/{currencySymbol}/{name}/{time}/{date}",
                arguments = listOf(
                    navArgument("amountSelected") { type = NavType.FloatType },
                    navArgument("name") { type = NavType.StringType },
                    navArgument("time") { type = NavType.StringType },
                    navArgument("date") { type = NavType.StringType },
                    navArgument("currencySymbol") { type = NavType.StringType },

                    )
            ) { backStackEntry ->

                val amountSelected = backStackEntry.arguments?.getFloat("amountSelected")?.toDouble() ?: 0.0
                val currencySymbol = backStackEntry.arguments?.getString("currencySymbol") ?: ""
                val name = backStackEntry.arguments?.getString("name") ?: ""
                val time = backStackEntry.arguments?.getString("time") ?: ""
                val date = backStackEntry.arguments?.getString("date") ?: ""

                val amount = backStackEntry.arguments?.getString("amount") ?: ""
                val currency = backStackEntry.arguments?.getString("currency") ?: ""
                val orderId = backStackEntry.arguments?.getString("orderId") ?: ""

                val paymentRequest = PaymentRequest(
                    amount = amount,
                    currency = currency,
                    orderId = orderId
                )

                val viewModel = hiltViewModel<SaveBookingViewModel>()
                val viewModel1 = hiltViewModel<PaymentViewModel>()
                val viewModel2 = hiltViewModel<com.nevaDev.padeliummarhaba.ui.views.BookingViewModel>()

                PaymentSection1(
                    selectedDate = LocalDate.now(),
                    selectedTimeSlot = time,
                    selectedReservation = ReservationOption(name, time, "$amountSelected", date),
                    onExtrasUpdate = { extra1, extra2, extra3 -> },
                    onPayWithCardClick = { /* Handle pay click */ },
                    totalAmount = "$amountSelected",
                    navController = navController,
                    paymentRequest = paymentRequest,
                    viewModel = viewModel,
                    viewModel1 = viewModel1,
                    bookingViewModel = viewModel2,
                    amountSelected = amountSelected,
                    currencySymbol = currencySymbol,

                    )
            }


        }
    }
}
