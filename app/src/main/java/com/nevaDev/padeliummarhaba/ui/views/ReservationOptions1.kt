package com.nevaDev.padeliummarhaba.ui.views

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nevaDev.padeliummarhaba.models.ReservationOption
import com.nevaDev.padeliummarhaba.viewmodels.ExtrasViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.TimeSlot
import com.nevadev.padeliummarhaba.R
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.data.repositoriesImp.SaveBookingRequestSerializer
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.PlanningDTO
import com.padelium.domain.dto.SaveBookingRequest
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BookingViewModel : ViewModel() {
    // Backing property for MutableLiveData
    private val _selectedBookings = MutableLiveData<List<GetBookingResponseDTO>>(emptyList())

    // Publicly exposed LiveData
    val selectedBookings: LiveData<List<GetBookingResponseDTO>> get() = _selectedBookings

    // Function to update the selected bookings
    fun updateBookings(newBookings: List<GetBookingResponseDTO>) {
        Log.d("BookingViewModel", "updateBookings called")

        _selectedBookings.value = newBookings
        Log.d("BookingViewModel", "Updated bookings: $newBookings")

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
    bookingViewModel: BookingViewModel = hiltViewModel()
) {
    var amountSelected by remember { mutableStateOf<Double?>(null) }
    var currencySymbol by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showLoginPopup by remember { mutableStateOf(false) }
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val gson = GsonBuilder()
        .registerTypeAdapter(SaveBookingRequest::class.java, SaveBookingRequestSerializer())
        .create()

    val dataResultBooking by viewModel.dataResultBooking.observeAsState(initial = DataResultBooking.Loading)
    val filteredTimeSlots by viewModel.filteredTimeSlots.observeAsState(emptyList())

    LaunchedEffect(key) {
        key?.let { viewModel.getBooking(it) }
        viewModel1.Extras()
    }


    when (val result = dataResultBooking) {
        is DataResultBooking.Loading -> LoadingState()
        is DataResultBooking.Success -> SuccessState(
            establishmentsList = result.data,
            filteredTimeSlots = filteredTimeSlots,
            isUserLoggedIn = isUserLoggedIn,
            navController = navController,
            bookingViewModel = bookingViewModel,
            saveBookingViewModel = viewModel2,
            onReservationSelected = onReservationSelected,
            setAmountSelected = { amountSelected = it },
            setCurrencySymbol = { currencySymbol = it },
            showLoginPopup = { showLoginPopup = it }
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
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
    }
}

@Composable
private fun SuccessState(
    establishmentsList: List<GetBookingResponseDTO>,
    filteredTimeSlots: List<TimeSlot>,
    isUserLoggedIn: Boolean,
    navController: NavController,
    bookingViewModel: BookingViewModel,
    saveBookingViewModel: SaveBookingViewModel,
    onReservationSelected: (ReservationOption) -> Unit,
    setAmountSelected: (Double) -> Unit,
    setCurrencySymbol: (String) -> Unit,
    showLoginPopup: (Boolean) -> Unit
) {
    if (establishmentsList.isNotEmpty()) {
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
                    showLoginPopup = showLoginPopup
                )
            }
        }
    } else {
        Text(
            text = "No establishments available",
            color = Color.Gray,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun EstablishmentCard(
    getBookingResponseDTO: GetBookingResponseDTO,
    filteredTimeSlots: List<TimeSlot>,
    isUserLoggedIn: Boolean,
    navController: NavController,
    bookingViewModel: BookingViewModel,
    saveBookingViewModel: SaveBookingViewModel,
    setAmountSelected: (Double) -> Unit,
    setCurrencySymbol: (String) -> Unit,
    showLoginPopup: (Boolean) -> Unit,
    viewModel1: ExtrasViewModel = hiltViewModel(),

    ) {
    val availablePlannings = getBookingResponseDTO.plannings.filter { planning ->
        filteredTimeSlots.any { slot -> slot.time == planning.fromStr }
    }
    var showLoginPopup by remember { mutableStateOf(false) }

    availablePlannings.forEach { planning ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 6.dp)
                .clickable {
                    handleCardClick(
                        planning = planning,
                        getBookingResponseDTO = getBookingResponseDTO,
                        bookingViewModel = bookingViewModel,
                        navController = navController,
                        isUserLoggedIn = isUserLoggedIn,
                        onLoginRequired = { showLoginPopup = true },
                        saveBookingViewModel = saveBookingViewModel,
                        viewModel1 = viewModel1 // Pass viewModel1 here

                    )
                },
            shape = RoundedCornerShape(12.dp),
            border = BorderStroke(1.dp, Color.Gray),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            EstablishmentCardContent(getBookingResponseDTO, planning)
        }
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
private fun FailureState(errorMessage: String?, setErrorMessage: (String) -> Unit) {
    setErrorMessage(errorMessage ?: "An unexpected error occurred.")
}
fun handleCardClick(
    planning: PlanningDTO,
    getBookingResponseDTO: GetBookingResponseDTO,
    bookingViewModel: BookingViewModel,
    navController: NavController,
    isUserLoggedIn: Boolean,
    onLoginRequired: () -> Unit,
    saveBookingViewModel: SaveBookingViewModel,
    viewModel1: ExtrasViewModel

) {
    val selectedTimeSlot = "${planning.fromStr} - ${planning.toStr}"
    val amountSelected = getBookingResponseDTO.amount
    val currencySymbol = getBookingResponseDTO.currencySymbol
    val establishmentName = getBookingResponseDTO.establishmentDTO?.name ?: "Unknown"

    val selectedReservation = ReservationOption(
        name = establishmentName,
        time = selectedTimeSlot,
        price = "$currencySymbol${amountSelected ?: 0.0}",
        date = "90 min"
    )

    // Update ViewModel with selected bookings using the updateBookings method
    bookingViewModel.updateBookings(listOf(getBookingResponseDTO))
    Log.d("BookingViewModel", "updateBookings called")
    viewModel1.Extras() // Make sure you call this before calling SaveBooking

    if (!isUserLoggedIn) {
        onLoginRequired()
    } else {
        // Accessing LiveData and mapping it to domain model
        val mappedBookings = bookingViewModel.selectedBookings.value?.toDomain() ?: emptyList()

        saveBookingViewModel.SaveBooking(mappedBookings)

        val url = "PaymentSection1/${amountSelected}/${currencySymbol}/${establishmentName}/${selectedTimeSlot}/90 min"
        navController.navigate(url)
    }
}

