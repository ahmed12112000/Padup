package com.nevaDev.padeliummarhaba.ui.views

import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevaDev.padeliummarhaba.models.ReservationOption
import java.time.LocalDate
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payment
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.gson.Gson
import com.nevaDev.padeliummarhaba.viewmodels.ExtrasViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.dto.SaveBookingRequest
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.domain.dto.GetBookingResponse
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


fun List<GetBookingResponseDTO>.toDomain(): List<GetBookingResponse> {
    return this.map { dto ->
        val startFormatted = dto.start?.let {
            // Parse the Instant and format it
            Instant.parse(it.toString()).atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        } ?: "2024-12-20 9:30" // Default fallback value if start is null
        val endFormatted = if (dto.end.isNullOrBlank()) {
            // Provide a default value or leave it as null if end is empty or blank
            "2024-12-20 9:30"  // Example default fallback value
        } else {
            // Parse the Instant and format it
            Instant.parse(dto.end).atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
        }

        GetBookingResponse(

            // Map fields from GetBookingResponseDTO to GetBookingResponse
            aamount = dto.aamount ?: BigDecimal.ZERO,
            amount = dto.amount ?: 0.0,
            amountfeeTrans = dto.amountfeeTrans ?: BigDecimal.ZERO,
            bookingAnnulationDTOSet = dto.bookingAnnulationDTOSet ?: emptyList(),
            isClient = dto.isClient ?: true,
            closeTime = dto.closeTime?.toString() ?: Instant.now().toString(), // Ensure ISO 8601 format

            couponCode = dto.couponCode ?: "",
            currencyId = dto.currencyId ?: 0L,
            currencySymbol = dto.currencySymbol ?: "",
            decimalNumber = dto.decimalNumber ?: 0,
            description = dto.description ?: "",
            end = endFormatted,
            establishmentDTO = dto.establishmentDTO,
            establishmentPacksDTO = dto.establishmentPacksDTO ?: emptyList(),
            establishmentPacksId = dto.establishmentPacksId ?: 0L,
            EstablishmentPictureDTO = dto.EstablishmentPictureDTO ?: emptyList(),
            facadeUrl = dto.facadeUrl ?: "",
            from = dto.from?.let { Instant.parse(it.toString()).toString() } ?: Instant.now().toString(),
            HappyHours = dto.HappyHours ?: emptyList(),
            mgAmount = dto.mgAmount ?: BigDecimal.ZERO,
            moyFeed = dto.moyFeed ?: 0.0,
            numberOfPart = dto.numberOfPart ?: 0.0,
            numberOfPlayer = dto.numberOfPlayer ?: 0,
            openTime = dto.openTime?.toString() ?: Instant.now().toString(),
            payFromAvoir = dto.payFromAvoir ?: false,
            plannings = dto.plannings ?: emptyList(),
            privateExtrasIds = dto.privateExtrasIds ?: emptyList(),
            ramountfeeTrans = dto.ramountfeeTrans ?: BigDecimal.ZERO,
            reduction = dto.reduction ?: BigDecimal.ZERO,
            reductionAmount = dto.reductionAmount ?: BigDecimal.ZERO,
            reductionSecondAmount = dto.reductionSecondAmount ?: BigDecimal.ZERO,
            reductionaAmount = dto.reductionaAmount ?: BigDecimal.ZERO,
            reductionaSecondAmount = dto.reductionaSecondAmount ?: BigDecimal.ZERO,
            rsamountfeeTrans = dto.rsamountfeeTrans ?: BigDecimal.ZERO,
            samountfeeTrans = dto.samountfeeTrans ?: BigDecimal.ZERO,
            searchDate = dto.searchDate ?: "",
            secondAamount = dto.secondAamount ?: BigDecimal.ZERO,
            secondAmount = dto.secondAmount ?: BigDecimal.ZERO,
            secondReduction = dto.secondReduction ?: 0,
            sharedExtrasIds = dto.sharedExtrasIds ?: emptyList(),
            to = dto.to?.let { Instant.parse(it.toString()).toString() } ?: Instant.now().toString(),
            start = startFormatted,
            totalFeed = dto.totalFeed ?: 0,
            users = dto.users ?: emptyList(),
            usersIds = dto.usersIds ?: emptyList(),
            withSecondPrice = dto.withSecondPrice ?: false,
            orderId = dto.orderId ?: 0L,
            id = dto.id ?: 0L,
            )
    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaymentSection1(
    selectedDate: LocalDate,
    selectedTimeSlot: String,
    selectedReservation: ReservationOption,
    onExtrasUpdate: (Int, Int, Boolean) -> Unit,
    onPayWithCardClick: () -> Unit,
    totalAmount: String,
    navController: NavController,
    viewModel: SaveBookingViewModel = hiltViewModel(),
    viewModel1: PaymentViewModel = hiltViewModel(),
    paymentRequest: PaymentRequest,
    amountSelected: Double,
    currencySymbol: String,
    bookingViewModel: BookingViewModel, // Use the shared BookingViewModel

) {
    val saveBookingViewModel: SaveBookingViewModel = hiltViewModel()
    val paymentViewModel: PaymentViewModel = hiltViewModel()
    val viewModel2: ExtrasViewModel = hiltViewModel()

    var additionalExtrasEnabled by remember { mutableStateOf(false) }
    val selectedExtras = remember { mutableStateListOf<Triple<String, String, Int>>() }

    var partnerName by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedParts by remember { mutableStateOf("1") }
    var phoneNumber by remember { mutableStateOf("") }
    var extrasEnabled by remember { mutableStateOf(false) }
    var selectedRaquette by remember { mutableStateOf(1) }
    var includeBalls by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val extrasCost = (if (includeBalls) 5 else 0) + (selectedRaquette * 2)
    onExtrasUpdate(extrasCost, selectedRaquette, includeBalls)

    var totalExtrasCost by remember { mutableStateOf(0.0) }
    onExtrasUpdate(totalExtrasCost.toInt(), selectedRaquette, includeBalls)
    onExtrasUpdate(totalExtrasCost.toInt(), selectedRaquette, includeBalls)



    val selectedBookingsJson = navController.previousBackStackEntry
        ?.arguments
        ?.getString("selectedBookings")
    val selectedBookings = remember {
        selectedBookingsJson?.let {
            Gson().fromJson(it, Array<GetBookingResponseDTO>::class.java).toList()
        } ?: emptyList()
    }


    // Map the DTOs to domain objects
    val dataResult by viewModel.dataResult.observeAsState()





    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())

    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Text(
                    text = "Requis pour votre réservation",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(18.dp))

                HorizontalDivider(
                    modifier = Modifier
                        .width(900.dp)
                        .padding(horizontal = 10.dp)
                        .offset(y = -10.dp),
                    color = Color.Gray, thickness = 1.dp)

                Spacer(modifier = Modifier.height(8.dp))

                Text(text = "    Numéro de téléphone",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(18.dp))

                HorizontalDivider(
                    modifier = Modifier
                        .width(900.dp)
                        .padding(horizontal = 10.dp)
                        .offset(y = -10.dp),
                    color = Color.Gray, thickness = 1.dp)

                // Phone number input with pre-filled number and "Modifier" button
                Row(
                    verticalAlignment = Alignment.CenterVertically)
                {

                    BasicTextField(
                        value = phoneNumber,
                        onValueChange = {
                            if (it.all { char -> char.isDigit() } && it.length <= 8) {
                                phoneNumber = it

                            }
                        },
                        modifier = Modifier
                            .offset(x = 3.dp)
                            .width(200.dp)
                            .padding(vertical = 4.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        decorationBox = { innerTextField ->
                            Row(
                                modifier = Modifier
                                    .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Phone Icon
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Phone Icon",
                                    modifier = Modifier.padding(8.dp)
                                )
                                androidx.compose.material.Text(
                                    text = "|",
                                    fontSize = 29.sp,
                                    color = Color.Black,
                                    modifier = Modifier.offset(x = -8.dp, y = -2.dp)
                                )
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    innerTextField()
                                }


                            }
                        }
                    )

                    Spacer(modifier = Modifier.width(10.dp))


                    Button(
                        onClick = {
                        },
                        modifier = Modifier
                            .offset(x=28.dp)
                            .width(120.dp)
                            .height(48.dp)
                            .border(1.dp, Color(0xFF0054D8), RoundedCornerShape(13.dp)),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.White, RoundedCornerShape(10.dp))
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Modifier",
                                color = Color(0xFF0054D8),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }

                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        ReservationDetailsCard(
            selectedExtras = selectedExtras,
            onExtrasUpdate = { updatedExtras, updatedTotalCost ->
                // Handle the updated extras list and total cost
                selectedExtras.clear()
                selectedExtras.addAll(updatedExtras)

                // Update the total cost or perform other actions
                val updatedCost = updatedTotalCost
            },
            selectedReservation = selectedReservation,
            viewModel2 = viewModel2
        )






        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            var selectedExtras by remember { mutableStateOf<List<Triple<String, String, Int>>>(emptyList()) }
            var totalExtrasCost by remember { mutableStateOf(0.0) }

            ExtrasSection(
                onExtrasUpdate = { extras, cost ->
                    selectedExtras = extras
                    totalExtrasCost = cost
                }
            )
            ReservationSummary(
                selectedDate = selectedDate,
                selectedTimeSlot = selectedTimeSlot ?: "Not selected",
                selectedReservation = selectedReservation ?: ReservationOption("Default", "Not selected", "100.0", "Not selected"),
                selectedExtras = selectedExtras, // Pass the selectedExtras list directly
                selectedRaquette = selectedRaquette.toString(),
                includeBalls = includeBalls,
                amountSelected = selectedReservation?.let {
                    val price = it.price.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0
                    val currencySymbol = it.price.replace("[\\d.]".toRegex(), "") // Extract currency symbol
                    Pair(price, currencySymbol)
                },
                onTotalAmountCalculated = { totalAmountSelected, currencySymbol ->
                    Log.d("TotalAmountSelected", "The total amount is $totalAmountSelected and currency symbol is $currencySymbol")
                },

            )






        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = -10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = -10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {


                val selectedBookings by bookingViewModel.selectedBookings.observeAsState(emptyList())

                Button(
                    onClick = {
                        isLoading = true

                        selectedBookings.forEach { booking ->
                            Log.d("SelectedBooking", "Establishment: ${booking.establishmentDTO?.name}")
                            Log.d("SelectedBooking", "Amount: ${booking.amount} ${booking.currencySymbol}")
                        }
                        val totalAmount1 = selectedBookings.sumOf {
                            it.amount.toString().toDoubleOrNull() ?: 0.0
                        }

                        val mappedBookings = selectedBookings.toDomain()
                        val paymentRequest = PaymentRequest(
                            amount = totalAmount1.toString(),
                            currency = selectedBookings.firstOrNull()?.currencySymbol ?: "",
                            orderId = "OrderID-${System.currentTimeMillis()}"
                        )


                        saveBookingViewModel.SaveBooking(mappedBookings)
                        saveBookingViewModel.dataResult.observe(lifecycleOwner) { result ->
                            when (result) {
                                is DataResult.Loading -> {
                                    // Show a loading state (e.g., a progress indicator)
                                    isLoading = true
                                    Log.d("SaveBooking", "Saving booking...")
                                }
                                is DataResult.Success -> {
                                    // Handle the success case, navigate to the next screen, or show a success message
                                    isLoading = false
                                    Log.d("SaveBooking", "Booking saved successfully!")
                                    result.data?.let { savedData ->
                                        Log.d("SaveBooking", "Saved Data: $savedData")
                                    }

                                    paymentViewModel.Payment(paymentRequest)
                                    paymentViewModel.dataResult.observe(lifecycleOwner) { paymentResult ->
                                        when (paymentResult) {
                                            is DataResult.Loading -> {
                                                // Handle loading state for payment
                                                Log.d("Payment", "Processing payment...")
                                            }
                                            is DataResult.Success -> {
                                                // Handle success, navigate to confirmation or show success message
                                                Log.d("Payment", "Payment processed successfully!")
                                                navController.navigate("confirmation_screen") {
                                                    popUpTo("payment_screen") { inclusive = true }
                                                }
                                            }
                                            is DataResult.Failure -> {
                                                // Handle payment failure
                                                Log.e("Payment", "Payment failed: ${paymentResult.errorMessage}")
                                                Toast.makeText(
                                                    context,
                                                    "Payment failed: ${paymentResult.errorMessage}",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                    }
                                }
                                is DataResult.Failure -> {
                                    // Show an error message or handle failure
                                    isLoading = false
                                    Log.e("SaveBooking", "Error saving booking: ${result.errorMessage}")
                                    Toast.makeText(
                                        context,
                                        "Failed to save booking: ${result.errorMessage}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }



                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    border = BorderStroke(2.dp, Color(0xFF0054D8))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Payment,
                            contentDescription = "Card Payment",
                            tint = Color(0xFF0054D8)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Card Crédit",
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }
                }

                Button(
                    onClick = {},
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    border = BorderStroke(2.dp, Color(0xFF0054D8))
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Money,
                            contentDescription = "Credits Payment",
                            tint = Color(0xFF0054D8)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Crédit Padelium",
                            fontWeight = FontWeight.Bold,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

    }
}


@Composable
fun WebViewScreen(paymentUrl: String, navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    loadUrl(paymentUrl)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.Black
            )
        }
    }
}








