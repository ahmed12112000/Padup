package com.nevaDev.padeliummarhaba.ui.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.nevaDev.padeliummarhaba.viewmodels.ReservationViewModel

import com.nevaDev.padeliummarhaba.models.EstablishmentDTO
import com.nevaDev.padeliummarhaba.models.ReservationOption
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.launch
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationScreen(
    navController: NavController,
    isUserLoggedIn: Boolean,
    context: Context,
    sharedPreferences: SharedPreferences,
    reservationViewModel: ReservationViewModel = viewModel()
) {
    val reservationKey = remember { mutableStateOf<String?>(null) }
    var showPaymentSection by remember { mutableStateOf(false) }
    var showLoginPopup by remember { mutableStateOf(false) }
    val selectedReservation = remember { mutableStateOf<ReservationOption?>(null) }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    val coroutineScope = rememberCoroutineScope()
    val establishments = remember { mutableStateOf<List<EstablishmentDTO>>(emptyList()) }
    val combinedEstablishments = mutableListOf<Pair<String, Any>>()
    val errorMessage1 by reservationViewModel.errorMessage1.collectAsState()







    // Mutable state for available time slots
    val availableTimeSlots = remember { mutableStateOf(listOf<String>()) }
    var selectedTimeSlot by remember { mutableStateOf("") }

    // Fetch reservation key and establishments on date change
    LaunchedEffect(selectedDate.value) {
        // Fetch the reservation key based on the selected date
        reservationViewModel.fetchReservationKeyAndFollowUp(selectedDate.value)

        // Call fetchGetBookingList to fetch available time slots
        val key = reservationViewModel.reservationKey.value
        if (!key.isNullOrEmpty()) {
            coroutineScope.launch {
                reservationViewModel.fetchGetBookingList(key) { bookings, error ->
                    if (error != null) {
                        reservationViewModel.errorMessage.value = error // Update error message
                    } else {
                        bookings?.let {
                            availableTimeSlots.value = it.map { planning -> planning.fromStr }
                        }
                    }
                }
            }
        }
    }

//Error:java.lang.illegalStateExeption:Expected Begin_ARRAY but wan BEGIN_OBJECT at line 1 column 23 $[0].establishmentDTO

    // Listen for changes in the view model's available time slots
    LaunchedEffect(reservationViewModel.availableTimeSlots) {
        availableTimeSlots.value = reservationViewModel.availableTimeSlots.value // Update the state
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(rememberScrollState(), Orientation.Vertical)
    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            //.verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (reservationViewModel.isLoading.value) {
            CircularProgressIndicator()
        } else {
            errorMessage1.takeIf { it.isNotEmpty() }?.let { error ->
                Text(text = error, color = Color.Red, textAlign = TextAlign.Center)
            }




            Button(onClick = {
                coroutineScope.launch {
                    reservationViewModel.fetchReservationKeyAndFollowUp(selectedDate.value)
                }
            }) {
                Text(text = "CHOISIR UN CRÉNEAU")
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        // Date selector
        if (!showPaymentSection) {
            DaySelectorWithArrows(selectedDate.value) { newDate ->
                selectedDate.value = newDate
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Time Slot Selector
            TimeSlotSelector(
                timeSlots = availableTimeSlots.value, // Pass the dynamically fetched time slots
                onTimeSlotSelected = { selectedSlot ->
                    selectedTimeSlot = selectedSlot // Store the selected slot in a local variable
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))





/*
// Add establishments
        reservationViewModel.establishments.value.forEach { establishment ->
            combinedEstablishments.add(
                Pair(establishment.name, establishment)
            )
        }

// Add planning
        reservationViewModel.establishments1.value.forEach { planning ->
            combinedEstablishments.add(
                Pair("Planning Item", planning) // Title for planning items
            )
        }

        LazyColumn {
            items(combinedEstablishments) { (title, item) ->
                when (item) {
                    is EstablishmentDTO -> {
                        // Display EstablishmentDTO details
                        Text(text = title, fontWeight = FontWeight.Bold)
                        Text(text = "${item.amount} ${item.currencySymbol}", color = Color.Gray)

                    }
                    is PlanningDTO -> {
                        // Display PlanningDTO details
                        Text(text = title, fontWeight = FontWeight.Bold) // Title for planning item
                        Text(
                            text = "${item.fromStr} - ${item.toStr}",
                            color = Color.Gray
                        )
                    }
                }
            }
        }
// select time slots....time inclue in plannings......build function...map in planninds object booking each one...
*/
/*
        val establishments3 by reservationViewModel.establishments3.collectAsState()  // Collect the state from ViewModel

        if (establishments3.isNotEmpty()) {
            LazyColumn {
                items(establishments3) { booking: PlanningDTO ->  // Explicitly specify the type of items
                    // Display booking information
                    Text(
                        text = "${booking.fromStr} - ${booking.toStr} | Price: ${booking.price} ${booking.currencySymbol}",
                        color = Color.Gray
                    )
                }
            }
        } else {
            // Optionally handle the case when there are no bookings
            Text(text = "No bookings available", color = Color.Gray)
        }

        val establishmentsList by reservationViewModel.establishments.collectAsState()
        if (establishmentsList.isNotEmpty()) {
            LazyColumn {
                items(establishmentsList) { establishment: EstablishmentDTO ->  // Specify the type explicitly
                    Text(text = establishment.name, fontWeight = FontWeight.Bold)
                    Text(text = "${establishment.amount} ${establishment.currencySymbol}", color = Color.Gray)
                }
            }

        }
*/
        /*
                reservationViewModel.establishments1.value.takeIf { it.isNotEmpty() }?.let { establishments ->
                    LazyColumn {
                        items(establishments) { establishment ->
                            // Loop through each Establishment and display its name with related planning details
                            establishment.Establishment.forEach { est ->
                                Text(text = est.name, fontWeight = FontWeight.Bold)

                                // Display each planning time range specific to this Establishment name
                               establishment.plannings.forEach { planning ->
                                    Text(
                                        text = "Planning for ${est.name}: ${planning.fromStr} - ${planning.toStr}",
                                        color = Color.Gray
                                    )
                                }
                            }

                            // Display other establishment details
                            Text(text = "${establishment.amount} ${establishment.currencySymbol}", color = Color.Gray)
                            Text(text = establishment.description, color = Color.Gray)


                        }
                    }
                }*/






        if (!showPaymentSection) {
            Spacer(modifier = Modifier.height(16.dp))

            // Reservation Options
            ReservationOptions(
                onReservationSelected = { reservation ->
                    selectedReservation.value = reservation
                    if (!isUserLoggedIn) {
                        showLoginPopup = true
                    } else {
                        showPaymentSection = true
                    }
                },
                isUserLoggedIn = isUserLoggedIn,
                key = reservationKey.value // Pass the key here if `reservationKey` holds it
            )


            selectedReservation.value?.let { reservation ->
                ReservationSummary(
                    selectedDate = selectedDate.value,
                    selectedTimeSlot = selectedTimeSlot, // Use the selected time slot
                    selectedReservation = reservation,
                    extrasCost = 0,
                    selectedRaquette = "1",
                    includeBalls = false,
                    onTotalAmountCalculated = { totalAmount ->
                        Log.d("TotalAmount", "Calculated Total Amount: $totalAmount")
                    }
                )
            }
        }

        if (showLoginPopup) {
            PopLoginDialog(
                onDismiss = { showLoginPopup = false },
                onLoginSuccess = {
                    showLoginPopup = false
                    showPaymentSection = true
                }
            )
        }
    }
}}



@Composable
fun TimeSlotSelector(
    timeSlots: List<String>, // Pass the fetched time slots here
    onTimeSlotSelected: (String) -> Unit
) {
    var selectedTime by remember { mutableStateOf("") }

    Column {
        Text(
            text = "Select a Time Slot",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.horizontalScroll(rememberScrollState())) {
            timeSlots.forEach { time ->
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .clickable {
                            selectedTime = time
                            onTimeSlotSelected(time) // Notify selected time
                        }
                        .background(
                            color = if (time == selectedTime) Color.Blue else Color.LightGray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = time,
                        color = if (time == selectedTime) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun ReservationOptions(
    onReservationSelected: (ReservationOption) -> Unit,
    isUserLoggedIn: Boolean,
    key: String?,
    viewModel: ReservationViewModel = viewModel()
) {
    // Collect the establishments list from the ViewModel
    val establishmentsList by viewModel.establishments.collectAsState()
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showLoginPopup by remember { mutableStateOf(false) }
    var selectedReservation by remember { mutableStateOf<ReservationOption?>(null) }

    // Fetch establishments and bookings on initial composition or when the key changes
    LaunchedEffect(key) {
        viewModel.fetchAvailableEstablishments(key)
        viewModel.loadBookings(key)
    }

    Column  {
        // Display error message if present
        if (errorMessage != null) {
            Text(text = errorMessage!!, color = Color.Red)
        } else {
            // Display establishments if they are not empty
            if (establishmentsList.isNotEmpty()) {
                LazyColumn {
                    items(establishmentsList) { establishment: EstablishmentDTO -> // Specify the type explicitly
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    selectedReservation = ReservationOption(
                                        name = establishment.name,
                                        time = "12:30H - 14:00H", // Example data
                                        price = "${establishment.amount} ${establishment.currencySymbol}", // Updated to use the establishment's data
                                        duration = "90 min" // Example data
                                    )
                                    if (!isUserLoggedIn) {
                                        showLoginPopup = true
                                    } else {
                                        selectedReservation?.let { onReservationSelected(it) }
                                    }
                                },
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(text = establishment.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text(text = "12:30H - 14:00H", fontSize = 14.sp) // Example data
                                    Text(text = "90 min", fontSize = 12.sp) // Example data
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(text = "${establishment.amount} ${establishment.currencySymbol}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Blue)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Button(
                                        onClick = {
                                            selectedReservation = ReservationOption(
                                                name = establishment.name,
                                                time = "12:30H - 14:00H",
                                                price = "${establishment.amount} ${establishment.currencySymbol}",
                                                duration = "90 min"
                                            )
                                            if (!isUserLoggedIn) {
                                                showLoginPopup = true
                                            } else {
                                                selectedReservation?.let { onReservationSelected(it) }
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color.DarkGray),
                                        modifier = Modifier.size(width = 80.dp, height = 30.dp)
                                    ) {
                                        Text(text = "Réserver", fontSize = 12.sp)
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // Optionally handle the case when there are no establishments
                Text(text = "No establishments available", color = Color.Gray)
            }
        }

        if (showLoginPopup) {
            PopLoginDialog(
                onDismiss = { showLoginPopup = false },
                onLoginSuccess = {
                    selectedReservation?.let { onReservationSelected(it) }
                    showLoginPopup = false
                }
            )
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaySelectorWithArrows(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val dayFormatter = DateTimeFormatter.ofPattern("EEE d MMM")
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy")

    // Lazy list state to control the scroll position
    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxWidth()) {
        // Top row with arrows for navigating weeks and showing current month/year
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "SEMAINE PRÉCÉDENTE",
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onDateSelected(selectedDate.minusWeeks(1)) } // Click to navigate previous week
            )

            Text(
                text = monthYearFormatter.format(selectedDate),
                color = Color.Blue,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Text(
                text = "SEMAINE SUIVANTE",
                color = Color.Gray,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onDateSelected(selectedDate.plusWeeks(1)) } // Click to navigate next week
            )
        }

        // Arrow buttons and "Today" button in the middle
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onDateSelected(selectedDate.minusWeeks(1)) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous week", tint = Color.Gray)
            }

            IconButton(onClick = { onDateSelected(selectedDate.minusDays(1)) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous day", tint = Color.Gray)
            }

            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .clickable { onDateSelected(LocalDate.now()) }
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "AUJOURD'HUI",
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = "Today",
                    tint = Color.Blue,
                    modifier = Modifier.size(24.dp)
                )
            }

            IconButton(onClick = { onDateSelected(selectedDate.plusDays(1)) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next day", tint = Color.Gray)
            }

            IconButton(onClick = { onDateSelected(selectedDate.plusWeeks(1)) }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next week", tint = Color.Gray)
            }
        }

        // Smooth horizontal scrolling days of the week
        LazyRow(
            state = listState,  // Using the list state to control scroll position
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(7) { i ->
                val day = selectedDate.minusDays(selectedDate.dayOfWeek.value.toLong() - i.toLong())
                Column(
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onDateSelected(day) },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = dayFormatter.format(day),
                        textAlign = TextAlign.Center,
                        color = if (day == selectedDate) Color.White else Color.Gray,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .background(
                                if (day == selectedDate) Color.Blue else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(8.dp)
                    )
                }
            }
        }

        // Scroll to the middle position when the selected day changes
        LaunchedEffect(selectedDate) {
            val middleIndex = 3 // Middle of the 7-day list
            listState.scrollToItem(middleIndex)
        }
    }
}



@Composable
fun PopLoginDialog(onDismiss: () -> Unit, onLoginSuccess: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .size(1000.dp)
                .padding(vertical = 100.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            LoginScreen(onLoginSuccess = {
                // Dismiss the dialog after login success
                onLoginSuccess() // Call the onLoginSuccess callback passed from ReservationScreen
                onDismiss()
            })
        }
    }
}






@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationSummary(
    selectedDate: LocalDate,
    selectedTimeSlot: String,
    selectedReservation: ReservationOption,
    extrasCost: Int,
    selectedRaquette: String,
    includeBalls: Boolean,
    onTotalAmountCalculated: (Int) -> Unit // Callback to pass total amount
) {
    // Function to calculate total amount based on reservation price and extras
    fun calculateTotalAmount(): Int {
        val priceString = selectedReservation.price.replace("[^\\d]".toRegex(), "")
        val price = priceString.toIntOrNull() ?: 0 // Default to 0 if parsing fails
        return price + extrasCost // Calculate total amount
    }

    val totalAmount = calculateTotalAmount() // Calculate total amount

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Détails réservation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        HorizontalDivider(
            modifier = Modifier
                .width(900.dp)
                .padding(horizontal = 10.dp)
                .offset(y = -10.dp),
            color = Color.Gray,
            thickness = 1.dp
        )

        ReservationDetailRow(label = "Espace", value = selectedReservation.name)
        ReservationDetailRow(label = "Prix", value = selectedReservation.price)
        ReservationDetailRow(
            label = "Date",
            value = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy").format(selectedDate)
        )
        ReservationDetailRow(label = "Heure", value = selectedTimeSlot)

        Spacer(modifier = Modifier.height(16.dp))

        // Price breakdown
        Text(
            text = "Détails du Prix",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ReservationDetailRow(label = "Prix de Réservation", value = selectedReservation.price)

        // Include details of extras if any are added
        val extrasDetails = buildString {
            if (includeBalls) append("3 New Balls + ")
            append("${selectedRaquette} Raquettes")
        }
        ReservationDetailRow(label = "Extras", value = extrasDetails)

        // Display total amount
        ReservationDetailRow(label = "Total", value = "$totalAmount")

        // Call the callback to pass the total amount
        onTotalAmountCalculated(totalAmount)
    }
}





@Composable
fun ReservationDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold)
        Text(text = value, fontWeight = FontWeight.Medium)
    }
}

/*
@Preview(showBackground = true)
@Composable
fun ReservationScreenPreview() {
    @Composable
    fun ReservationScreen(
        navController: NavController,
        key: String,
        date: String,
        isUserLoggedIn: Boolean,
        rawResponse: String // New parameter to accept the raw API response
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Reservation Key: $key", fontSize = 20.sp)
            Text(text = "Reservation Date: $date", fontSize = 20.sp)
            Text(text = "Raw API Response: $rawResponse", fontSize = 14.sp) // Display raw response
        }
    }

}*/
/*
@Preview(showBackground = true)
@Composable
fun PaymentSectionPreview() {
    // Mock data to use in the preview
    val mockSelectedDate = LocalDate.now()
    val mockSelectedTimeSlot = "10:00 AM - 11:00 AM"
    val mockSelectedReservation = ReservationOption(
        name = "Tennis Court",
        price = 50.toString(),
        time = "10:00 AM", // Provide a mock value for time
        duration = "1 hour" // Provide a mock value for duration
    )

    PaymentSection(
        selectedDate = mockSelectedDate,
        selectedTimeSlot = mockSelectedTimeSlot,
        selectedReservation = mockSelectedReservation,
        onExtrasUpdate = { extrasCost, selectedRaquette, includeBalls ->
            // Handle extras update in the preview
        },
        onPayWithCardClick = {
            // Handle pay with card click in the preview
        }
    )
}*/


//i want to devise this file into files corresponding to the MVVM methode to make the work smoother and better

