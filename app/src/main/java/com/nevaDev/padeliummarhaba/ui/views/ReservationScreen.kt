package com.nevaDev.padeliummarhaba.ui.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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
import com.padelium.domain.dto.GetBookingResponse

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
    paymentPayAvoirViewModel : PaymentPayAvoirViewModel

) {
    val reservationKey = remember { mutableStateOf<String?>(null) }
    var showPaymentSection by remember { mutableStateOf(false) }
    var showLoginPopup by remember { mutableStateOf(false) }
    val selectedReservation = remember { mutableStateOf<ReservationOption?>(null) }
    val currentDate = ZonedDateTime.now(ZoneId.of("Africa/Tunis")).toLocalDate()
    val selectedDate = remember { mutableStateOf(LocalDate.now()) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val selectedTimeSlot = remember { mutableStateOf<String?>(null) }

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    // Observing parsed time slots from ViewModel
    val parsedTimeSlots by getBookingViewModel.parsedTimeSlots.collectAsState(initial = emptyList())

    // Function to fetch reservation data
    // Function to fetch reservation data
    fun fetchReservationData(date: LocalDate) {
        val formattedDate = date.format(DateTimeFormatter.ISO_LOCAL_DATE) + " 00:00"
        val fetchKeyRequest = FetchKeyRequest(dateTime = formattedDate)

        isLoading = true
        viewModel.getReservationKey(fetchKeyRequest, date)

        viewModel.dataResultBooking.observe(lifecycleOwner) { result ->
            when (result) {
                is DataResultBooking.Loading -> isLoading = true
                is DataResultBooking.Success -> {
                    reservationKey.value = result.data.key
                    isLoading = false
                    onFetchSuccess()

                    reservationKey.value?.let { key ->
                        // Now that the key is ready, call getBooking to fetch the booking data
                        //getBookingViewModel.getBooking(key, selectedDate.value)
                    }
                }
                is DataResultBooking.Failure -> {
                    isLoading = false
                    errorMessage = result.errorMessage ?: "Unknown error occurred"
                    Log.e("ReservationScreen", "Error: ${result.errorMessage}")
                }
            }
        }
    }

    // Function to fetch time slots based on selected date
    fun filterSlotsByDate(newDate: LocalDate) {
        reservationKey.value?.let { key ->
            getBookingViewModel.getBooking(key, newDate)
            Log.e("TAGGGGGG","innnnnnnnnnnnnnnnnnnn")

        } ?: run {
            Log.e("TAGGGGGG","ahhhhhhhhhhhhhhhhhhhhhh")

            errorMessage = ""
        }
    }
    LaunchedEffect(selectedDate.value) {
        fetchReservationData(selectedDate.value)
        filterSlotsByDate(selectedDate.value)
    }

/*
    LaunchedEffect(selectedDate.value) {
        fetchReservationData(selectedDate.value)
        filterSlotsByDate(selectedDate.value)
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

   //     Spacer(modifier = Modifier.height(10.dp))

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color.Red,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        } else if (parsedTimeSlots.isNotEmpty()) {
            // Display available time slots here, e.g., in a LazyColumn or other UI component
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(parsedTimeSlots) { timeSlot ->
                    Text(text = timeSlot.toString(), modifier = Modifier.padding(8.dp))
                }
            }
        } else {
            Text(
                text = "",
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

        }
/*

                TimeSlotSelector(
                    timeSlots = parsedTimeSlots,
                    onTimeSlotSelected = { selectedTimeSlot.value = it },
                    selectedTimeSlot = selectedTimeSlot.value
                )

*/

        if (parsedTimeSlots.isEmpty()) {
            Text(text = "", color = Color.Red, textAlign = TextAlign.Center)
        }
    }

      //  Spacer(modifier = Modifier.height(16.dp))

        if (!showPaymentSection) {
            ReservationOptions(
                onReservationSelected = { selectedReservation.value = it },
                isUserLoggedIn = isUserLoggedIn,
                key = reservationKey.value,
                viewModel = getBookingViewModel,
                navController = navController,
                selectedDate = selectedDate.value ,
                selectedTimeSlot = selectedTimeSlot.value,
                paymentPayAvoirViewModel = paymentPayAvoirViewModel


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
                    modifier = Modifier.size(24.dp),

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
    val startOfWeek = finalSelectedDate.minusDays(finalSelectedDate.dayOfWeek.value.toLong() - 1L)
    val daysInWeek = (0..6).map { offset -> startOfWeek.plusDays(offset.toLong()) }
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH)
    val listState = rememberLazyListState()

    LaunchedEffect(finalSelectedDate) {
        val selectedIndex = daysInWeek.indexOf(finalSelectedDate)
        if (selectedIndex != -1) {
            listState.animateScrollToItem(selectedIndex)
        }
    }

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
                val newDate = finalSelectedDate.minusDays(1)
                onDateSelected(newDate)
            }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Previous day", tint = Color.Gray)
            }

            Row(
                modifier = Modifier.clickable {
                    onDateSelected(currentDate) // Set to current date explicitly
                }
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
                val newDate = finalSelectedDate.plusDays(1)
                onDateSelected(newDate)
            }) {
                Icon(Icons.Default.ArrowForward, contentDescription = "Next day", tint = Color.Gray)
            }
        }

        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp) // Add space between days
        ) {
            items(daysInWeek) { day ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(
                        modifier = Modifier
                            .clickable {
                                onDateSelected(day)
                            }
                            .background(
                                color = if (day == finalSelectedDate) Color(0xFF0054D8) else Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .border(
                                width = if (day == finalSelectedDate) 0.dp else 1.dp,
                                color = Color.Gray,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(vertical = 4.dp, horizontal = 4.dp)
                            .width(40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dayFormatter.format(day).uppercase(),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = if (day == finalSelectedDate) Color.White else Color.Gray,
                            fontSize = 10.sp
                        )
                        Text(
                            text = dateFormatter.format(day),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = if (day == finalSelectedDate) Color.White else Color.Black,
                            fontSize = 14.sp
                        )
                        Text(
                            text = monthFormatter.format(day).uppercase(),
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            color = if (day == finalSelectedDate) Color.White else Color.Gray,
                            fontSize = 10.sp
                        )
                    }
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



@Preview(showBackground = true)
@Composable
fun TabItemPreview() {
    val navController = rememberNavController()

    Row {
        TabItem(
            isSelected = true,
            title = "CHOISIR UN CRÉNEAU",
            icon = painterResource(id = R.drawable.calendre),
            onClick = {
                Log.d("TabItemPreview", "Tab clicked")
            },
            navController = navController,

        )

        Spacer(modifier = Modifier.width(16.dp))


    }
}







