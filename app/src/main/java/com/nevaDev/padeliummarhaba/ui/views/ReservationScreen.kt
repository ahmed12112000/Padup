package com.nevaDev.padeliummarhaba.ui.views

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.nevadev.padeliummarhaba.R
import java.util.Locale
import com.nevaDev.padeliummarhaba.viewmodels.ExtrasViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetInitViewModel
import com.nevaDev.padeliummarhaba.viewmodels.InitBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SearchListViewModel
import com.padelium.domain.dataresult.DataResult
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
    viewModel2: GetInitViewModel = hiltViewModel(),
    viewModel3: SearchListViewModel = hiltViewModel(),
    viewModel4: InitBookingViewModel = hiltViewModel(),

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
    val saveBookingViewModel: SaveBookingViewModel = hiltViewModel()

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
                reservationKey.value?.let { key ->
                    viewModel2.GetInit(key)
                    viewModel3.searchList(key)
                    viewModel4.InitBooking(key)
                    getBookingViewModel.getBooking(key)
                }
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
            items(filteredTimeSlots.distinctBy { it.time }) { timeSlot ->
                TimeSlotButton(
                    slot = timeSlot.time,
                    isSelected = selectedTimeSlot.value == timeSlot.time,
                    onClick = { selectedTimeSlot.value = timeSlot.time }
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
    icon: Painter,
    onClick: () -> Unit,
    onBackClick: () -> Unit
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
            IconButton(onClick = { onBackClick() }) {
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
    selectedExtras: List<Triple<String, String, Int>>, // Now it's passed as a parameter
    selectedRaquette: String,
    includeBalls: Boolean,
    amountSelected: Pair<Double, String>?, // Update to accept Pair
    onTotalAmountCalculated: (Double, String) -> Unit,

) {
    // Tracking the total cost of the extras selected
    val reservationPrice = selectedReservation.price.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0
    val totalExtrasCost = selectedExtras.sumOf { (name, priceString, _) ->
        priceString.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0
    }

    // Calculate the total amount selected (price + extras) and remember it
    val totalAmountSelected = remember(amountSelected, totalExtrasCost) {
        val baseAmount = amountSelected?.first ?: reservationPrice
        baseAmount + totalExtrasCost // Use amountSelected.first for price
    }

    val currencySymbol = selectedReservation.price.takeWhile { !it.isDigit() && it != '.' }

    // Update the total amount and send both the amount and currency symbol to the callback
    onTotalAmountCalculated(totalAmountSelected, currencySymbol)

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
        ReservationDetailRow(label = "Prix", value = "${currencySymbol} ${String.format("%.2f", amountSelected?.first ?: 0.0)}") // Display currency symbol next to price
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

        ReservationDetailRow(label = "Prix de Réservation", value = "${currencySymbol} ${String.format("%.2f", amountSelected?.first ?: 0.0)}")

        selectedExtras.forEach { (name, price, _) ->
            ReservationDetailRow(label = "Extra: $name", value = price)
        }

        // Display summary of extras
        val extrasSummary = selectedExtras.joinToString(separator = ", ") { (name, price, _) ->
            "$name ($price)"
        }
        ReservationDetailRow(label = "Extras", value = extrasSummary)

        // Display the total cost including the added extras
        ReservationDetailRow(label = "Total", value = "${currencySymbol} ${String.format("%.2f", totalAmountSelected)}") // Display currency symbol with total amount
        onTotalAmountCalculated(totalAmountSelected, currencySymbol)

    }
}





@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun DaySelectorWithArrows(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
) {
    val dayFormatter = DateTimeFormatter.ofPattern("EEE", Locale.FRENCH)
    val dateFormatter = DateTimeFormatter.ofPattern("d", Locale.FRENCH)
    val monthFormatter = DateTimeFormatter.ofPattern("MMM", Locale.FRENCH)

    val daysInWeek = (0..6).map { offset ->
        selectedDate.minusDays(selectedDate.dayOfWeek.value.toLong() - 1L).plusDays(offset.toLong())
    }
    val monthYearFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH)


    val listState = rememberLazyListState()

    LaunchedEffect(selectedDate) {
        val selectedIndex = daysInWeek.indexOf(selectedDate)
        if (selectedIndex != -1) {
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
        getBookingViewModel = hiltViewModel<GetBookingViewModel>()
    )
}



@Preview(showBackground = true)
@Composable
fun TabItemPreview() {
    Row {
        TabItem(
            isSelected = true,
            title = "CHOISIR UN CRÉNEAU",
            icon = painterResource(id = R.drawable.calendre),
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
    val today = LocalDate.of(2024, 11, 29)

    DaySelectorWithArrows(
        selectedDate = today,
        onDateSelected = { selectedDay ->
            println("Selected date: $selectedDay")
        }
    )
}
@Preview(showBackground = true)
@Composable
fun ReservationOptionsPreview() {
    val navController = rememberNavController()

    ReservationOptions(
        onReservationSelected = {},
        isUserLoggedIn = true,
        key = "dummyKey",
        navController = navController,
        selectedDate = LocalDate.now(),
        selectedTimeSlot = "12:00"
    )
}






