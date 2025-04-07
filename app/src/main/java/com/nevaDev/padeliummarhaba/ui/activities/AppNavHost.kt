package com.nevaDev.padeliummarhaba.ui.activities

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.net.http.SslError
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
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
import com.nevaDev.padeliummarhaba.viewmodels.ErrorCreditViewModel
import com.nevaDev.padeliummarhaba.viewmodels.FindTermsViewModel
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
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.CreditErrorRequest
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.GetPaymentRequest
import com.padelium.domain.dto.GetReservationResponse
import com.padelium.domain.dto.LoginRequest
import com.padelium.domain.dto.PaymentGetAvoirRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
    modifier: Modifier = Modifier
    ) {
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

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
        ) {
            composable("Profile_screen") {
                ProfileScreen(
                    navController = navController,
                )
            }

            composable("SignUp_SuccessScreen") {
                SignUpSuccessScreen(
                    navController = navController,
                )
            }

            composable("server_error_screen") {
                ServerErrorScreen(onRetry = {},
                    navController = navController
                )
            }

            composable("payment_error_screen") {
                PaymentErrorScreen(onRetry = {},
                    navController = navController,
                )
            }

            composable("popup_credit") { backStackEntry ->
                var showPopup by remember { mutableStateOf(true) } // Manage popup visibility state
                val name = backStackEntry.arguments?.getString("name").orEmpty()
                val time = backStackEntry.arguments?.getString("time").orEmpty()
                val price = backStackEntry.arguments?.getString("price").orEmpty()
                // Retrieve necessary arguments (ensure they're passed properly when navigating)
                val mappedBookingsJson = backStackEntry.arguments?.getString("mappedBookingsJson") ?: ""
                val selectedDate = LocalDate.parse(backStackEntry.arguments?.getString("selectedDate") ?: LocalDate.now().toString())
                val bookingId = backStackEntry.arguments?.getString("bookingId")
                val playerIds = backStackEntry.arguments?.getString("playerIds")
                val playerIdList: List<Long> = playerIds?.split(",")?.mapNotNull { it.toLongOrNull() } ?: emptyList()

                if (showPopup) {
                    PopupCredit(
                        onPayClick = {
                            showPopup = false
                            navController.navigate("main_screen")
                        },
                        onCancelClick = {
                            showPopup = false
                            navController.popBackStack()
                        },
                        viewModel = hiltViewModel(), // GetProfileViewModel
                        navController = navController,
                        errorCreditViewModel = hiltViewModel(), // ErrorCreditViewModel
                        onTotalAmountCalculated = { amount, currency ->
                            Log.d("TotalAmount", "Total Amount Calculated: $amount $currency")
                        },
                        adjustedAmount = 0.0,
                        totalExtrasCost = 0.0,
                        showPopup = showPopup, // Pass visibility state
                        onDismiss = { showPopup = false }, // Handle dismiss event
                        mappedBookingsJson = mappedBookingsJson, // Pass mapped bookings data
                        viewModel9 = hiltViewModel(), // SharedViewModel
                        findTermsViewModel = hiltViewModel(), // FindTermsViewModel
                        selectedDate = selectedDate, // Pass selected date
                        selectedReservation = ReservationOption(name, time, price, mappedBookingsJson),
                        saveBookingViewModel = hiltViewModel(),
                        bookingId= bookingId,
                        playerIds = playerIdList
                    )
                }
            }


            composable("signup_screen") {
                SignUpScreen(
                    navController = navController,
                    onSignupSuccess = {},
                    viewModel = hiltViewModel()
                )
            }

            composable(
                route = "login_screen?redirectUrl={redirectUrl}",
                arguments = listOf(
                    navArgument("redirectUrl") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = ""
                    }
                )
            ) { backStackEntry ->
                val redirectUrl = backStackEntry.arguments?.getString("redirectUrl") ?: ""

                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(redirectUrl.ifEmpty { "main_screen" }) {
                            popUpTo("login_screen") { inclusive = true }
                        }
                    },
                    viewModel = hiltViewModel(),
                    getProfileViewModel = hiltViewModel(),
                    navController = navController,
                    loginRequest = LoginRequest("", ""),
                )
            }









            composable("reservation_options/{selectedDate}",
            arguments = listOf(
                navArgument("selectedDate") { type = NavType.StringType },
               // navArgument("selectedTimeSlot") { type = NavType.StringType }
            )) { backStackEntry ->
                val selectedDate = backStackEntry.arguments?.getString("selectedDate")?.let { LocalDate.parse(it) }
                val selectedTimeSlot = backStackEntry.arguments?.getString("selectedTimeSlot")
                val sharedViewModel: SharedViewModel = viewModel()
                val destinationRoute = backStackEntry.arguments?.getString("destination_route") ?: "main_screen"

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
                    getReservationViewModel = hiltViewModel(),
                )
            }
            composable("reservation_options") { backStackEntry ->
                val selectedDate = backStackEntry.arguments?.getString("selectedDate")?.let { LocalDate.parse(it) }
                val selectedTimeSlot = backStackEntry.arguments?.getString("selectedTimeSlot")
                val sharedViewModel: SharedViewModel = viewModel()
                val destinationRoute = backStackEntry.arguments?.getString("destination_route") ?: "main_screen"

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
                    getReservationViewModel = hiltViewModel(),

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
                    navArgument("encodedId") { type = NavType.LongType; defaultValue = 0L },

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
                    Id = encodedId,

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
                    },
                )
            }




            composable(
                route = "WebViewScreen?paymentUrl={paymentUrl}&numberOfPart={numberOfPart}&userIds={userIds}&sharedList={sharedList}&privateList={privateList}&bookingId={bookingId}",
                arguments = listOf(
                    navArgument("paymentUrl") { type = NavType.StringType },
                    navArgument("numberOfPart") { type = NavType.IntType; defaultValue = 1 },
                    navArgument("userIds") { type = NavType.StringType; defaultValue = "" },
                    navArgument("sharedList") { type = NavType.StringType; defaultValue = "" },
                    navArgument("privateList") { type = NavType.StringType; defaultValue = "" },
                    navArgument("bookingId") { type = NavType.StringType; defaultValue = "" },

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
                val encodedbookingId = backStackEntry.arguments?.getString("bookingId").orEmpty()

                WebViewScreen(
                    formUrl = paymentUrl,
                    navController = navController,
                    getPaymentViewModel = getPaymentViewModel,
                    getManagerViewModel = getManagerViewModel,
                    getEmailViewModel = getEmailViewModel,
                    numberOfPart = numberOfPart,
                    userIds = encodedUserIds,
                    sharedList = encodedSharedList,
                    privateList = encodedPrivateList,
                    bookingIds = encodedbookingId,
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
                    },
                    )
            }





            composable("PaymentSuccessScreen") {
                PaymentSuccessScreen(navController = navController)
            }

            composable("main_screen") {
                MainScreen(
                    navController = navController,
                    onReservationClicked = { selectedDate ->
                    },

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
                    },

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
                val destinationRoute = backStackEntry.arguments?.getString("destination_route") ?: "main_screen"

                ReservationScreen(
                    navController = navController,
                    isUserLoggedIn = isUserLoggedIn,
                    context = context,
                    sharedPreferences = sharedPreferences,
                    onFetchSuccess = { /* Handle fetch success */ },
                    viewModel = viewModel,
                    getBookingViewModel = getBookingViewModel,
                    paymentPayAvoirViewModel = paymentPayAvoirViewModel,
                    getReservationViewModel = hiltViewModel(),

                )
            }


            composable(
                "payment_section1/{name}/{time}/{price}/{mappedBookings}/{selectedDate}",
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "android-app://androidx.navigation/payment_section1/{name}/{time}/{price}/{mappedBookings}/{selectedDate}"
                    }
                ),
                arguments = listOf(
                    navArgument("name") { type = NavType.StringType },
                    navArgument("time") { type = NavType.StringType },
                    //  navArgument("date") { type = NavType.StringType },
                    navArgument("price") { type = NavType.StringType },
                    navArgument("mappedBookings") { type = NavType.StringType },
                    navArgument("selectedDate") { type = NavType.StringType } // Add selectedDate argument

                    )
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name").orEmpty()
                val time = backStackEntry.arguments?.getString("time").orEmpty()
                val price = backStackEntry.arguments?.getString("price").orEmpty()
                val adjustedAmounte = backStackEntry.arguments?.getDouble("adjustedAmounte") ?: 0.0
                val totalExtrasCost = backStackEntry.arguments?.getDouble("totalExtrasCost") ?: 0.0
                val totalAmountSelected = backStackEntry.arguments?.getDouble("totalAmountSelected") ?: 0.0

                val mappedBookingsJson = backStackEntry.arguments?.getString("mappedBookings").orEmpty()

                val type = object : TypeToken<List<GetBookingResponse>>() {}.type
                val mappedBookings: List<GetBookingResponse> = Gson().fromJson(mappedBookingsJson, type)
                val selectedParts = backStackEntry.arguments?.getInt("selectedParts") ?: 1
                val selectedDateString = backStackEntry.arguments?.getString("selectedDate").orEmpty() // Get selectedDate

                val selectedDate: LocalDate = try {
                    LocalDate.parse(selectedDateString) // Adjust the format if necessary
                } catch (e: Exception) {
                    // Handle the exception (e.g., log it, show a message, etc.)
                    LocalDate.now() // Fallback to current date or handle as needed
                }
                PaymentSection1(
                    selectedDate = selectedDate,
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
                    )
            }

            composable(
                route = "PartnerPaymentScreen/{partnerPayId}",
                arguments = listOf(navArgument("partnerPayId") { type = NavType.StringType })
            ) { backStackEntry ->
                val partnerPayId = backStackEntry.arguments?.getString("partnerPayId")

                PartnerPaymentScreen(
                    navController = navController,
                    partnerPayId = partnerPayId
                )
            }


            composable("CreditPayment") {
                val sharedViewModel: SharedViewModel = hiltViewModel()

                CreditPayment(
                    navController = navController,
                )
            }

            composable("summary_screen") {
                val sharedViewModel: SharedViewModel = hiltViewModel()

                SummaryScreen(
                    navController = navController,
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
                    },

                )
            }


            composable(
                route = "popup_partner_credit/{partnerPayId}/{totalPrice}",
                arguments = listOf(
                    navArgument("partnerPayId") { type = NavType.StringType },
                    navArgument("totalPrice") { type = NavType.StringType } // Add totalPrice as an argument
                )
            ) { backStackEntry ->
            var showPopup by remember { mutableStateOf(true) } // Manage popup visibility state
                val name = backStackEntry.arguments?.getString("name").orEmpty()
                val time = backStackEntry.arguments?.getString("time").orEmpty()
                val price = backStackEntry.arguments?.getString("price").orEmpty()
                // Retrieve necessary arguments (ensure they're passed properly when navigating)
                val mappedBookingsJson = backStackEntry.arguments?.getString("mappedBookingsJson") ?: ""
                val selectedDate = LocalDate.parse(backStackEntry.arguments?.getString("selectedDate") ?: LocalDate.now().toString())
                val bookingId = backStackEntry.arguments?.getString("bookingId")
                val playerIds = backStackEntry.arguments?.getString("playerIds")
                val playerIdList: List<Long> = playerIds?.split(",")?.mapNotNull { it.toLongOrNull() } ?: emptyList()
                val partnerPayId = backStackEntry.arguments?.getString("partnerPayId")
                val totalPriceString = backStackEntry.arguments?.getString("totalPrice").orEmpty()
                var totalPrice = totalPriceString.toBigDecimalOrNull() ?: BigDecimal.ZERO

                if (showPopup) {
                    PopupCreditPartner(

                        navController = navController,
                        showPopup = showPopup, // Pass visibility state
                        onDismiss = { showPopup = false }, // Handle dismiss event
                        viewModel4 = hiltViewModel(), // SharedViewModel
                     //   bookingId =bookingId,
                        viewModel = hiltViewModel(),
                        partnerPayId = partnerPayId,
                        totalPrice = totalPrice,


                    )
                }
            }

        }
    }
}





