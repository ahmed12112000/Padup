package com.nevaDev.padeliummarhaba.ui.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import com.padelium.data.dto.ReservationOption
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nevaDev.padeliummarhaba.viewmodels.GetBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.KeyViewModel
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.FetchKeyRequest
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import com.nevadev.padeliummarhaba.R
import java.util.Locale
import com.nevaDev.padeliummarhaba.viewmodels.GetInitViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetReservationViewModel
import com.nevaDev.padeliummarhaba.viewmodels.InitBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPayAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SearchListViewModel
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.InitBookingRequest
import kotlinx.coroutines.Job
import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Calendar

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
    var isLoading by remember { mutableStateOf(false) }
    val selectedTimeSlot = remember { mutableStateOf<String?>(null) }
    var hasCompletedFetch by remember { mutableStateOf(false) }
    var hasFetchedInitBooking by remember { mutableStateOf(false) }
    var hasFetchedSearchList by remember { mutableStateOf(false) }
    var hasCalledInitBooking by remember { mutableStateOf(false) }
    var hasNoData by remember { mutableStateOf(false) }
    var hasAvailableReservations by remember { mutableStateOf(false) }
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    var previousSelectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var hasFetchedBooking by remember { mutableStateOf(false) }


    fun fetchInitBooking() {
        reservationKey.value?.let { key ->
            if (!hasFetchedInitBooking) {
                hasFetchedInitBooking = true
                val initBookingRequest = InitBookingRequest(key = key)
                viewModel4.InitBooking(initBookingRequest)
            }
        }
    }

    fun fetchSearchList() {
        reservationKey.value?.let { key ->
            if (!hasFetchedSearchList) {
                hasFetchedSearchList = true
                viewModel3.searchList(key)
            }
        }
    }

    fun  fetchInitData() {
        reservationKey.value?.let { key ->
            if (!hasCompletedFetch) {
                hasCompletedFetch = true
                viewModel2.GetInit(key)
            }
        }
    }
    fun fetchBookingData() {
        reservationKey.value?.let { key ->
            hasFetchedBooking = true
            getBookingViewModel.getBooking(key, selectedDate.value)
        }
    }
    fun handleFailure(errorCode: Int?) {
        isLoading = false
        errorCode?.let {
            if (it != 200) {
                navController.navigate("server_error_screen")
            }
        }
    }
    fun resetData() {
        reservationKey.value = null
        hasCompletedFetch = false
        hasFetchedInitBooking = false
        hasFetchedSearchList = false
        hasCalledInitBooking = false
        hasFetchedBooking = false
        isLoading = false
        fetchJob?.cancel()

    }
    fun fetchReservationData(date: LocalDate) {
        if (isLoading) return
        resetData()

        val formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE) + " 00:00"
        val fetchKeyRequest = FetchKeyRequest(dateTime = formattedDate)

        isLoading = true
        viewModel.getReservationKey(fetchKeyRequest, date)
    }

    LaunchedEffect(selectedDate.value) {
        if (selectedDate.value != previousSelectedDate) {
            if (selectedDate.value.dayOfMonth in 11..22) {
            }
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
                fetchInitData()

                viewModel2.GetInit(reservationKey.value ?: return@observe).also {
                    fetchInitBooking()
                }
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
    viewModel4.dataResult1.observe(lifecycleOwner) { result ->
        when (result) {
            is DataResult.Success -> {
                fetchBookingData()
            }
            is DataResult.Failure -> handleFailure(result.errorCode)
            is DataResult.Loading -> isLoading = true
        }
    }

    LaunchedEffect(reservationKey.value) {
        fetchBookingData()
    }

    getBookingViewModel.dataResultBooking.observe(lifecycleOwner) { result ->
        when (result) {
            is DataResultBooking.Success -> {
                val bookingResponses = result.data as? List<GetBookingResponseDTO>


                if (bookingResponses != null && bookingResponses.isNotEmpty()) {
                    val plannings = bookingResponses.flatMap { it.plannings }

                    hasAvailableReservations = plannings.isNotEmpty()

                    hasNoData = !hasAvailableReservations

                } else {
                    hasNoData = true
                    hasAvailableReservations = false
                }
            }
            is DataResultBooking.Failure -> {
                handleFailure(result.errorCode)
                hasNoData = true
                hasAvailableReservations = false
            }
            is DataResultBooking.Loading -> isLoading = true
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
                icon = painterResource(id = R.drawable.calendre),
                onClick = { showPaymentSection = false },
                navController = navController
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        DaySelectorWithArrows(
            selectedDate = selectedDate.value,
            onDateSelected = { newDate ->
                selectedDate.value = newDate
                resetData()
                fetchReservationData(newDate)
            }
        )



        Spacer(modifier = Modifier.height(10.dp))
        if (hasNoData) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Pas des heures disponibles selon vos critères de recherche",
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
        }
    }


    if (!showPaymentSection) {
        Spacer(modifier = Modifier.height(10.dp))

 if (hasAvailableReservations) {
            ReservationOptions(
                onReservationSelected = { selectedReservation.value = it },
                key = reservationKey.value,
                viewModel = getBookingViewModel,
                navController = navController,
                selectedDate = selectedDate.value,
                selectedTimeSlot = selectedTimeSlot.value,
                paymentPayAvoirViewModel = paymentPayAvoirViewModel,
                getReservationViewModel = getReservationViewModel
            )
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
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
    totalExtrasCost: Double
) {


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
    val maxSelectableDate = today + (21 * 24 * 60 * 60 * 1000)
    val calendar = Calendar.getInstance()
    var currentMonth by remember { mutableStateOf(calendar.get(Calendar.MONTH)) }
    var currentYear by remember { mutableStateOf(calendar.get(Calendar.YEAR)) }
    calendar.set(Calendar.MONTH, currentMonth)
    calendar.set(Calendar.YEAR, currentYear)
    calendar.set(Calendar.DAY_OF_MONTH, 1)
    val startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
    val totalDaysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    val frenchMonths =
        listOf("Jan", "Fév", "Mar", "Avr", "Mai", "Juin", "Juil", "Aoû", "Sep", "Oct", "Nov", "Déc")

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
            modifier = Modifier
                .heightIn(min = 10.dp, max = 600.dp)
                .padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 10.dp, max = 500.dp)
                    .background(Color.White)
                    .padding(16.dp)
            ) {
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

                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    items(startDayOfWeek - 1) {
                        Box(modifier = Modifier.size(40.dp))
                    }

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
                                        isSelected -> Color(0xFF0054D8)
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
                                        isDisabled -> Color.Gray
                                        else -> Color.Black
                                    },
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.background(Color.White)
                    ) {
                        Text("Annuler", color = Color(0xFF0054D8), fontWeight = FontWeight.Bold)
                    }
                    TextButton(
                        onClick = {
                            onDateSelected(selectedDate)
                            onDismiss()
                        },
                        modifier = Modifier.background(Color.White)
                    ) {
                        Text("OK", color = Color(0xFF0054D8), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
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
    var showCalendarDialog by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
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

        if (showCalendarDialog) {
            CustomDatePickerModal(
                onDateSelected = { millis ->
                    millis?.let { onDateSelected(LocalDate.ofEpochDay(it / 86400000)) }
                    showCalendarDialog = false
                },
                onDismiss = { showCalendarDialog = false }
            )
        }
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



