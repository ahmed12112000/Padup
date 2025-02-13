package com.nevaDev.padeliummarhaba.ui.activities

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.nevaDev.padeliummarhaba.ui.views.*
import com.nevaDev.padeliummarhaba.viewmodels.GetBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetEmailViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetManagerViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetPaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.nevaDev.padeliummarhaba.viewmodels.KeyViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PartnerPayViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentGetAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPartBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPayAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.UserViewModel
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.GetReservationResponse
import com.padelium.domain.dto.LoginRequest
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
    onSignupSuccess: () -> Unit,
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
            )
        }

        NavHost(
            navController = navController,
            startDestination  = "main_screen"
            //              startDestination = if (isUserLoggedIn) "main_screen" else "login_screen")
        ) {
            composable("Profile_screen") {
                ProfileScreen()
            }

            composable("login_screen") { backStackEntry ->
                val viewModel: UserViewModel = hiltViewModel()
                val getProfileViewModel: GetProfileViewModel = hiltViewModel()
                val sharedViewModel: SharedViewModel = viewModel()
                val destinationRoute = backStackEntry.arguments?.getString("destination_route") ?: "main_screen"

                LoginScreen(
                    onLoginSuccess = {
                        onLoginSuccess()
                        navController.navigate(destinationRoute) { // Navigate to the intended destination
                            popUpTo("login_screen") { inclusive = true }
                        }
                    },
                    viewModel = viewModel,
                    getProfileViewModel = getProfileViewModel,
                    navController = navController,
                    loginRequest = LoginRequest("", ""),
                    destinationRoute = destinationRoute,
                    sharedViewModel = sharedViewModel

                )
            }

            composable("reservation_options/{selectedDate}/{selectedTimeSlot}") { backStackEntry ->
                val selectedDate = backStackEntry.arguments?.getString("selectedDate")?.let { LocalDate.parse(it) }
                val selectedTimeSlot = backStackEntry.arguments?.getString("selectedTimeSlot")
                val sharedViewModel: SharedViewModel = viewModel() // Ensure it's created

                ReservationOptions(
                    onReservationSelected = { /* Handle reservation selection */ },
                 //   isUserLoggedIn = isUserLoggedIn,
                    key = null, // Pass any required key
                    navController = navController,
                    selectedDate = selectedDate ?: LocalDate.now(), // Default to today if null
                    selectedTimeSlot = selectedTimeSlot,
                    viewModel = hiltViewModel(),
                    viewModel1 = hiltViewModel(),
                    viewModel2 = hiltViewModel(),
                    bookingViewModel = hiltViewModel(),
                    paymentPayAvoirViewModel = hiltViewModel(),
                    sharedViewModel = sharedViewModel
                )
            }




            composable("CreditCharge") {
                CreditCharge(navController = navController)
            }

            composable(
                route = "WebViewScreen1?formUrl={formUrl}&encodedAmount={encodedAmount}&encodedId={encodedId}",
                arguments = listOf(
                    navArgument("formUrl") { type = NavType.StringType },
                    navArgument("encodedAmount") { type = NavType.StringType; defaultValue = "0" },
                    navArgument("encodedId") { type = NavType.LongType; defaultValue = 0L }
                )
            ) { backStackEntry ->
                val formUrl = backStackEntry.arguments?.getString("formUrl") ?: ""
                val encodedAmount = backStackEntry.arguments?.getString("encodedAmount")?.toBigDecimalOrNull() ?: BigDecimal.ZERO
                val paymentGetAvoirViewModel: PaymentGetAvoirViewModel = hiltViewModel()
                val encodedId = backStackEntry.arguments?.getLong("encodedId") ?: 0L

                WebViewScreen1(
                    formUrl = formUrl,
                    navController = navController,
                    paymentGetAvoirViewModel = paymentGetAvoirViewModel,
                    amount = encodedAmount,
                    Id = encodedId
                )
            }

            composable(
                route = "WebViewScreen2?formUrl={formUrl}&orderId={orderId}&BookingId={BookingId}&privateList={privateList}&encodedPartnerPayId={encodedPartnerPayId}",
                arguments = listOf(
                    navArgument("formUrl") { type = NavType.StringType },
                    navArgument("orderId") { type = NavType.StringType },
                    navArgument("BookingId") { type = NavType.StringType },
                    navArgument("privateList") { type = NavType.StringType; defaultValue = "" },
                    navArgument("encodedPartnerPayId") { type = NavType.StringType },

                    )
            ) { backStackEntry ->
                val formUrl = backStackEntry.arguments?.getString("formUrl") ?: ""
                val orderId = backStackEntry.arguments?.getString("orderId") ?: ""
                val bookingId = backStackEntry.arguments?.getString("BookingId") ?: ""
                val privateListString = backStackEntry.arguments?.getString("privateList") ?: ""
                val privateList = privateListString.split(",").mapNotNull { it.toLongOrNull() }.toMutableList()

                val viewmodel: PaymentPartBookingViewModel = hiltViewModel()

                WebViewScreen2(
                    navController = navController,
                    viewmodel = viewmodel,
                    formUrl = formUrl,
                )
            }




            composable(
                route = "WebViewScreen?paymentUrl={paymentUrl}&numberOfPart={numberOfPart}&userIds={userIds}&sharedList={sharedList}&privateList={privateList}",
                arguments = listOf(
                    navArgument("paymentUrl") { type = NavType.StringType },
                    navArgument("numberOfPart") { type = NavType.IntType; defaultValue = 1 },
                    navArgument("userIds") { type = NavType.StringType; defaultValue = "" },
                    navArgument("sharedList") { type = NavType.StringType; defaultValue = "" },
                    navArgument("privateList") { type = NavType.StringType; defaultValue = "" }

                )
            ) { backStackEntry ->
                val paymentUrl = backStackEntry.arguments?.getString("paymentUrl") ?: ""
                val getPaymentViewModel: GetPaymentViewModel = hiltViewModel()
                val getManagerViewModel: GetManagerViewModel = hiltViewModel()
                val getEmailViewModel: GetEmailViewModel = hiltViewModel()
                val numberOfPart = backStackEntry.arguments?.getInt("numberOfPart") ?: 1
                val encodedUserIds = backStackEntry.arguments?.getString("userIds").orEmpty()
                val encodedSharedList = backStackEntry.arguments?.getString("sharedList").orEmpty()
                val encodedPrivateList = backStackEntry.arguments?.getString("privateList").orEmpty()

                // Observe selectedParts from the StateFlow
                WebViewScreen(
                    formUrl = paymentUrl,
                    navController = navController,
                    getPaymentViewModel = getPaymentViewModel,
                    getManagerViewModel = getManagerViewModel,
                    getEmailViewModel = getEmailViewModel,
                    numberOfPart = numberOfPart,
                    userIds = encodedUserIds,
                    sharedList = encodedSharedList,
                    privateList = encodedPrivateList
                )
            }





            composable("PaymentSuccessScreen") {
                PaymentSuccessScreen(navController = navController)
            }
            // MainScreen with top bar
            composable("main_screen") {
                MainScreen(
                    navController = navController,
                    onReservationClicked = { selectedDate ->

                    }
                )
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
                val sharedViewModel: SharedViewModel = viewModel() // Ensure it's created

                ReservationScreen(
                    navController = navController,
                    isUserLoggedIn = isUserLoggedIn,
                    context = context,
                    sharedPreferences = sharedPreferences,
                    onFetchSuccess = { /* Handle fetch success */ },
                    viewModel = viewModel,
                    getBookingViewModel = getBookingViewModel,
                    paymentPayAvoirViewModel = paymentPayAvoirViewModel,
                    sharedViewModel = sharedViewModel
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

            composable(
                route = "PartnerPaymentScreen/{partnerPayId}",
                arguments = listOf(navArgument("partnerPayId") { type = NavType.StringType }) // Expect a String argument
            ) { backStackEntry ->
                val partnerPayId = backStackEntry.arguments?.getString("partnerPayId")

                PartnerPaymentScreen(
                    navController = navController,
                    partnerPayId = partnerPayId // Pass ID to screen
                )
            }


            composable("CreditPayment") { CreditPayment(navController = navController) }



            /*
                        composable("login_screen") {
                            LoginScreen(
                                onLoginSuccess = onLoginSuccess,
                                navController = navController,
                                loginRequest = LoginRequest("", "")
                            )
                        }



            composable("signup_screen") {
                SignUpScreen(
                    onSignupSuccess = onSignupSuccess,
                    navController = navController,
                )
            }

             */

            /*
                        composable("Profile_screen") {
                            ProfileScreen()
                        }

             */







            /*


             */
            /*  composable("PaymentSuccessScreen") {
                  PaymentSuccessScreen(navController = navController)
              }*/


            composable("summary_screen") {
                SummaryScreen(navController = navController)
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

        }
    }
}
