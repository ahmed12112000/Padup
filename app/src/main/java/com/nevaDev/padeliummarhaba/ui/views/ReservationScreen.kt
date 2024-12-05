package com.nevaDev.padeliummarhaba.ui.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nevaDev.padeliummarhaba.models.ReservationOption
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Button
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nevaDev.padeliummarhaba.viewmodels.GetBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.KeyViewModel
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.FetchKeyRequest
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.nevadev.padeliummarhaba.R
import java.util.Locale
import androidx.compose.ui.text.style.TextOverflow
import com.nevaDev.padeliummarhaba.viewmodels.ExtrasViewModel
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.EstablishmentDTO
import com.padelium.domain.dto.ExtrasRequest
import java.math.BigDecimal


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationScreen(
    navController: NavController,
    isUserLoggedIn: Boolean,
    context: Context,
    sharedPreferences: SharedPreferences,
    onFetchSuccess: () -> Unit,
    viewModel: KeyViewModel = hiltViewModel(),
    getBookingViewModel: GetBookingViewModel = hiltViewModel(),
) {
    val reservationKey = remember { mutableStateOf<String?>(null) }
    var showPaymentSection by remember { mutableStateOf(false) }
    var showLoginPopup by remember { mutableStateOf(false) }
    val selectedReservation = remember { mutableStateOf<ReservationOption?>(null) }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val selectedTimeSlot = remember { mutableStateOf<String?>(null) }

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val filteredTimeSlots by getBookingViewModel.filteredTimeSlots.observeAsState(emptyList())

    LaunchedEffect(filteredTimeSlots) {
        if (filteredTimeSlots.isNotEmpty() && selectedTimeSlot.value == null) {
            selectedTimeSlot.value = " ${filteredTimeSlots.first().time}"
        }
    }

    viewModel.dataResultBooking.observe(lifecycleOwner) { result ->
        when (result) {
            is DataResultBooking.Loading -> isLoading = true
            is DataResultBooking.Success -> {
                reservationKey.value = result.data.key
                isLoading = false
                onFetchSuccess()
            }
            is DataResultBooking.Failure -> {
                isLoading = false
                errorMessage = result.errorMessage ?: "Unknown error occurred"
            }
        }
    }

    LaunchedEffect(selectedDate.value) {
        val fetchKeyRequest = FetchKeyRequest(dateTime = selectedDate.value.format(DateTimeFormatter.ISO_LOCAL_DATE) + " 00:00")
        viewModel.getReservationKey(fetchKeyRequest)
    }

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TabItem(
                isSelected = !showPaymentSection,
                title = "CHOISIR UN CRÉNEAU",
                icon = painterResource(id = R.drawable.calendre),  // Load drawable icon
                onClick = { showPaymentSection = false },
                onBackClick = {

                }
            )




        }

        Spacer(modifier = Modifier.height(8.dp))

        DaySelectorWithArrows(
            selectedDate = selectedDate.value,
            onDateSelected = { newDate ->
                selectedDate.value = newDate
                getBookingViewModel.filterSlotsByDate(newDate)
            },
          //  viewModel = getBookingViewModel
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            errorMessage?.let {
                Text(text = it, color = Color.Red, textAlign = TextAlign.Center)
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredTimeSlots) { timeSlot ->
                TimeSlotButton(
                    slot = " ${timeSlot.time}",
                    isSelected = selectedTimeSlot.value == " ${timeSlot.time}",
                    onClick = { selectedTimeSlot.value = " ${timeSlot.time}" }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (!showPaymentSection) {
            ReservationOptions(
                onReservationSelected = { selectedReservation.value = it },
                isUserLoggedIn = isUserLoggedIn,
                key = reservationKey.value,
                viewModel = getBookingViewModel,
                navController = navController,
                selectedDate = selectedDate.value ,
                selectedTimeSlot = selectedTimeSlot.value

            )
        }
/*
        if (selectedReservation.value != null && selectedTimeSlot.value != null) {
            ReservationSummary(
                selectedDate = selectedDate.value,
                selectedReservation = selectedReservation.value!!,
                selectedTimeSlot = selectedTimeSlot.value!!,
                extrasCost = 0,
                selectedRaquette = "1",
                includeBalls = false,
                onTotalAmountCalculated = { totalAmount ->
                    Log.d("TotalAmount", "Calculated Total Amount: $totalAmount")
                }
            )
        } else if (showLoginPopup) {
            PopLoginDialog(
                onDismiss = { showLoginPopup = false },
                onLoginSuccess = {
                    showLoginPopup = false
                    showPaymentSection = true
                }
            )
        }*/
    }
}

@Composable
fun TimeSlotButton(slot: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) Color(0xFF0054D8) else Color.White,
            contentColor = if (isSelected) Color.White else Color(0xFF0054D8)
        ),
        border = BorderStroke(1.dp, Color(0xFF0054D8)),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.padding(2.dp)
    ) {
        Text(text = slot, fontWeight = FontWeight.Bold)
    }
}


@Composable
fun TabItem(
    isSelected: Boolean,
    title: String,
    icon: Painter,  // Change from ImageVector to Painter
    onClick: () -> Unit,
    onBackClick: () -> Unit // Function to handle back navigation
) {
    Column(
        horizontalAlignment = Alignment.Start,  // Align content to the left
        modifier = Modifier
            .clickable { onClick() }
            .background(
                if (isSelected) Color.White else Color.Transparent,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        // Row for back arrow, icon, and text
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth() // Ensure the row takes full width
        ) {
            // Back arrow icon (clickable)
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,  // Back arrow icon
                    contentDescription = "Back",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Icon
            Icon(
                painter = icon,  // Use painter resource
                contentDescription = null,
                tint = if (isSelected) Color.Black else Color.Gray,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Text
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.Black else Color.Gray,
                textAlign = TextAlign.Start // Align text to the left
            )
        }

        // Blue underline for selected tab (Divider below the text and icon)
        if (isSelected) {
            Divider(
                color = Color(0xFF0054D8),
                thickness = 2.dp,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp) // Ensure space between text and divider
            )
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
    selectedTimeSlot: String? // Rename this as `currentSelectedTimeSlot` for clarity
) {
    var amountSelected by remember { mutableStateOf<Double?>(null) }
    var currentSelectedTimeSlot by remember { mutableStateOf<String?>(null) }

    val dayFormatter = DateTimeFormatter.ofPattern("EEE d MMM")
    val dataResultBooking by viewModel.dataResultBooking.observeAsState(initial = DataResultBooking.Loading)
    val filteredTimeSlots by viewModel.filteredTimeSlots.observeAsState(emptyList())
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showLoginPopup by remember { mutableStateOf(false) }
    var selectedReservation by remember { mutableStateOf<ReservationOption?>(null) }

    var selectedTimeSlot by remember { mutableStateOf<String?>(null) }
    var selectedFromStr by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key) {
        key?.let { viewModel.getBooking(it) }
    }

    viewModel1.dataResult.observe(lifecycleOwner) { result ->
        isLoading = false

        when (result) {
            is DataResult.Loading -> {
                Log.e("TAG", "Loading")
            }
            is DataResult.Success -> {
                Log.e("TAG", "Success")
            }
            is DataResult.Failure -> {
                isLoading = false
                Log.e("TAG", "Failure - Error Code: ${result.exception},${result.errorCode}, Message: ${result.errorMessage}")
            }
        }
    }
    when (val result = dataResultBooking) {
        is DataResultBooking.Loading -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        is DataResultBooking.Success -> {
            val establishmentsList = result.data

            if (establishmentsList.isNotEmpty()) {
                LazyColumn {
                    items(establishmentsList) { getBookingResponseDTO ->

                        val establishmentDTOList = getBookingResponseDTO.EstablishmentDTO
                        val establishmentName = establishmentDTOList.firstOrNull()?.name ?: "Mercedes"

                        // Filter the available plannings based on the selected time slots
                        val availablePlannings = getBookingResponseDTO.plannings.filter { planning ->
                            filteredTimeSlots.any { slot -> slot.time == planning.fromStr }
                        }

                        // White background card with rounded corners
                        availablePlannings.forEach { planning ->
                            // Only proceed if the selected time slot matches
                            if (selectedFromStr == null || selectedFromStr == planning.fromStr) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(6.dp)
                                        .clickable {
                                            Log.d("Debugging", "Selected Planning: $planning")
                                            Log.d("Debugging", "Establishment Name: $establishmentName")

                                            currentSelectedTimeSlot = "${planning.fromStr} - ${planning.toStr}"
                                            amountSelected = getBookingResponseDTO.amount

                                            selectedReservation = ReservationOption(
                                                name = establishmentName,
                                                time = "${planning.fromStr} ${planning.toStr}",
                                                price = "${getBookingResponseDTO.amount} ${getBookingResponseDTO.currencySymbol}",
                                                duration = "90 min"
                                            )
                                            selectedTimeSlot = "${planning.fromStr} ${planning.toStr}"
                                            amountSelected = getBookingResponseDTO.amount

                                            if (!isUserLoggedIn) {
                                                showLoginPopup = true
                                            } else {
                                                selectedReservation?.let {
                                                    onReservationSelected(it)
                                                }
                                                val totalAmount = getBookingResponseDTO.amount
                                                navController.navigate("PaymentSection1/${totalAmount}")
                                            }
                                        },
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, Color.Gray) // Add grey border here
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.White) // White background for the card
                                            .padding(16.dp)
                                    ) {
                                        // Content on top of the white background
                                        Column {
                                            // Establishment name and time range
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(bottom = 8.dp),
                                                horizontalArrangement = Arrangement.Start, // Align items to the start
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
                                                Column(
                                                    modifier = Modifier
                                                        .padding(start = 10.dp)
                                                ) {
                                                    Text(
                                                        text = establishmentName,
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
                                                        text = "90 min", fontSize = 16.sp,
                                                        color = Color.Gray // Duration label
                                                    )
                                                }
                                                // Button and pricing details
                                                Column( modifier = Modifier
                                                    .padding(start = 15.dp),
                                                    horizontalAlignment = Alignment.End
                                                ) {
                                                    Button(
                                                        onClick = {
                                                            // Prepare the ExtrasRequest list
                                                            val extrasRequest = listOf(
                                                                ExtrasRequest(
                                                                    id = "abc".toLongOrNull() ?: 0L,
                                                                    name = establishmentName,
                                                                    code = "SomeCode",
                                                                    description = "Some description",
                                                                    picture = "https://example.com/picture.jpg",
                                                                    amount = BigDecimal("01"),
                                                                    currencyId = 1L,
                                                                    currencyName = 1L,
                                                                    isShared = false
                                                                )
                                                            )

                                                            // Call the Extras function in ExtrasViewModel
                                                            viewModel1.Extras(extrasRequest)

                                                            // Navigate or handle reservation logic
                                                            selectedReservation = ReservationOption(
                                                                name = establishmentName,
                                                                time = "${planning.fromStr} ${planning.toStr}",
                                                                price = "${getBookingResponseDTO.amount} ${getBookingResponseDTO.currencySymbol}",
                                                                duration = "90 min"
                                                            )
                                                            selectedTimeSlot = "${planning.fromStr} ${planning.toStr}"

                                                            if (!isUserLoggedIn) {
                                                                showLoginPopup = true
                                                            } else {
                                                                selectedReservation?.let {
                                                                    onReservationSelected(it)
                                                                }
                                                                val totalAmount = getBookingResponseDTO.amount
                                                                navController.navigate("PaymentSection1/${totalAmount}")
                                                            }
                                                        },
                                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0054D8)),
                                                        shape = RoundedCornerShape(12.dp),
                                                        modifier = Modifier
                                                            .size(width = 120.dp, height = 40.dp)
                                                            .wrapContentWidth()
                                                            .offset(x = 80.dp)
                                                    ) {
                                                        Text(
                                                            text = "${getBookingResponseDTO.amount} ${getBookingResponseDTO.currencySymbol}",
                                                            fontWeight = FontWeight.Bold,
                                                            fontSize = 18.sp,
                                                            color = Color(0xFFD7F057),
                                                            modifier = Modifier.fillMaxWidth(),
                                                            textAlign = TextAlign.Center
                                                        )
                                                    }

                                                    Spacer(modifier = Modifier.height(8.dp))
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Text(text = "No establishments available", color = Color.Gray)
            }
        }
        is DataResultBooking.Failure -> {
            errorMessage = result.errorMessage ?: "An unexpected error occurred."
        }
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

    errorMessage?.let {
        Text(text = it, color = Color.Red)
    }

}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationSummary(
    selectedDate: LocalDate,
    selectedTimeSlot: String,
    selectedReservation: ReservationOption,
    extrasCost: Int,
    selectedExtras: List<Triple<String, String, Int>>,
    selectedRaquette: String,
    includeBalls: Boolean,
    amountSelected: Double?,
    onTotalAmountCalculated: (Int) -> Unit,

) {
    val price = selectedReservation.price.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0
    val totalExtrasCost = selectedExtras.sumOf { (name, priceString, _) ->
        priceString.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0
    }

    val totalAmountSelected = remember {
        (amountSelected?.toInt() ?: 0) + totalExtrasCost.toInt()
    }
    onTotalAmountCalculated(totalAmountSelected)

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
        ReservationDetailRow(label = "Prix", value = "$${String.format("%.2f", amountSelected ?: 0.0)}")
        ReservationDetailRow(
            label = "Date",
            value = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy").format(selectedDate)
        )
        ReservationDetailRow(label = "Heure", value = selectedTimeSlot)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Détails du Prix",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ReservationDetailRow(label = "Prix de Réservation", value = "$${String.format("%.2f", amountSelected ?: 0.0)}")

        selectedExtras.forEach { (name, price, _) ->
            ReservationDetailRow(label = "Extra: $name", value = price)
        }

        // Display summary of extras
        val extrasSummary = selectedExtras.joinToString(separator = ", ") { (name, price, _) ->
            "$name ($price)"
        }
        ReservationDetailRow(label = "Extras", value = extrasSummary)

        ReservationDetailRow(label = "Total", value = "$${totalAmountSelected}")

        onTotalAmountCalculated(totalAmountSelected)
    }
}
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaySelectorWithArrows(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
) {
    val dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.FRENCH) // Abbreviated day name (e.g., "Sam")
    val dateFormatter = DateTimeFormatter.ofPattern("d", Locale.FRENCH) // Day of the month (e.g., "29")
    val monthFormatter = DateTimeFormatter.ofPattern("MMM", Locale.FRENCH) // Abbreviated month name (e.g., "Nov")

    val daysInWeek = (0..6).map { offset ->
        selectedDate.minusDays(selectedDate.dayOfWeek.value.toLong() - 1L).plusDays(offset.toLong())
    }
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH)


    val listState = rememberLazyListState()

    // Automatically scroll to center the selected day
    LaunchedEffect(selectedDate) {
        val selectedIndex = daysInWeek.indexOf(selectedDate)
        if (selectedIndex != -1) {
            // Scroll to the position with an offset to center the selected day
            listState.animateScrollToItem(
                index = selectedIndex,
                scrollOffset = -listState.layoutInfo.viewportEndOffset / 2
            )
        }
    }
    Column(modifier = Modifier.fillMaxWidth()) {
        // Month-Year Header
        Text(
            text = monthYearFormatter.format(selectedDate).uppercase(Locale.FRENCH),
            color = Color(0xFF0054D8),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
    // Navigation Row with Arrows and "AUJOURD'HUI"
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left Arrow
        IconButton(onClick = { onDateSelected(selectedDate.minusDays(1)) }) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Previous day", tint = Color.Gray)
        }

        // "AUJOURD'HUI" Button
        Row(
            modifier = Modifier
                .clickable { onDateSelected(LocalDate.now()) }
                .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "AUJOURD'HUI",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0054D8),
                fontSize = 14.sp,
            )
            Icon(
                Icons.Default.CalendarToday,
                contentDescription = "Today",
                tint = Color(0xFF0054D8),
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(20.dp)
            )
        }

        // Right Arrow
        IconButton(onClick = { onDateSelected(selectedDate.plusDays(1)) }) {
            Icon(Icons.Default.ArrowForward, contentDescription = "Next day", tint = Color.Gray)
        }
    } }
    LazyRow(
        state = listState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(0.dp) // No space between boxes, add gray line manually
    ) {
        items(daysInWeek) { day ->
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Box for each day with reduced size
                Column(
                    modifier = Modifier
                        .clickable { onDateSelected(day) }
                        .background(
                            color = if (day == selectedDate) Color(0xFF0054D8) else Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = if (day == selectedDate) 0.dp else 1.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 8.dp, horizontal = 8.dp) // Reduced padding
                        .width(60.dp),  // Smaller width
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Abbreviated day name (e.g., "Sam")
                    Text(
                        text = dayFormatter.format(day).uppercase(),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = if (day == selectedDate) Color.White else Color.Gray,
                        fontSize = 12.sp  // Smaller font size for day name
                    )

                    // Day of the month (e.g., "29")
                    Text(
                        text = dateFormatter.format(day),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = if (day == selectedDate) Color.White else Color.Black,
                        fontSize = 18.sp  // Smaller font size for day number
                    )

                    // Abbreviated month name (e.g., "Nov")
                    Text(
                        text = monthFormatter.format(day).uppercase(),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        color = if (day == selectedDate) Color.White else Color.Gray,
                        fontSize = 12.sp  // Smaller font size for month
                    )
                }

                // Gray line separator (except after the last item)
                if (day != daysInWeek.last()) {
                    Box(
                        modifier = Modifier
                            .height(40.dp) // Match the reduced height of the day box
                            .width(1.dp)
                            .background(Color.Gray)
                    )
                }
            }
        }
    }
}











/*
@Composable
fun PopLoginDialog(onDismiss: () -> Unit, onLoginSuccess: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .size(1000.dp)
                .padding(vertical = 100.dp),
            shape = RoundedCornerShape(16.dp)
        ) { /*
            LoginScreen(onLoginSuccess = {
                onLoginSuccess()
                onDismiss()
                navController = navController

                viewModel = hiltViewModel(), */
            })
        }
    }
}

*/



/*
@Preview(showBackground = true)
@Composable
fun ReservationScreenPreview() {
    val navController = rememberNavController()

    val context = LocalContext.current
    val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    val onFetchSuccess: () -> Unit = {
        Log.d("ReservationPreview", "Fetch Success!")
    }

    ReservationScreen(
        navController = navController,
        isUserLoggedIn = true,
        context = context,
        sharedPreferences = sharedPreferences,
        onFetchSuccess = onFetchSuccess
    )
}
*/

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 400, heightDp = 800)
@Composable
fun ReservationScreenPreview() {
    // Mock data to simulate context and shared preferences
    val mockContext = LocalContext.current
    val mockSharedPreferences = mockContext.getSharedPreferences("MockPrefs", Context.MODE_PRIVATE)

    // Preview for the ReservationScreen
    ReservationScreen(
        navController = rememberNavController(),
        isUserLoggedIn = true,
        context = mockContext,
        sharedPreferences = mockSharedPreferences,
        onFetchSuccess = {
            Log.d("ReservationScreenPreview", "Fetch successful")
        },
        viewModel = hiltViewModel<KeyViewModel>(), // You might need to provide a mocked ViewModel for testing
        getBookingViewModel = hiltViewModel<GetBookingViewModel>() // Same for this ViewModel
    )
}

@Preview(showBackground = true)
@Composable
fun TimeSlotButtonPreview() {
    TimeSlotButton(
        slot = "10:00 AM",
        isSelected = true,
        onClick = {
            Log.d("TimeSlotButtonPreview", "Time slot selected")
        }
    )
}

@Preview(showBackground = true)
@Composable
fun TabItemPreview() {
    Row {
        // TabItem with a selected state and a calendar icon
        TabItem(
            isSelected = true,
            title = "CHOISIR UN CRÉNEAU",
            icon = painterResource(id = R.drawable.calendre),  // Replace with your actual drawable resource
            onClick = {
                Log.d("TabItemPreview", "Tab clicked")
            },
            onBackClick = {
                Log.d("TabItemPreview", "Back arrow clicked")
                // Handle back navigation logic here
            }
        )

        Spacer(modifier = Modifier.width(16.dp))


    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, widthDp = 400, heightDp = 150)
@Composable
fun PreviewDaySelectorWithArrows() {
    // Define a sample selected date
    val today = LocalDate.of(2024, 11, 29) // Example: 29th November 2024

    // Preview the composable with dummy onDateSelected logic
    DaySelectorWithArrows(
        selectedDate = today,
        onDateSelected = { selectedDay ->
            println("Selected date: $selectedDay") // Debug action for the preview
        }
    )
}
@Preview(showBackground = true)
@Composable
fun ReservationOptionsPreview() {
    // Mock data


    // Mock ViewModel


    // NavController Mock (using rememberNavController for preview)
    val navController = rememberNavController()

    // Preview of the composable
    ReservationOptions(
        onReservationSelected = {},
        isUserLoggedIn = true,
        key = "dummyKey",
        navController = navController,
        selectedDate = LocalDate.now(),
        selectedTimeSlot = "12:00"
    )
}






