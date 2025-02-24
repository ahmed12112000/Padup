package com.nevaDev.padeliummarhaba.ui.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nevadev.padeliummarhaba.R
import java.util.Locale
import com.nevaDev.padeliummarhaba.viewmodels.GetInitViewModel
import com.nevaDev.padeliummarhaba.viewmodels.InitBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPayAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SearchListViewModel
import com.nevaDev.padeliummarhaba.viewmodels.TimeSlot
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.InitBookingRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.DayOfWeek

import java.time.ZoneId
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationScreen(
    navController: NavController,
    isUserLoggedIn: Boolean = false,
    context: Context,
    sharedPreferences: SharedPreferences,
    onFetchSuccess: () -> Unit,
    viewModel: KeyViewModel = hiltViewModel(),
    getBookingViewModel: GetBookingViewModel = hiltViewModel(),
    viewModel2: GetInitViewModel = hiltViewModel(),
    viewModel3: SearchListViewModel = hiltViewModel(),
    viewModel4: InitBookingViewModel = hiltViewModel(),
    paymentPayAvoirViewModel: PaymentPayAvoirViewModel,
) {
    var fetchJob by remember { mutableStateOf<Job?>(null) }

    val reservationKey = remember { mutableStateOf<String?>(null) }
    var showPaymentSection by remember { mutableStateOf(false) }
    val selectedReservation = remember { mutableStateOf<ReservationOption?>(null) }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val selectedTimeSlot = remember { mutableStateOf<String?>(null) }

    var hasCompletedFetch by remember { mutableStateOf(false) } // Flag to prevent repeated fetches
    var hasFetchedInitBooking by remember { mutableStateOf(false) } // Flag for InitBooking   hasCalledInitBooking
    var hasFetchedGetBooking by remember { mutableStateOf(false) }
    var hasFetchedSearchList by remember { mutableStateOf(false) }
    var hasCalledInitBooking by remember { mutableStateOf(false) }

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    val parsedTimeSlots by getBookingViewModel.parsedTimeSlots.collectAsState(initial = emptyList())

    // Function to fetch reservation data
    fun fetchReservationData(date: LocalDate) {
        if (isLoading || hasCompletedFetch) return // Don't fetch if already loading or completed
        val formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE) + " 00:00"
        val fetchKeyRequest = FetchKeyRequest(dateTime = formattedDate)

        isLoading = true
        viewModel.getReservationKey(fetchKeyRequest, date)
        Log.d("Tagggg", "FETCH KEY 1")
    }

    // Observe ViewModel response for reservation key
    LaunchedEffect(selectedDate.value) {
        fetchReservationData(selectedDate.value)
    }

    viewModel.dataResultBooking.observe(lifecycleOwner) { result ->
        when (result) {
            is DataResultBooking.Loading -> isLoading = true
            is DataResultBooking.Success -> {
                if (!hasFetchedInitBooking) { // ✅ Ensure it's only called once
                    reservationKey.value = result.data.key
                    isLoading = false
                    onFetchSuccess()
                    reservationKey.value?.let { key ->
                        if (!hasFetchedSearchList) { // ✅ Ensure searchList is only called once
                            viewModel3.searchList(key)
                            hasFetchedSearchList = true
                            Log.d("Tagggg", "FETCH KEY 2")
                        }
                    }
                }
            }
            is DataResultBooking.Failure -> isLoading = false
        }
    }

    // Step 2: Observe searchList results
    viewModel3.dataResultBooking.observe(lifecycleOwner) { searchResult ->
        when (searchResult) {
            is DataResultBooking.Success -> {
                reservationKey.value?.let { key ->
                    if (!hasFetchedInitBooking) { // ✅ Ensure GetInit is only called once
                        viewModel2.GetInit(key)
                        Log.d("Tagggg", "FETCH TERRAIN 3********")
                        hasFetchedInitBooking = true
                    }
                }
            }
            is DataResultBooking.Failure -> { /* Handle failure */ }
            else -> {}
        }
    }

    // Step 3: Observe GetInit results
    // Step 3: Observe GetInit results
    viewModel2.dataResultBooking.observe(lifecycleOwner) { searchResult ->
        when (searchResult) {
            is DataResultBooking.Success -> {
                reservationKey.value?.let { key ->
                    if (!hasCalledInitBooking) { // ✅ Ensure InitBooking is only called once
                        val initBookingRequest = InitBookingRequest(key = key)
                        viewModel4.InitBooking(initBookingRequest)
                        Log.d("Tagggg", "FETCH DATA 4********")
                        hasCalledInitBooking = true // ✅ Use separate flag
                    }
                }
            }
            is DataResultBooking.Failure -> { /* Handle failure */ }
            else -> {}
        }
    }



    // Step 4: Observe InitBooking results
    viewModel4.dataResult1.observe(lifecycleOwner) { initResult ->
        when (initResult) {
            is DataResult.Success -> {
                if (!hasCompletedFetch) { // ✅ Ensure getBooking is only called once
                    reservationKey.value?.let { key ->
                        getBookingViewModel.getBooking(key, selectedDate.value)
                        Log.d("Tagggg", "FETCH FULL DATA 5********")
                        hasCompletedFetch = true
                    }
                }
            }
            is DataResult.Failure -> { /* Handle failure */ }
            else -> {}
        }
    }





    // Function to fetch time slots based on selected date
    fun filterSlotsByDate(newDate: LocalDate) {
        fetchJob?.cancel() // Cancel previous job

        fetchJob = CoroutineScope(Dispatchers.Main).launch { // ✅ Use CoroutineScope
            delay(1000) // Debounce API calls
            reservationKey.value?.let { key ->
                getBookingViewModel.getBooking(key, newDate)
            } ?: run {
                errorMessage = ""
            }
        }
    }

    LaunchedEffect(selectedDate.value, reservationKey.value) {
        reservationKey.value?.let { key ->
            filterSlotsByDate(selectedDate.value)
            //  fetchReservationData(selectedDate.value)

        }
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
                navController = navController
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        DaySelectorWithArrows(
            selectedDate = selectedDate.value,
            onDateSelected = { newDate ->
                selectedDate.value = newDate
                filterSlotsByDate(newDate)
            }
        )






//      ZoneId.of("GMT+1")

        Spacer(modifier = Modifier.height(10.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    if (!showPaymentSection) {
        ReservationOptions(
            onReservationSelected = { selectedReservation.value = it },
            //  isUserLoggedIn = isUserLoggedIn,
            key = reservationKey.value,
            viewModel = getBookingViewModel,
            navController = navController,
            selectedDate = selectedDate.value ,
            selectedTimeSlot = selectedTimeSlot.value,
            paymentPayAvoirViewModel = paymentPayAvoirViewModel,

        )
    }
}
@Composable
fun TimeSlotButton(
    timeSlots: List<TimeSlot>,
    onTimeSlotSelected: (String) -> Unit,
    selectedTimeSlot: String?
) {
    val uniqueTimeSlots = timeSlots.distinctBy { it.time } // Deduplicate time slots

    // Use Box or set a fixed height for LazyVerticalGrid to avoid infinite height constraints
    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(4), // Four buttons per row
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .heightIn(max = 400.dp), // Limit the height to avoid infinite height constraints
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
}




@Composable
fun TabItem(
    isSelected: Boolean,
    title: String,
    icon: Painter,
    onClick: () -> Unit,
    navController: NavController
) {
    Column(
        horizontalAlignment = Alignment.Start,
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
            modifier = Modifier.fillMaxWidth()
        ) {
            // Back arrow icon (clickable)
            IconButton(
                onClick = {
                    navController.navigate("main_screen")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Gray,
                    modifier = Modifier.size(24.dp)
                )
            }

            Icon(
                painter = icon,
                contentDescription = null,
                tint = if (isSelected) Color.Black else Color.Gray,
                modifier = Modifier.size(20.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = if (isSelected) Color.Black else Color.Gray,
                textAlign = TextAlign.Start
            )
        }

        if (isSelected) {
            Divider(
                color = Color(0xFF0054D8),
                thickness = 2.dp,
                modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
            )
        }
    }
}


@Composable
fun ReservationSummary(
    selectedDate: LocalDate,
    selectedTimeSlot: String,
    selectedReservation: ReservationOption,
    selectedExtras: List<Triple<String, String, Int>>,
    amountSelected: Pair<Double, String>?,
    onTotalAmountCalculated: (Double) -> Unit,
    price: String,
    time: String,
    navController: NavController,
    adjustedAmount: Double,
    adjustedSharedExtrasAmount: Double,
    totalSharedExtrasCost: Double,
    totalExtrasCost: Double // Use the passed totalExtrasCost
) {

    // Calculate total cost including extras

    val totalAmountSelected = adjustedAmount + totalExtrasCost

    onTotalAmountCalculated(totalAmountSelected)

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text(
            text = "Détails Réservation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Display reservation details...
        ReservationDetailRow(label = "Espace", value = selectedReservation.name)
        ReservationDetailRow(label = "Prix", value = "${selectedReservation.price} DT"  )
        ReservationDetailRow(
            label = "Date",
            value = DateTimeFormatter.ofPattern("EEE d MMM yyyy", Locale.FRENCH)
                .format(selectedDate)
                .replace(".", "")
                .replaceFirstChar { it.uppercaseChar() }
        )

        ReservationDetailRow(label = "Heure", value = selectedReservation.time)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Détails du Prix",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ReservationDetailRow(label = "Prix de Réservation", value = "${String.format("%.2f", adjustedAmount)} DT")

        selectedExtras.forEach { (name, price, _) ->
            ReservationDetailRow(label = "Extra: $name", value = price)
        }

        ReservationDetailRow(label = "Total", value = "${String.format("%.2f", totalAmountSelected)} DT")
        Log.d("ReservationSummary", "Total Amount after extras: $totalAmountSelected")
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaySelectorWithArrows(
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit
) {
    val dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.FRENCH)
    val dateFormatter = DateTimeFormatter.ofPattern("d", Locale.FRENCH)
    val monthFormatter = DateTimeFormatter.ofPattern("MMM", Locale.FRENCH)

    val currentDate = ZonedDateTime.now(ZoneId.of("Africa/Tunis")).toLocalDate()
    val finalSelectedDate = selectedDate ?: currentDate
    val startOfWeek = finalSelectedDate.with(DayOfWeek.MONDAY) // Always start from Monday
    val daysInWeek = List(7) { startOfWeek.plusDays(it.toLong()) }

    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH)
    val listState = rememberLazyListState()

    Column(modifier = Modifier.fillMaxWidth()) {
        // Month-Year Header
        Text(
            text = monthYearFormatter.format(finalSelectedDate).uppercase(Locale.FRENCH),
            color = Color(0xFF0054D8),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        )

        // Navigation Row with Arrows and "AUJOURD'HUI"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                val newDate = finalSelectedDate.minusWeeks(1) // Go to previous week
                onDateSelected(newDate)
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous week", tint = Color.Gray)
            }

            Row(
                modifier = Modifier
                    .clickable { onDateSelected(currentDate) }
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .padding(vertical = 4.dp, horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "AUJOURD'HUI",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0054D8),
                    fontSize = 12.sp,
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

            IconButton(onClick = {
                val newDate = finalSelectedDate.plusWeeks(1) // Go to next week
                onDateSelected(newDate)
            }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next week", tint = Color.Gray)
            }
        }

        // Days of the Week with Increased Spacing
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp) // Slightly less spacing
        ) {
            items(daysInWeek) { day ->
                Column(
                    modifier = Modifier
                        .clickable { onDateSelected(day) }
                        .background(
                            color = if (day == finalSelectedDate) Color(0xFF0054D8) else Color.White,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = if (day == finalSelectedDate) 0.dp else 1.dp,
                            color = Color.Gray,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(vertical = 6.dp, horizontal = 6.dp) // Reduced padding
                        .width(35.dp), // Reduced width
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = dayFormatter.format(day).uppercase(),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = if (day == finalSelectedDate) Color.White else Color.Gray,
                        fontSize = 10.sp // Reduced font size
                    )
                    Text(
                        text = dateFormatter.format(day),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = if (day == finalSelectedDate) Color.White else Color.Black,
                        fontSize = 14.sp // Reduced font size
                    )
                    Text(
                        text = monthFormatter.format(day).uppercase(),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        color = if (day == finalSelectedDate) Color.White else Color.Gray,
                        fontSize = 10.sp // Reduced font size
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewDaySelectorWithArrows() {
    DaySelectorWithArrows(
        selectedDate = LocalDate.now(), // Set the selected date to today for the preview
        onDateSelected = {}
    )
}
