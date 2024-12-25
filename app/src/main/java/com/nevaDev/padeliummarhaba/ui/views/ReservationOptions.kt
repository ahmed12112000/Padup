package com.nevaDev.padeliummarhaba.ui.views

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nevaDev.padeliummarhaba.models.ReservationOption
import com.nevaDev.padeliummarhaba.viewmodels.ExtrasViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetBookingViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.Extra
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.ui.tooling.preview.Preview
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.TimeSlot
import com.padelium.data.dto.GetBookingResponseDTO


@Composable
fun ReservationOptions2(
    onReservationSelected: (ReservationOption) -> Unit,
    isUserLoggedIn: Boolean,
    key: String?,
    navController: NavController,
    viewModel: GetBookingViewModel = hiltViewModel(),
    viewModel1: ExtrasViewModel = hiltViewModel(),
    selectedDate: LocalDate,
    selectedTimeSlot: String?
) {
    var selectedReservation by remember { mutableStateOf<ReservationOption?>(null) }
    var showLoginPopup by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    val dataResultBooking by viewModel.dataResultBooking.observeAsState(DataResultBooking.Loading)
    val filteredTimeSlots by viewModel.filteredTimeSlots.observeAsState(emptyList())
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val saveBookingViewModel: SaveBookingViewModel = hiltViewModel()

    LaunchedEffect(key) { key?.let { viewModel.getBooking(it ) } }

    when (val result = dataResultBooking) {
        is DataResultBooking.Loading -> LoadingIndicator2()
        is DataResultBooking.Success -> BookingList2(
            result = result,
            filteredTimeSlots = filteredTimeSlots,
            isUserLoggedIn = isUserLoggedIn,
            navController = navController,
            onReservationSelected = { reservation, fetchExtras ->
                selectedReservation = reservation
                if (!isUserLoggedIn) {
                    showLoginPopup = true
                } else {
                    onReservationSelected(reservation)
                    fetchExtras()
                }
            },
            viewModel1 = viewModel1,
            lifecycleOwner = lifecycleOwner,
            isLoading = { isLoading = it },
            setErrorMessage = { errorMessage = it }
        )
        is DataResultBooking.Failure -> ErrorText2(result.errorMessage)
    }

    if (showLoginPopup) {
        /*PopLoginDialog(
            onDismiss = { showLoginPopup = false },
            onLoginSuccess = {
                selectedReservation?.let { onReservationSelected(it) }
                showLoginPopup = false
                navController.navigate("PaymentSection1/${selectedReservation?.price?.toIntOrNull() ?: 0}")
            }
        )*/
    }

    errorMessage?.let { ErrorText2(it) }
}

@Composable
fun LoadingIndicator2() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}
@Composable
fun BookingList2(
    result: DataResultBooking.Success<List<GetBookingResponseDTO>>,
    filteredTimeSlots: List<TimeSlot>,
    isUserLoggedIn: Boolean,
    navController: NavController,
    onReservationSelected: (ReservationOption, () -> Unit) -> Unit,
    viewModel1: ExtrasViewModel,
    lifecycleOwner: LifecycleOwner,
    isLoading: (Boolean) -> Unit,
    setErrorMessage: (String?) -> Unit
) {
    val establishmentsList = result.data

    if (establishmentsList.isNotEmpty()) {
        LazyColumn {
            items(establishmentsList) { bookingResponse ->
                BookingCard2(
                    bookingResponse = bookingResponse,
                    filteredTimeSlots = filteredTimeSlots,
                    isUserLoggedIn = isUserLoggedIn,
                    navController = navController,
                    onReservationSelected = onReservationSelected,
                    viewModel1 = viewModel1,
                    lifecycleOwner = lifecycleOwner,
                    isLoading = isLoading,
                    setErrorMessage = setErrorMessage
                )
            }
        }
    } else {
        Text("No establishments available", color = Color.Gray)
    }
}


@Composable
fun BookingCard2(
    bookingResponse: GetBookingResponseDTO,
    filteredTimeSlots: List<TimeSlot>,
    isUserLoggedIn: Boolean,
    navController: NavController,
    onReservationSelected: (ReservationOption, () -> Unit) -> Unit,
    viewModel1: ExtrasViewModel,
    lifecycleOwner: LifecycleOwner,
    isLoading: (Boolean) -> Unit,
    setErrorMessage: (String?) -> Unit
) {
    val establishmentName = bookingResponse.establishmentDTO?.name ?: "Unknown"
    val availablePlannings = bookingResponse.plannings.filter { planning ->
        filteredTimeSlots.any { slot -> slot.time == planning.fromStr }
    }

    availablePlannings.forEach { planning ->
        val formattedPrice = "${bookingResponse.amount} ${bookingResponse.currencySymbol}"
        val formattedTimeSlot = "${planning.fromStr} - ${planning.toStr}"

        val reservation = ReservationOption(
            name = establishmentName,
            time = formattedTimeSlot,
            price = formattedPrice,
            date = "90 min"
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .clickable {
                    onReservationSelected(reservation) {
                        bookingResponse.amount?.let {
                            bookingResponse.currencySymbol?.let { it1 ->
                                fetchExtrasAndNavigate2(
                                    navController = navController,
                                    viewModel1 = viewModel1,
                                    lifecycleOwner = lifecycleOwner,
                                    isLoading = isLoading,
                                    setErrorMessage = setErrorMessage,
                                    amount = it,
                                    currencySymbol = it1,
                                    establishmentName = establishmentName,
                                    timeSlot = formattedTimeSlot,
                                    amountSelected = bookingResponse.amount.toString(),
                                    currentSelectedTimeSlot = formattedTimeSlot
                                )
                            }
                        }
                    }
                },
            shape = RoundedCornerShape(8.dp),
            border = BorderStroke(1.dp, Color.Gray)
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                Icon(
                    painter = painterResource(id = R.drawable.logopadelium),
                    contentDescription = "Icon",
                    modifier = Modifier
                        .size(60.dp)
                        .background(Color(0xFF0054D8), shape = CircleShape)
                        .padding(8.dp),
                    tint = Color.Unspecified
                )

                Column(modifier = Modifier.padding(start = 8.dp).weight(1f)) {
                    Text(establishmentName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text(formattedTimeSlot, fontSize = 16.sp, color = Color.Gray)
                }

                Button(
                    onClick = {
                        onReservationSelected(reservation) {
                            bookingResponse.amount?.let {
                                bookingResponse.currencySymbol?.let { it1 ->
                                    fetchExtrasAndNavigate2(
                                        navController = navController,
                                        viewModel1 = viewModel1,
                                        lifecycleOwner = lifecycleOwner,
                                        isLoading = isLoading,
                                        setErrorMessage = setErrorMessage,
                                        amount = it,
                                        currencySymbol = it1,
                                        establishmentName = establishmentName,
                                        timeSlot = formattedTimeSlot,
                                        amountSelected = bookingResponse.amount.toString(),
                                        currentSelectedTimeSlot = formattedTimeSlot
                                    )
                                }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0054D8)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.size(width = 120.dp, height = 40.dp)
                ) {
                    Text(
                        formattedPrice,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color(0xFFD7F057)
                    )
                }
            }
        }
    }
}
private fun fetchExtrasAndNavigate2(
    navController: NavController,
    viewModel1: ExtrasViewModel,
    lifecycleOwner: LifecycleOwner,
    isLoading: (Boolean) -> Unit,
    setErrorMessage: (String?) -> Unit,
    amount: Double,
    currencySymbol: String,
    establishmentName: String,
    timeSlot: String,
    currentSelectedTimeSlot: String,
    amountSelected: String,
) {
    isLoading(true)
    viewModel1.Extras()
    viewModel1.extrasState.observe(lifecycleOwner) { extrasState ->
        when (extrasState) {
            is DataResult.Loading -> isLoading(true)
            is DataResult.Success -> {
                isLoading(false)
                // Pass establishmentName as "Espace" in ReservationSummary    amount
                navController.navigate(
                    "PaymentSection1/${currencySymbol.toString()}/${amount}/${establishmentName}/${timeSlot}/${amountSelected}/${currentSelectedTimeSlot}"
                )
            }
            is DataResult.Failure -> {
                isLoading(false)
                setErrorMessage(extrasState.errorMessage)
            }
        }
    }
}


@Composable
fun ErrorText2(message: String?) {
    Text(message ?: "An unexpected error occurred.", color = Color.Red)
}




