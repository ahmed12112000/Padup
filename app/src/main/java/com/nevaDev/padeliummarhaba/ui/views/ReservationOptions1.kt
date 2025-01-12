package com.nevaDev.padeliummarhaba.ui.views

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nevaDev.padeliummarhaba.models.ReservationOption
import com.nevaDev.padeliummarhaba.viewmodels.ExtrasViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPayAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.TimeSlot
import com.nevadev.padeliummarhaba.R
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.data.repositoriesImp.SaveBookingRequestSerializer
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.PlanningDTO
import com.padelium.domain.dto.SaveBookingRequest
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun TimeSlotSelector(
    timeSlots: List<TimeSlot>,
    onTimeSlotSelected: (String) -> Unit,
    selectedTimeSlot: String?
) {
    val uniqueTimeSlots = timeSlots.distinctBy { it.time } // Deduplicate time slots

    LazyVerticalGrid(
        columns = GridCells.Fixed(4), // Four buttons per row
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(uniqueTimeSlots) { timeSlot ->
            val formattedTime = timeSlot.time.format(DateTimeFormatter.ofPattern("H:mm"))

            Button(
                onClick = { onTimeSlotSelected(formattedTime) },
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0054D8)),

                modifier = Modifier.fillMaxWidth() // Ensures proper spacing
            ) {
                Text(
                    text = formattedTime,
                    color = if (selectedTimeSlot == formattedTime) Color.White else Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}






@Composable
fun ReservationOptions(
    onReservationSelected: (ReservationOption) -> Unit,
    isUserLoggedIn: Boolean,
    key: String?,
    navController: NavController,
    viewModel: GetBookingViewModel = hiltViewModel(),
    viewModel1: ExtrasViewModel = hiltViewModel(),
    selectedDate: LocalDate,
    selectedTimeSlot: String?,
    viewModel2: SaveBookingViewModel = hiltViewModel(),
    bookingViewModel: GetBookingViewModel = hiltViewModel(),
    paymentPayAvoirViewModel : PaymentPayAvoirViewModel

) {
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
    Column(modifier = Modifier.fillMaxSize().offset(y=300.dp)) {
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
                    isUserLoggedIn = isUserLoggedIn,
                    navController = navController,
                    bookingViewModel = bookingViewModel,
                    saveBookingViewModel = saveBookingViewModel,
                    setAmountSelected = setAmountSelected,
                    setCurrencySymbol = setCurrencySymbol,
                    showLoginPopup = showLoginPopup,
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
    isUserLoggedIn: Boolean,
    navController: NavController,
    bookingViewModel: GetBookingViewModel = hiltViewModel(),
    saveBookingViewModel: SaveBookingViewModel,
    setAmountSelected: (Double) -> Unit,
    setCurrencySymbol: (String) -> Unit,
    showLoginPopup: (Boolean) -> Unit,
    viewModel1: ExtrasViewModel = hiltViewModel(),
    selectedTimeSlot: String?,
    paymentPayAvoirViewModel: PaymentPayAvoirViewModel

) {
    // Filter plannings by the selected time slot
    val availablePlannings = getBookingResponseDTO.plannings.filter { planning ->
        planning.fromStr == selectedTimeSlot
    }

    var showLoginPopup by remember { mutableStateOf(false) }

    availablePlannings.forEach { planning ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clickable {
                    handleCardClick(
                        selectedBooking = getBookingResponseDTO,  // Pass entire DTO as the selected booking
                        planning = planning,  // Pass only the current planning that matches the selected time slot
                        bookingViewModel = bookingViewModel,
                        navController = navController,
                        isUserLoggedIn = isUserLoggedIn,
                        onLoginRequired = { showLoginPopup = true },
                        saveBookingViewModel = saveBookingViewModel,
                        viewModel1 = viewModel1,
                        onReservationSelected = { reservationOption ->
                        },
                        paymentPayAvoirViewModel= paymentPayAvoirViewModel

                    )
                },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.Gray),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            EstablishmentCardContent(getBookingResponseDTO, planning)
        }
    }

    if (showLoginPopup) {
        // Handle login popup logic here (e.g., show a dialog or navigate to login screen)
    }
}

@Composable
private fun EstablishmentCardContent(getBookingResponseDTO: GetBookingResponseDTO, planning: PlanningDTO) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logopadelium),
            contentDescription = "Establishment Icon",
            modifier = Modifier
                .size(60.dp)
                .background(Color(0xFF0054D8), shape = CircleShape)
                .padding(8.dp),
            tint = Color.Unspecified
        )

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = getBookingResponseDTO.establishmentDTO?.name ?: "Unknown",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Text(
                text = "${planning.fromStr} - ${planning.toStr}",
                fontSize = 16.sp,
                color = Color.Gray
            )
            Text(
                text = "90 min",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }

        Button(
            onClick = {
                // Button click logic here
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0054D8)),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.size(width = 120.dp, height = 40.dp)
        ) {
            Text(
                text = "${getBookingResponseDTO.amount} ${getBookingResponseDTO.currencySymbol}",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color(0xFFD7F057),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
 fun FailureState(errorMessage: String?, setErrorMessage: (String) -> Unit) {
    setErrorMessage(errorMessage ?: "An unexpected error occurred.")
}




fun handleCardClick(
    selectedBooking: GetBookingResponseDTO, // The entire DTO as the selected booking
    planning: PlanningDTO, // The specific planning clicked
    bookingViewModel: GetBookingViewModel,
    navController: NavController,
    isUserLoggedIn: Boolean,
    onLoginRequired: () -> Unit,
    saveBookingViewModel: SaveBookingViewModel,
    viewModel1: ExtrasViewModel,
    onReservationSelected: (ReservationOption) -> Unit,
     paymentPayAvoirViewModel: PaymentPayAvoirViewModel

) {
    // Extract selectedTimeSlot for logging or further processing


    // Prepare amount and price details
    val amountSelected = selectedBooking.amount ?: BigDecimal.ZERO
    val currencySymbol = selectedBooking.currencySymbol ?: "€"
    val formattedAmount = String.format("%.2f", amountSelected)
    val price = " $formattedAmount"

    // Extract establishment name safely
    val establishmentName = selectedBooking.establishmentDTO?.name ?: "Unknown"
    val selectedTimeSlot = "${planning.fromStr} - ${planning.toStr}"
//      val establishmentName = selectedBooking.establishmentDTO?.name ?: "Unknown"



    // Create a new booking object with only the relevant planning
    val updatedBooking = selectedBooking.copy(plannings = listOf(planning)) // Only include the selected planning

    // Update bookings in the ViewModel with only the current planning
    bookingViewModel.updateBookings(listOf(updatedBooking))  // Ensure this contains only the relevant booking
    Log.d("BookingViewModel", "updateBookings called with selected booking")

    // Call Extras
    viewModel1.Extras()

    // Handle user login state
    if (!isUserLoggedIn) {
        onLoginRequired()
    } else {
        // Save only the selected booking
        val mappedBookings = listOf(updatedBooking).toDomain().joinToString(separator = ", ") {
            it.toString()
        }

      //  saveBookingViewModel.SaveBooking(listOf(updatedBooking).toDomain())

        // Create ReservationOption with corrected values
        val reservationOption = ReservationOption(
            name = establishmentName,
            time = selectedTimeSlot,
            //date = "hello",
            price = amountSelected.toString(),
            mappedBookings = mappedBookings
        )
        Log.d("ReservationSummary", "selectedTimeSlot: $price")
        Log.d("ReservationSummary1111", "selectedTimeSlot: $selectedTimeSlot")



        val mappedBookingsJson = Uri.encode(Gson().toJson(listOf(updatedBooking).toDomain()))
        Log.d("HandleCardClick", "ReservationOption: $reservationOption")

        // Notify about the selected reservation
        onReservationSelected(reservationOption)

        try {
            // Trim and sanitize the price string before conversion
            val sanitizedPrice = price.trim().replace(",", "")
            val amount = BigDecimal(sanitizedPrice)

            paymentPayAvoirViewModel.PaymentPayAvoir(amount)
        } catch (e: NumberFormatException) {
            Toast.makeText(
                navController.context,
                "Invalid price format. Please try again.",
                Toast.LENGTH_LONG
            ).show()
            return
        }


        // Navigate to payment screen with serialized mappedBookings JSON
        navController.navigate(
            "payment_section1/${Uri.encode(reservationOption.name)}/${Uri.encode(reservationOption.time)}/${Uri.encode(reservationOption.price)}/$mappedBookingsJson"
        )

    }
}







fun <T> List<T>.toJson(): String {
    return Gson().toJson(this)
}
//  persollable
// constructor