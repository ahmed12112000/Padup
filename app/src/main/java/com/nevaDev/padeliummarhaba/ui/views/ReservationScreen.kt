package com.nevaDev.padeliummarhaba.ui.views

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
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
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.Popup
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nevadev.padeliummarhaba.R
import java.util.Locale
import com.nevaDev.padeliummarhaba.viewmodels.GetInitViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetReservationViewModel
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
import java.text.SimpleDateFormat
import java.time.DayOfWeek

import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar
import java.util.Date
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
    getReservationViewModel: GetReservationViewModel
) {
    var fetchJob by remember { mutableStateOf<Job?>(null) }

    val reservationKey = remember { mutableStateOf<String?>(null) }
    var showPaymentSection by remember { mutableStateOf(false) }
    val selectedReservation = remember { mutableStateOf<ReservationOption?>(null) }
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val selectedTimeSlot = remember { mutableStateOf<String?>(null) }

    // Flags to prevent repeated fetches
    var hasCompletedFetch by remember { mutableStateOf(false) }
    var hasFetchedInitBooking by remember { mutableStateOf(false) }
    var hasFetchedSearchList by remember { mutableStateOf(false) }
    var hasCalledInitBooking by remember { mutableStateOf(false) }

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    var previousSelectedDate by remember { mutableStateOf<LocalDate?>(null) }

    val parsedTimeSlots by getBookingViewModel.parsedTimeSlots.collectAsState(initial = emptyList())

    var hasFetchedBooking by remember { mutableStateOf(false) }

    fun fetchInitBooking() {
        reservationKey.value?.let { key ->
            if (!hasFetchedInitBooking) {
                hasFetchedInitBooking = true
                val initBookingRequest = InitBookingRequest(key = key)
                viewModel4.InitBooking(initBookingRequest)
                Log.d("Tagggg", "FETCH DATA 4********")
            }
        }
    }

    fun fetchSearchList() {
        reservationKey.value?.let { key ->
            if (!hasFetchedSearchList) {
                hasFetchedSearchList = true
                viewModel3.searchList(key)
                Log.d("Tagggg", "FETCH KEY 2")
            }
        }
    }
    fun  fetchInitData() {
        reservationKey.value?.let { key ->
            if (!hasCompletedFetch) {
                hasCompletedFetch = true
                viewModel2.GetInit(key)
                Log.d("Tagggg", "FETCH TERRAIN 3********")
            }
        }
    }
    fun  fetchBookingData() {
        reservationKey.value?.let { key ->
                hasFetchedBooking = true
                getBookingViewModel.getBooking(key, selectedDate.value)
                Log.d("Tagggg", "FETCH TERRAIN 3********")

        }
    }

    /*

    fun fetchBookingData() {
        // Only fetch if the data has not been fetched yet
        if (!hasFetchedBooking) {
            hasFetchedBooking = true // Mark as fetched

            fetchJob?.cancel()
            fetchJob = CoroutineScope(Dispatchers.Main).launch {
                reservationKey.value?.let { key ->
                    getBookingViewModel.getBooking(key, selectedDate.value) // Use selectedDate.value here
                    Log.d("Tagggg", "FETCH FULL DATA 5********")
                }
            }
        }
    }

     */

    LaunchedEffect(reservationKey.value) {
        Log.d("Tagggg", "Reservation Key Changed: ${reservationKey.value}")
        fetchBookingData() // Call fetchBookingData whenever reservationKey changes
    }

    fun handleFailure(errorCode: Int?) {
        isLoading = false
        errorCode?.let {
            if (it != 200) {
                navController.navigate("server_error_screen")
            }
        }
    }
    // Function to reset all data before fetching new reservation data
    fun resetData() {
        reservationKey.value = null
        hasCompletedFetch = false
        hasFetchedInitBooking = false
        hasFetchedSearchList = false
        hasCalledInitBooking = false
        isLoading = false
        fetchJob?.cancel()
    }
    fun fetchReservationData(date: LocalDate) {
        if (isLoading) return // Prevent multiple parallel requests
        resetData() // Clear all previous data

        val formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE) + " 00:00"
        val fetchKeyRequest = FetchKeyRequest(dateTime = formattedDate)

        isLoading = true
        viewModel.getReservationKey(fetchKeyRequest, date)
        Log.d("Tagggg", "FETCH KEY for Date: $date")
    }

    LaunchedEffect(selectedDate.value) {
        if (selectedDate.value != previousSelectedDate) {
            fetchReservationData(selectedDate.value)
            previousSelectedDate = selectedDate.value
        }
    }

    viewModel.dataResultBooking.observe(lifecycleOwner) { result ->
        when (result) {
            is DataResultBooking.Success -> {
                reservationKey.value = result.data.key
                isLoading = false
                onFetchSuccess()
                fetchSearchList()
            }
            is DataResultBooking.Failure -> handleFailure(result.errorCode)
            is DataResultBooking.Loading -> isLoading = true
        }
    }



    viewModel3.dataResultBooking.observe(lifecycleOwner) { result ->
        when (result) {
            is DataResultBooking.Success -> {
                // Call the next step in sequence: Fetch Init Data
                fetchInitData()
                fetchInitBooking()
            }
            is DataResultBooking.Failure -> handleFailure(result.errorCode)
            is DataResultBooking.Loading -> isLoading = true
        }
    }

    viewModel3.navigateToErrorScreen.observe(lifecycleOwner) { shouldNavigate ->
        if (shouldNavigate) {
            navController.navigate("server_error_screen")
        }
    }

/*
    viewModel2.dataResultBooking.observe(lifecycleOwner) { result ->
        when (result) {
            is DataResultBooking.Success -> {
                // Call the next step in sequence: Fetch Init Booking
                fetchInitBooking()
            }
            is DataResultBooking.Failure -> handleFailure(result.errorCode)
            is DataResultBooking.Loading -> isLoading = true
        }
    }

 */



    viewModel4.dataResult1.observe(lifecycleOwner) { result ->
        when (result) {
            is DataResult.Success -> {
                // Call the final step in sequence: Fetch Booking Data
                fetchBookingData()
            }
            is DataResult.Failure -> handleFailure(result.errorCode)
            is DataResult.Loading -> isLoading = true
        }
    }






// Ensure that when selectedDate changes, the fetchBookingData is called again






    // Function to fetch time slots based on the selected date


    // Trigger filtering when a new date is selected
     /* LaunchedEffect(selectedDate.value, reservationKey.value) {
        reservationKey.value?.let { key ->
            filterSlotsByDate(selectedDate.value)
        }
    }

      */

    Column(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TabItem(
                isSelected = !showPaymentSection,
                title = "CHOISIR UN CRÉNEAU",
                icon = painterResource(id = R.drawable.calendre),
                onClick = { showPaymentSection = false },
                navController = navController
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        DaySelectorWithArrows(
            selectedDate = selectedDate.value,
            onDateSelected = { newDate ->
                Log.d("DATE_SELECTION", "Selected Date: $newDate")
                selectedDate.value = newDate
                resetData()
                fetchReservationData(newDate)
            }
        )







//      ZoneId.of("GMT+1")

        Spacer(modifier = Modifier.height(10.dp))


    }


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
            getReservationViewModel = getReservationViewModel
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
            fontSize = 18.sp,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text("OK",color = Color(0xFF0054D8), fontWeight = FontWeight.Bold,)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler",color = Color(0xFF0054D8), fontWeight = FontWeight.Bold,)
            }
        }
    ) {
        Surface(
            color = Color.White, // Set the background color to white
            modifier = Modifier.fillMaxSize() // Ensure it takes up the full space
        ) {
            DatePicker(state = datePickerState)
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePickerModal(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = remember { mutableStateOf<Long?>(null) }
    val selectedDate = datePickerState.value
    val today = Calendar.getInstance().apply {
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }.timeInMillis
    val maxSelectableDate = today + (21 * 24 * 60 * 60 * 1000) // 21 days ahead

    val calendar = Calendar.getInstance()
    var currentMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var currentYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }

    calendar.set(Calendar.MONTH, currentMonth)
    calendar.set(Calendar.YEAR, currentYear)
    calendar.set(Calendar.DAY_OF_MONTH, 1)

    val startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val totalDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    // French abbreviated month names
    val frenchMonths =
        listOf("Jan", "Fév", "Mar", "Avr", "Mai", "Juin", "Juil", "Aoû", "Sep", "Oct", "Nov", "Déc")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = Color.White, // Ensuring entire dialog is white
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFE0E0E0)), // Light grayish border
            modifier = Modifier
               // .fillMaxWidth()
                .heightIn(min = 10.dp, max = 600.dp) // Control min/max height

                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 10.dp, max = 500.dp) // Control min/max height

                    .background(Color.White) // Ensuring white background
                    .padding(16.dp)
            ) {
                // Month and Year Header with Arrows
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(onClick = {
                        if (currentMonth == 0) {
                            currentMonth = 11
                            currentYear--
                        } else {
                            currentMonth--
                        }
                    }) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Mois précédent",
                            tint = Color.Black
                        )
                    }
                    Text(
                        text = "${frenchMonths[currentMonth]} $currentYear",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    IconButton(onClick = {
                        if (currentMonth == 11) {
                            currentMonth = 0
                            currentYear++
                        } else {
                            currentMonth++
                        }
                    }) {
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = "Mois suivant",
                            tint = Color.Black
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Days of the week
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    listOf("LUN", "MAR", "MER", "JEU", "VEN", "SAM", "DIM").forEach {
                        Text(
                            text = it,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Calendar Grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    // Empty cells before the first day
                    items(startDayOfWeek - 1) {
                        Box(modifier = Modifier.size(40.dp))
                    }

                    // Days of the month
                    items(totalDaysInMonth) { day ->
                        val normalizedCalendar = Calendar.getInstance().apply {
                            set(Calendar.YEAR, calendar.get(Calendar.YEAR))
                            set(Calendar.MONTH, calendar.get(Calendar.MONTH))
                            set(Calendar.DAY_OF_MONTH, day + 1)
                            set(Calendar.HOUR_OF_DAY, 0)
                            set(Calendar.MINUTE, 0)
                            set(Calendar.SECOND, 0)
                            set(Calendar.MILLISECOND, 0)
                        }
                        val dateMillis = normalizedCalendar.timeInMillis

                        val isSelected = selectedDate?.let {
                            val selectedCalendar =
                                Calendar.getInstance().apply { timeInMillis = it }
                            selectedCalendar.get(Calendar.YEAR) == normalizedCalendar.get(
                                Calendar.YEAR
                            ) &&
                                    selectedCalendar.get(Calendar.MONTH) == normalizedCalendar.get(
                                Calendar.MONTH
                            ) &&
                                    selectedCalendar.get(Calendar.DAY_OF_MONTH) == normalizedCalendar.get(
                                Calendar.DAY_OF_MONTH
                            )
                        } ?: false

                        val isDisabled = dateMillis < today || dateMillis > maxSelectableDate

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(40.dp)
                                .background(
                                    when {
                                        isSelected -> Color(0xFF0054D8) // Blue for selected date
                                        else -> Color.Transparent
                                    },
                                    shape = CircleShape
                                )
                                .clickable(enabled = !isDisabled) {
                                    datePickerState.value = dateMillis
                                }
                        ) {
                            Text(
                                text = (day + 1).toString(),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = when {
                                        isSelected -> Color.White
                                        isDisabled -> Color.Gray // Gray color for disabled days
                                        else -> Color.Black
                                    },
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Buttons Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.background(Color.White) // Ensuring button background is white
                    ) {
                        Text("Annuler", color = Color(0xFF0054D8), fontWeight = FontWeight.Bold)
                    }
                    TextButton(
                        onClick = {
                            onDateSelected(selectedDate)
                            onDismiss()
                        },
                        modifier = Modifier.background(Color.White) // Ensuring button background is white
                    ) {
                        Text("OK", color = Color(0xFF0054D8), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}






@Composable
fun MonthSelectionDialog(onMonthSelected: (Int) -> Unit) {
    val months = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    AlertDialog(
        onDismissRequest = {},
        title = {
            Text("Select Month")
        },
        text = {
            Column {
                months.forEachIndexed { index, month ->
                    TextButton(onClick = { onMonthSelected(index) }) {
                        Text(month)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {}) {
                Text("Cancel")
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomDatePickerModal() {
    CustomDatePickerModal(
        onDateSelected = { selectedDate ->
            // Handle the selected date here
            Log.d("Selected Date", "Date selected: $selectedDate")
        },
        onDismiss = {
            // Handle dismiss action here
            Log.d("DatePicker", "Date Picker dismissed")
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DatePickerModalDarkPreview() {
    MaterialTheme {
        DatePickerModal(
            onDateSelected = { /* Handle date selection */ },
            onDismiss = { /* Handle dismiss */ }
        )
    }
}

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
    val startOfWeek = finalSelectedDate.with(DayOfWeek.MONDAY)
    val daysInWeek = List(7) { startOfWeek.plusDays(it.toLong()) }

    val maxFutureDate = currentDate.plusWeeks(3)
    val minPastDate = currentDate

    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH)
    val listState = rememberLazyListState()

    // State for popup visibility and selected date
    var showCalendarDialog by remember { mutableStateOf(false) }

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
                .padding(vertical = 2.dp)
        )

        // Navigation Row with Arrows and "AUJOURD'HUI"
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    val newDate = finalSelectedDate.minusWeeks(1)
                    if (newDate >= minPastDate) {
                        onDateSelected(newDate)
                    }
                },
                enabled = finalSelectedDate > minPastDate
            ) {
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
                    Icons.Filled.CalendarMonth,
                    contentDescription = "Today",
                    tint = Color(0xFF0054D8),
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .size(20.dp)
                        .clickable { showCalendarDialog = true }
                )
            }

            IconButton(
                onClick = {
                    val newDate = finalSelectedDate.plusWeeks(1)
                    if (newDate <= maxFutureDate) {
                        onDateSelected(newDate)
                    }
                },
                enabled = finalSelectedDate < maxFutureDate
            ) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next week", tint = Color.Gray)
            }
        }

        // Show the CustomDatePickerModal when clicked
        if (showCalendarDialog) {
            CustomDatePickerModal(
                onDateSelected = { millis ->
                    millis?.let { onDateSelected(LocalDate.ofEpochDay(it / 86400000)) }
                    showCalendarDialog = false // Close dialog after selecting a date
                },
                onDismiss = { showCalendarDialog = false }
            )
        }

        // Days of the Week
        LazyRow(
            state = listState,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(daysInWeek) { day ->
                val isClickable = day >= currentDate
                val textColor = if (!isClickable) Color.LightGray else if (day == finalSelectedDate) Color.White else Color.Black
                val backgroundColor = if (day == finalSelectedDate) Color(0xFF0054D8) else Color.White
                val borderColor = if (day == finalSelectedDate) Color.Transparent else Color.Gray

                Column(
                    modifier = Modifier
                        .then(
                            if (isClickable) Modifier.clickable { onDateSelected(day) }
                            else Modifier
                        )
                        .background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
                        .border(1.dp, borderColor, shape = RoundedCornerShape(8.dp))
                        .padding(vertical = 6.dp, horizontal = 6.dp)
                        .width(35.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = dayFormatter.format(day).uppercase(),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = textColor,
                        fontSize = 10.sp
                    )
                    Text(
                        text = dateFormatter.format(day),
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = textColor,
                        fontSize = 14.sp
                    )
                    Text(
                        text = monthFormatter.format(day).uppercase(),
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        color = textColor,
                        fontSize = 10.sp
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
