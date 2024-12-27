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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.nevaDev.padeliummarhaba.models.ReservationOption
import com.nevaDev.padeliummarhaba.viewmodels.ExtrasViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.data.repositoriesImp.SaveBookingRequestSerializer
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.SaveBookingRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@HiltViewModel
class BookingViewModel0 @Inject constructor(
) : ViewModel() {
    var selectedBookings: List<GetBookingResponseDTO> = emptyList()
    val gson = GsonBuilder().create()

    // Other methods and properties for your ViewModel
}

@Composable
fun ReservationOptions0(
    onReservationSelected: (ReservationOption) -> Unit,
    isUserLoggedIn: Boolean,
    key: String?,
    navController: NavController,
    viewModel: GetBookingViewModel = hiltViewModel(),
    viewModel1: ExtrasViewModel = hiltViewModel(),
    selectedDate: LocalDate,
    selectedTimeSlot: String?,
    viewModel2 :  ViewModel = hiltViewModel(),
    bookingViewModel: BookingViewModel = hiltViewModel(),


) {
    var amountSelected by remember { mutableStateOf<Double?>(null) }
    var currentSelectedTimeSlot by remember { mutableStateOf<String?>(null) }
    var currencySymbol by remember { mutableStateOf<String?>(null) }
    val dayFormatter = DateTimeFormatter.ofPattern("EEE d MMM")
    val dataResultBooking by viewModel.dataResultBooking.observeAsState(initial = DataResultBooking.Loading)
    val filteredTimeSlots by viewModel.filteredTimeSlots.observeAsState(emptyList())
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var showLoginPopup by remember { mutableStateOf(false) }
    var selectedReservation by remember { mutableStateOf<ReservationOption?>(null) }

    var selectedTimeSlot by remember { mutableStateOf<String?>(null) }
    var selectedFromStr by remember { mutableStateOf<String?>(null) }
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val saveBookingViewModel: SaveBookingViewModel = hiltViewModel()
    val gson = GsonBuilder()
        .registerTypeAdapter(SaveBookingRequest::class.java, SaveBookingRequestSerializer())
        .create()


    val selectedBookings = remember { mutableStateListOf<GetBookingResponseDTO>() }

    LaunchedEffect(key) {
        key?.let { viewModel.getBooking(it) }
    }
    LaunchedEffect(Unit) {
        viewModel1.Extras() // Fetch extras
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
                LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                    items(establishmentsList) { getBookingResponseDTO ->
                        val establishmentName = getBookingResponseDTO.establishmentDTO?.name ?: "Unknown"
                        val availablePlannings = getBookingResponseDTO.plannings.filter { planning ->
                            filteredTimeSlots.any { slot -> slot.time == planning.fromStr }
                        }

                        availablePlannings.forEach { planning ->
                            if (selectedFromStr == null || selectedFromStr == planning.fromStr) {
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 6.dp)
                                        .clickable {
                                            currentSelectedTimeSlot = "${planning.fromStr} - ${planning.toStr}"
                                            amountSelected = getBookingResponseDTO.amount
                                            currencySymbol = getBookingResponseDTO.currencySymbol

                                            selectedReservation = ReservationOption(
                                                name = establishmentName,
                                                time = "${planning.fromStr} ${planning.toStr}",
                                                price = "$currencySymbol${amountSelected ?: 0.0}",
                                                date = "90 min"
                                            )

                                            //bookingViewModel.selectedBookings = listOf(getBookingResponseDTO)


                                           // bookingViewModel.selectedBookings.forEach { booking ->
                                             //   Log.d("SelectedBooking", "Establishment: ${booking.establishmentDTO?.name}, Amount: ${booking.amount}, Currency: ${booking.currencySymbol}, From: ${booking.from}, id: ${booking.id}")

                                       //     }
                                            if (!isUserLoggedIn) {
                                                showLoginPopup = true
                                            } else {
                                             //   val mappedBookings = bookingViewModel.selectedBookings.toDomain()


                                                // Call the ViewModel to save the booking
                                             //   saveBookingViewModel.SaveBooking(mappedBookings)
                                                val selectedBookingsJson = Gson().toJson(bookingViewModel.selectedBookings)

                                                val url = "PaymentSection1/${amountSelected}/${currencySymbol}/${establishmentName}/${planning.fromStr} ${planning.toStr}/90 min?selectedBookings=${Uri.encode(Gson().toJson(selectedBookings))}"
                                                navController.navigate(url)

                                            }
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, Color.Gray),
                                    elevation = CardDefaults.cardElevation(4.dp) // Wrap the elevation value here
                                ) {
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
                                                text = "90 min",
                                                fontSize = 16.sp,
                                                color = Color.Gray
                                            )
                                        }

                                        Button(
                                            onClick = {

                                                if (!isUserLoggedIn) {
                                                    showLoginPopup = true
                                                } else {



                                                }


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
                            }
                        }
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
        is DataResultBooking.Failure -> {
            errorMessage = result.errorMessage ?: "An unexpected error occurred."
        }
    }

    if (showLoginPopup) {
        // Handle login popup logic
    }

    errorMessage?.let {
        Text(text = it, color = Color.Red, modifier = Modifier.padding(16.dp))
    }
}