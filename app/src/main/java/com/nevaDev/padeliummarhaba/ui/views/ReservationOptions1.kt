package com.nevaDev.padeliummarhaba.ui.views

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color.RED
import android.graphics.Color.WHITE
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.gson.Gson
import com.nevaDev.padeliummarhaba.models.ReservationOption
import com.nevaDev.padeliummarhaba.viewmodels.ExtrasViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPayAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.TimeSlot
import com.nevadev.padeliummarhaba.R
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.LoginRequest
import com.padelium.domain.dto.PlanningDTO
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.google.android.material.snackbar.Snackbar
import android.view.View
import android.widget.TextView
import com.android.identity.util.AndroidAttestationExtensionParser
import com.nevaDev.padeliummarhaba.di.SessionManager
import com.nevaDev.padeliummarhaba.ui.activities.LoginActivity
import com.nevaDev.padeliummarhaba.ui.activities.SharedViewModel

@Composable
fun TimeSlotSelector(
    timeSlots: List<TimeSlot>,
    onTimeSlotSelected: (String) -> Unit,
    selectedTimeSlot: String?
) {
    val uniqueTimeSlots = timeSlots.distinctBy { it.time }

    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(uniqueTimeSlots) { timeSlot ->
            val formattedTime = timeSlot.time.format(DateTimeFormatter.ofPattern("H:mm"))
            val isSelected = selectedTimeSlot == formattedTime

            Button(
                onClick = { onTimeSlotSelected(formattedTime) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) Color(0xFF0054D8) else Color.White // White background if not selected
                ),
                border = if (!isSelected) {
                    BorderStroke(1.dp, Color.Black) // Black border if not selected
                } else {
                    null // No border if selected
                },
                modifier = Modifier
                    .size(30.dp) // Reduced size for button
            ) {
                Text(
                    text = formattedTime,
                    color = if (selectedTimeSlot == formattedTime) Color.White else Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp // Reduced font size
                )
            }
        }
    }
}







@Composable
fun ReservationOptions(
    onReservationSelected: (ReservationOption) -> Unit,
    // isUserLoggedIn: Boolean,
    key: String?,
    navController: NavController,
    viewModel: GetBookingViewModel = hiltViewModel(),
    viewModel1: ExtrasViewModel = hiltViewModel(),
    selectedDate: LocalDate,
    selectedTimeSlot: String?,
    viewModel2: SaveBookingViewModel = hiltViewModel(),
    bookingViewModel: GetBookingViewModel = hiltViewModel(),
    paymentPayAvoirViewModel : PaymentPayAvoirViewModel,
    sharedViewModel: SharedViewModel

) {
    val isUserLoggedIn by sharedViewModel.isLoggedIn.observeAsState(false)

    var amountSelected by remember { mutableStateOf<Double?>(null) }
    var currencySymbol by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showLoginPopup by remember { mutableStateOf(false) }

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val dataResultBooking by viewModel.dataResultBooking.observeAsState(initial = DataResultBooking.Loading)
    val filteredTimeSlots by viewModel.filteredTimeSlots.observeAsState(emptyList())
    var selectedTimeSlot by remember { mutableStateOf<String?>(null) }


    when (val result = dataResultBooking) {
        is DataResultBooking.Loading -> LoadingState()
        is DataResultBooking.Success -> SuccessState(
            establishmentsList = result.data,
            filteredTimeSlots = filteredTimeSlots.distinctBy { it.time }, // Ensure uniqueness
            isUserLoggedIn = isUserLoggedIn,
            navController = navController,
            bookingViewModel = bookingViewModel,
            saveBookingViewModel = viewModel2,
            onReservationSelected = onReservationSelected,
            setAmountSelected = { amountSelected = it },
            setCurrencySymbol = { currencySymbol = it },
            showLoginPopup = { showLoginPopup = it },
            selectedTimeSlot = selectedTimeSlot,
            onTimeSlotSelected = { selectedTimeSlot = it },
            paymentPayAvoirViewModel= paymentPayAvoirViewModel

        )
        is DataResultBooking.Failure -> FailureState(result.errorMessage, setErrorMessage = { errorMessage = it })
    }

    if (showLoginPopup) {
        // Handle login popup logic
    }

    errorMessage?.let {
        Text(text = it, color = Color.Red, modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}
@Composable
fun SuccessState(
    establishmentsList: List<GetBookingResponseDTO>,
    filteredTimeSlots: List<TimeSlot>,
    onTimeSlotSelected: (String) -> Unit,
    selectedTimeSlot: String?,
    isUserLoggedIn: Boolean,
    navController: NavController,
    bookingViewModel: GetBookingViewModel = hiltViewModel(),
    saveBookingViewModel: SaveBookingViewModel,
    onReservationSelected: (ReservationOption) -> Unit,
    setAmountSelected: (Double) -> Unit,
    setCurrencySymbol: (String) -> Unit,
    showLoginPopup: (Boolean) -> Unit,
    paymentPayAvoirViewModel: PaymentPayAvoirViewModel

) {
    Column(modifier = Modifier
        .fillMaxSize()
        .offset(y=280.dp)
    ) {
        // Display TimeSlotSelector with deduplicated filteredTimeSlots
        TimeSlotSelector(
            timeSlots = filteredTimeSlots,
            onTimeSlotSelected = onTimeSlotSelected,
            selectedTimeSlot = selectedTimeSlot
        )

        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            items(establishmentsList) { getBookingResponseDTO ->
                EstablishmentCard(
                    getBookingResponseDTO = getBookingResponseDTO,
                    filteredTimeSlots = filteredTimeSlots,
                    navController = navController,
                    bookingViewModel = bookingViewModel,
                    saveBookingViewModel = saveBookingViewModel,
                    setAmountSelected = setAmountSelected,
                    setCurrencySymbol = setCurrencySymbol,
                    selectedTimeSlot = selectedTimeSlot,
                    paymentPayAvoirViewModel= paymentPayAvoirViewModel

                )
            }
        }
    }
}


@Composable
private fun EstablishmentCard(
    getBookingResponseDTO: GetBookingResponseDTO,
    filteredTimeSlots: List<TimeSlot>,
    navController: NavController,
    bookingViewModel: GetBookingViewModel = hiltViewModel(),
    saveBookingViewModel: SaveBookingViewModel,
    setAmountSelected: (Double) -> Unit,
    setCurrencySymbol: (String) -> Unit,
    viewModel1: ExtrasViewModel = hiltViewModel(),
    selectedTimeSlot: String?,
    paymentPayAvoirViewModel: PaymentPayAvoirViewModel,
    sharedViewModel: SharedViewModel = hiltViewModel() // Access SharedViewModel
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) } // ✅ Create SessionManager instance

    // Filter plannings by the selected time slot
    val availablePlannings = getBookingResponseDTO.plannings.filter { planning ->
        planning.fromStr == selectedTimeSlot
    }

    var showLoginPopup by remember { mutableStateOf(false) }

    availablePlannings.forEach { planning ->
        val amountToShow = if (planning.reductionPrice != null && planning.reductionPrice != BigDecimal.ZERO) {
            planning.reductionPrice
        } else {
            getBookingResponseDTO.amount ?: BigDecimal.ZERO
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp) // Reduced padding
                .clickable {
                    // Pass the login state to handleCardClick
                    handleCardClick(
                        selectedBooking = getBookingResponseDTO,
                        planning = planning,
                        bookingViewModel = bookingViewModel,
                        navController = navController,
                        onLoginRequired = { showLoginPopup = true },
                        saveBookingViewModel = saveBookingViewModel,
                        viewModel1 = viewModel1,
                        onReservationSelected = { reservationOption -> },
                        paymentPayAvoirViewModel = paymentPayAvoirViewModel,
                        amountToShow = amountToShow,
                        context = context,
                        sessionManager = sessionManager // ✅ Pass SessionManager
                    )
                },
            shape = RoundedCornerShape(10.dp), // Slightly smaller radius
            border = BorderStroke(1.dp, Color.Gray),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            EstablishmentCardContent(getBookingResponseDTO, planning)
        }
    }



    /*
    if (showLoginPopup) {
        Dialog(onDismissRequest = { showLoginPopup = false }) {
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color.White,
                modifier = Modifier.padding(16.dp)
            ) {
                LoginScreen(
                    onLoginSuccess = {
                        showLoginPopup = false // Hide the popup on successful login
                        bookingViewModel.updateLoginState(true) // Update the login state
                    },
                    navController = navController,
                    loginRequest = LoginRequest(username = "", password = "") // Initialize with empty values or your logic
                )
            }
        }
    }

     */
}


@Composable
private fun EstablishmentCardContent(getBookingResponseDTO: GetBookingResponseDTO, planning: PlanningDTO) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(6.dp), // Reduced padding for more compact layout
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logopadelium),
            contentDescription = "Establishment Icon",
            modifier = Modifier
                .size(48.dp) // Smaller icon size
                .background(Color(0xFF0054D8), shape = CircleShape)
                .padding(4.dp), // Reduced padding
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(12.dp)) // Reduced space between icon and text

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = getBookingResponseDTO.establishmentDTO?.name ?: "Unknown",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp, // Smaller font size
                color = Color.Black
            )
            Text(
                text = "${planning.fromStr} - ${planning.toStr}",
                fontSize = 12.sp, // Smaller font size
                color = Color.Gray
            )
            Text(
                text = "90 min",
                fontSize = 12.sp, // Smaller font size
                color = Color.Gray
            )
        }

        Column(
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            val amountToShow = getBookingResponseDTO.amount ?: BigDecimal.ZERO

            if (planning.reductionPrice != null && planning.reductionPrice != BigDecimal.ZERO) {
                Box(modifier = Modifier.padding(bottom = 4.dp)) {
                    Text(
                        text = "${String.format("%.2f", amountToShow)} DT",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp, // Smaller font size
                        color = Color(0xFF0054D8),
                        textAlign = TextAlign.Center
                    )
                    Canvas(
                        modifier = Modifier
                            .matchParentSize()
                    ) {
                        val textHeight = size.height / 2
                        drawLine(
                            color = Color.Red,
                            strokeWidth = 6f,
                            start = Offset(0f, textHeight),
                            end = Offset(size.width, textHeight)
                        )
                    }
                }

                Text(
                    text = "${String.format("%.2f", planning.reductionPrice)} DT",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp, // Smaller font size
                    color = Color.Red, // Discounted price in Red
                    textAlign = TextAlign.Center
                )
            } else {
                Text(
                    text = "${String.format("%.2f", amountToShow)} DT",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp, // Smaller font size
                    color = Color(0xFF0054D8),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}




@Composable
fun FailureState(errorMessage: String?, setErrorMessage: (String) -> Unit) {
    setErrorMessage(errorMessage ?: "An unexpected error occurred.")
}


fun handleCardClick(
    selectedBooking: GetBookingResponseDTO,
    planning: PlanningDTO,
    bookingViewModel: GetBookingViewModel,
    navController: NavController,
    onLoginRequired: () -> Unit,
    saveBookingViewModel: SaveBookingViewModel,
    viewModel1: ExtrasViewModel,
    onReservationSelected: (ReservationOption) -> Unit,
    paymentPayAvoirViewModel: PaymentPayAvoirViewModel,
    amountToShow: BigDecimal,
    context: Context,
    sessionManager: SessionManager, // ✅ Inject SessionManager to check login status
) {
    val currencySymbol = selectedBooking.currencySymbol ?: "€"
    val formattedAmount = String.format("%.2f", amountToShow)
    val price = " $formattedAmount"

    val establishmentName = selectedBooking.establishmentDTO?.name ?: "Unknown"
    val selectedTimeSlot = "${planning.fromStr} - ${planning.toStr}"

    val updatedBooking = selectedBooking.copy(plannings = listOf(planning))

    bookingViewModel.updateBookings(listOf(updatedBooking))
    Log.d("BookingViewModel", "updateBookings called with selected booking")

    if (!sessionManager.isLoggedIn()) { // ✅ Check token-based login status
        // Redirect to LoginActivity and pass the destination
        val intent = Intent(context, LoginActivity::class.java).apply {
            putExtra(
                "destination_route",
                "payment_section1/${Uri.encode(establishmentName)}/${Uri.encode(selectedTimeSlot)}/${Uri.encode(price)}/${Uri.encode(Gson().toJson(listOf(updatedBooking).toDomain()))}"
            )
        }
        context.startActivity(intent)
    } else {
        // Proceed with booking
        val mappedBookingsJson = Uri.encode(Gson().toJson(listOf(updatedBooking).toDomain()))

        val reservationOption = ReservationOption(
            name = establishmentName,
            time = selectedTimeSlot,
            price = amountToShow.toString(),
            mappedBookings = mappedBookingsJson
        )

        Log.d("HandleCardClick", "ReservationOption: $reservationOption")
        onReservationSelected(reservationOption)

        try {
            val sanitizedPrice = price.trim().replace(",", "")
            BigDecimal(sanitizedPrice)
        } catch (e: NumberFormatException) {
            Toast.makeText(context, "Invalid price format. Please try again.", Toast.LENGTH_LONG).show()
            return
        }

        navController.navigate(
            "payment_section1/${Uri.encode(reservationOption.name)}/${Uri.encode(reservationOption.time)}/${Uri.encode(reservationOption.price)}/$mappedBookingsJson"
        )
    }
}






fun <T> List<T>.toJson(): String {
    return Gson().toJson(this)
}
