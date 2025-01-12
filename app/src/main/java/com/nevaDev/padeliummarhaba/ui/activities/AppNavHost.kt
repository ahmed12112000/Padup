package com.nevaDev.padeliummarhaba.ui.activities

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nevaDev.padeliummarhaba.models.ReservationOption
import com.nevaDev.padeliummarhaba.repository.Booking.BookingViewModel
import com.nevaDev.padeliummarhaba.ui.views.*
import com.nevaDev.padeliummarhaba.viewmodels.GetBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.KeyViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPayAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.domain.dto.EstablishmentBasicDTO
import com.padelium.domain.dto.EstablishmentPictureBasicDTO
import com.padelium.domain.dto.GetBookingResponse
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


            composable(
                route = "WebViewScreen?paymentUrl={paymentUrl}",
                arguments = listOf(navArgument("paymentUrl") { type = NavType.StringType })
            ) { backStackEntry ->
                val paymentUrl = backStackEntry.arguments?.getString("paymentUrl") ?: ""
                WebViewScreen(formUrl = paymentUrl, navController = navController)
            }

            composable("WebViewScreen?paymentUrl={paymentUrl}") { backStackEntry ->
                val paymentUrl = backStackEntry.arguments?.getString("paymentUrl") ?: ""
                WebViewScreen(formUrl = paymentUrl, navController = navController)
            }
            composable("PaymentSuccessScreen") {
                PaymentSuccessScreen(navController = navController)
            }
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
                val paymentPayAvoirViewModel: PaymentPayAvoirViewModel = hiltViewModel()

                ReservationScreen(
                    navController = navController,
                    isUserLoggedIn = isUserLoggedIn,
                    context = context,
                    sharedPreferences = sharedPreferences,
                    onFetchSuccess = { /* Handle fetch success */ },
                    viewModel = viewModel,
                    getBookingViewModel = getBookingViewModel,
                    paymentPayAvoirViewModel = paymentPayAvoirViewModel


                )
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
                    navArgument("mappedBookings") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name").orEmpty()
                val time = backStackEntry.arguments?.getString("time").orEmpty()
               // val date = backStackEntry.arguments?.getString("date").orEmpty()
                val price = backStackEntry.arguments?.getString("price").orEmpty()

                // Get mappedBookingsJson from arguments
                val mappedBookingsJson = backStackEntry.arguments?.getString("mappedBookings").orEmpty()

                // Deserialize JSON into List<GetBookingResponse>
                val type = object : TypeToken<List<GetBookingResponse>>() {}.type
                val mappedBookings: List<GetBookingResponse> = Gson().fromJson(mappedBookingsJson, type)

                // Use the deserialized mappedBookings in your UI
                PaymentSection1(
                    selectedDate = LocalDate.now(),
                    selectedReservation = ReservationOption(name, time, price, mappedBookingsJson), // Pass JSON string here
                    onExtrasUpdate = { _, _, _ -> },
                    navController = navController,
                    bookingViewModel = hiltViewModel(),
                    selectedTimeSlot = time,
                    mappedBookingsJson = mappedBookingsJson,
                    price = price,
                    onTotalAmountCalculated = { totalAmount, currency ->
                        // Handle total amount calculation here
                    },
                )
            }

            composable(
                route = "reservation_summary/{name}/{time}/{price}/{mappedBookings}",
                arguments = listOf(
                    navArgument("name") { type = NavType.StringType },
                    navArgument("time") { type = NavType.StringType },
                   // navArgument("date") { type = NavType.StringType },
                    navArgument("price") { type = NavType.StringType },
                    navArgument("mappedBookings") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name").orEmpty()
                val time = backStackEntry.arguments?.getString("time").orEmpty()
               // val date = backStackEntry.arguments?.getString("date").orEmpty()
                val price = backStackEntry.arguments?.getString("price").orEmpty()
                val mappedBookingsJson = backStackEntry.arguments?.getString("mappedBookings").orEmpty()
                var selectedDate by remember { mutableStateOf(LocalDate.now()) }

                // Use the parsed values in ReservationSummary
                ReservationSummary(
                    selectedDate = selectedDate, // Parse date string to LocalDate
                    selectedTimeSlot = time,
                    selectedReservation = ReservationOption(name, time, price, mappedBookingsJson), // Pass JSON string here
                    selectedExtras = emptyList(), // Pass any extras if needed
                    amountSelected = null, // Pass appropriate amount if available
                    onTotalAmountCalculated = { totalAmount, currency ->
                        // Handle total amount calculation here
                    },
                    price = price,
                    time = time,
                    navController = navController,

                    )
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








        }
    }
}
