package com.nevaDev.padeliummarhaba.ui.views

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.nevaDev.padeliummarhaba.viewmodels.BalanceViewModel
import com.nevaDev.padeliummarhaba.viewmodels.ConfirmBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.ExtrasViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetPaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPayAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PaymentRequest
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.domain.dto.ConfirmBookingRequest
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.GetPaymentRequest
import com.padelium.domain.dto.PaymentResponse
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.padelium.domain.dto.SaveBookingResponse // Adjust import as necessary
import com.padelium.domain.dto.bookingIds
import java.util.UUID

fun List<GetBookingResponseDTO>.toDomain(): List<GetBookingResponse> {
    val formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

    // Function to safely parse the date and return the formatted result or fallback
    val parseDateSafely: (String?) -> String = { rawDate ->
        rawDate?.let {
            try {
                // Use the current date with a fixed time of 11:00
                val currentDate = LocalDate.now()
                val fixedDateTime = LocalDateTime.of(currentDate, LocalTime.of(11, 0))

                // If the raw date is not null, parse and format it to the desired output format
                // This will ignore the time part of the original date and always return 11:00
                fixedDateTime.format(formatterOutput)

            } catch (e: Exception) {
                // Fallback to current date with fixed time of 11:00 if parsing fails
                val currentDate = LocalDate.now()
                val fixedDateTime = LocalDateTime.of(currentDate, LocalTime.of(11, 0))
                fixedDateTime.format(formatterOutput)
            }
        } ?: run {
            // Fallback if rawDate is null, return the current date with time 11:00
            val currentDate = LocalDate.now()
            val fixedDateTime = LocalDateTime.of(currentDate, LocalTime.of(11, 0))
            fixedDateTime.format(formatterOutput)
        }
    }

    return this.map { dto ->
        val startFormatted = dto.plannings?.firstOrNull()?.fromStr?.let { timeStr ->
            val formattedTimeStr = if (timeStr.length == 4) "0$timeStr" else timeStr
            val currentDate = LocalDate.now()
            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
            LocalDateTime.of(currentDate, time).format(formatterOutput)
        } ?: "2024-12-20 09:30" // Default fallback value if fromStr is null

        val endFormatted = dto.plannings?.firstOrNull()?.toStr?.let { timeStr ->
            val formattedTimeStr = if (timeStr.length == 4) "0$timeStr" else timeStr
            val currentDate = LocalDate.now()
            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
            LocalDateTime.of(currentDate, time).format(formatterOutput)
        } ?: "2024-12-20 09:30"  // Default fallback value if toStr is null

        GetBookingResponse(
            aamount = dto.aamount ?: BigDecimal.ZERO,
            amount = dto.amount ?: BigDecimal.ZERO,
            amountfeeTrans = dto.amountfeeTrans ?: BigDecimal.ZERO,
            bookingAnnulationDTOSet = dto.bookingAnnulationDTOSet ?: emptyList(),
            isClient = dto.isClient ?: true,
            closeTime = dto.closeTime?.toString() ?: Instant.now().toString(),

            couponCode = dto.couponCode ?: "",
            currencyId = dto.currencyId ?: 0L,
            currencySymbol = dto.currencySymbol ?: "",
            decimalNumber = dto.decimalNumber ?: 0,
            description = dto.description ?: "",
            end = endFormatted,
            establishmentDTO = dto.establishmentDTO,
            establishmentPacksDTO = dto.establishmentPacksDTO ?: emptyList(),

            // Explicitly convert 0L to null
            establishmentPacksId = if (dto.establishmentPacksId == 0L) null else dto.establishmentPacksId,

            EstablishmentPictureDTO = dto.EstablishmentPictureDTO ?: emptyList(),
            facadeUrl = dto.facadeUrl ?: "",
            from = dto.from?.let { Instant.parse(it.toString()).toString() } ?: Instant.now().toString(),
            HappyHours = dto.HappyHours ?: emptyList(),
            mgAmount = dto.mgAmount ?: BigDecimal.ZERO,
            moyFeed = dto.moyFeed ?: 0.0,
            numberOfPart =  dto.numberOfPart ?: 0,
            numberOfPlayer = dto.numberOfPlayer ?: 0,
            openTime = dto.openTime?.toString() ?: Instant.now().toString(),
            payFromAvoir = dto.payFromAvoir ?: false,
            plannings = dto.plannings ?: emptyList(),
            ramountfeeTrans = dto.ramountfeeTrans ?: BigDecimal.ZERO,
            reduction = dto.reduction ?: 0,
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
            userIds = dto.userIds ?: emptyList(),
            withSecondPrice = dto.withSecondPrice ?: false,
            orderId = dto.orderId ?: 0L,
            id = dto.id ?: 0L,
            privateExtrasIds = dto.privateExtrasIds ?: emptyList(),
            buyerId = dto.buyerId ?: "",
            couponIds = dto.couponIds ?: emptyMap(),
            )
    }
}



@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaymentSection1(
    selectedDate: LocalDate,
    selectedReservation: ReservationOption,
    onExtrasUpdate: (Int, Int, Boolean) -> Unit,
    navController: NavController,
    viewModel: SaveBookingViewModel = hiltViewModel(),
    bookingViewModel: GetBookingViewModel,
    price: String, // Add price as a parameter
    selectedTimeSlot: String, // Ensure this is passed
    mappedBookingsJson: String ,
    onTotalAmountCalculated: (Double, String) -> Unit // Add this parameter

) {

    // Deserialize JSON into List<GetBookingResponse>
    val type = object : TypeToken<List<GetBookingResponse>>() {}.type
    val mappedBookings: List<GetBookingResponse> = Gson().fromJson(mappedBookingsJson, type)
    val saveBookingViewModel: SaveBookingViewModel = hiltViewModel()
    val paymentPayAvoirViewModel: PaymentPayAvoirViewModel = hiltViewModel()
    val confirmBookingViewModel: ConfirmBookingViewModel = hiltViewModel()
    val balanceViewModel: BalanceViewModel = hiltViewModel()

    val paymentViewModel: PaymentViewModel = hiltViewModel()
    val GetPaymentViewModel: GetPaymentViewModel = hiltViewModel()

    val viewModel2: ExtrasViewModel = hiltViewModel()
    val selectedTimeSlot = remember { mutableStateOf<String?>(null) }
    val time = remember { mutableStateOf<String?>(null) }

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
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(18.dp))

                HorizontalDivider(
                    modifier = Modifier
                        .width(900.dp)
                        .padding(horizontal = 10.dp)
                        .offset(y = -10.dp),
                    color = Color.Gray, thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "    Numéro de téléphone",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(18.dp))

                HorizontalDivider(
                    modifier = Modifier
                        .width(900.dp)
                        .padding(horizontal = 10.dp)
                        .offset(y = -10.dp),
                    color = Color.Gray, thickness = 1.dp
                )

                // Phone number input with pre-filled number and "Modifier" button
                Row(
                    verticalAlignment = Alignment.CenterVertically
                )
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
                            .offset(x = 28.dp)
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
            var selectedExtras by remember {
                mutableStateOf<List<Triple<String, String, Int>>>(
                    emptyList()
                )
            }
            var totalExtrasCost by remember { mutableStateOf(0.0) }

            ExtrasSection(
                onExtrasUpdate = { extras, cost ->
                    selectedExtras = extras
                    totalExtrasCost = cost
                }
            )

            // Pass selectedTimeSlot and ReservationOption correctly
            ReservationSummary(
                selectedDate = selectedDate,
                selectedTimeSlot = selectedTimeSlot.toString(), // Pass selected time
                selectedReservation = selectedReservation, // Pass selected reservation
                selectedExtras = selectedExtras,
                amountSelected = Pair(
                    selectedReservation.price.replace("[^\\d.]".toRegex(), "").toDoubleOrNull()
                        ?: 0.0,
                    selectedReservation.price.takeWhile { !it.isDigit() && it != '.' }
                ),
                onTotalAmountCalculated = { totalAmount, currency ->
                    Log.d("TotalAmount", "Calculated total: $totalAmount $currency")
                },
                price =price,
                time = time.toString(),
                navController = navController,
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


                Button(
                    onClick = {
                        isLoading = true

                        val selectedBooking = mappedBookings.firstOrNull()

                        if (selectedBooking != null) {
                            val reservationPrice = selectedReservation.price.replace("[^\\d.,]".toRegex(), "").toDoubleOrNull() ?: 0.0
                            // Calculate total amount selected
                            val totalAmountSelected = reservationPrice + totalExtrasCost
                            Log.d("Button", "Calculateddddddd total amount: $totalAmountSelected")

                            // Check if the amount is valid
                            if (totalAmountSelected <= 0) {
                                Toast.makeText(context, "Total amount not calculated or is zero", Toast.LENGTH_LONG).show()
                                isLoading = false
                                return@Button
                            }

                            // Update the first booking's amount
                            val updatedMappedBookings = mappedBookings.mapIndexed { index, booking ->
                                if (index == 0) {
                                    booking.copy(amount = BigDecimal(totalAmountSelected)) // Update amount here
                                } else {
                                    booking
                                }
                            }
                            Log.d("Ahmed", "Updated mappedBookings: ${updatedMappedBookings[0].amount}")

                            val totalAmountBigDecimal = BigDecimal.valueOf(totalAmountSelected)
                            Log.d("Button", "Passing BigDecimal amount to ViewModel: $totalAmountBigDecimal")

                            // Call the onTotalAmountCalculated with totalAmountSelected and currency (you can extract currency from selectedReservation or set it manually)
                            val currency = selectedReservation.price.takeWhile { !it.isDigit() && it != '.' }
                            onTotalAmountCalculated(totalAmountSelected, currency)

                            // Call ViewModel method
                            paymentPayAvoirViewModel.PaymentPayAvoir(totalAmountBigDecimal)

                            // Save the updated bookings
                            saveBookingViewModel.SaveBooking(updatedMappedBookings)
                            saveBookingViewModel.dataResult.observe(lifecycleOwner) { result ->
                                when (result) {
                                    is DataResult.Loading -> {
                                        isLoading = true
                                        Log.d("SaveBooking", "Saving booking...")
                                    }

                                    is DataResult.Success -> {
                                        isLoading = false
                                        Log.d("SaveBooking", "Booking saved successfully!")

                                        val bookingList = result.data as? List<SaveBookingResponse>
                                        if (!bookingList.isNullOrEmpty()) {
                                            val firstBooking = bookingList[0]
                                            val bookingId = firstBooking.id.toString()

                                            // Create a PaymentRequest using the updated amount
                                            val paymentRequest = PaymentRequest(
                                                amount = totalAmountSelected.toString(),
                                                currency = selectedBooking.currencySymbol ?: "EUR",
                                                orderId = bookingId
                                            )

                                            // Process the payment
                                            paymentViewModel.Payment(paymentRequest)
                                            paymentViewModel.dataResult.observe(lifecycleOwner) { paymentResult ->
                                                when (paymentResult) {
                                                    is DataResult.Loading -> {
                                                        Log.d("Payment", "Processing payment...")
                                                    }

                                                    is DataResult.Success -> {
                                                        Log.d("Payment", "Payment processed successfully!")

                                                        val paymentResponse = paymentResult.data as? PaymentResponse
                                                        val formUrl = paymentResponse?.formUrl
                                                        val orderId = paymentResponse?.orderId

                                                        if (!formUrl.isNullOrEmpty() && !orderId.isNullOrEmpty()) {
                                                            // Navigate to WebViewScreen with the form URL
                                                            navController.navigate("WebViewScreen?paymentUrl=${Uri.encode(formUrl)}")

                                                            // Create and process GetPaymentRequest
                                                            val getPaymentRequest = GetPaymentRequest(
                                                                bookingIds = bookingList.mapNotNull { it.id },
                                                                couponIds = selectedBooking.couponIds ?: emptyMap(),
                                                                numberOfPart = selectedBooking.numberOfPart,
                                                                orderId = orderId,
                                                                privateExtrasIds = selectedBooking.privateExtrasIds ?: emptyList(),
                                                                sharedExtrasIds = selectedBooking.sharedExtrasIds ?: emptyList(),
                                                                userIds = selectedBooking.userIds ?: emptyList()
                                                            )
                                                            GetPaymentViewModel.GetPayment2(getPaymentRequest)
                                                        } else {
                                                            Log.e("Payment", "No form URL found in the response.")
                                                            Toast.makeText(
                                                                context,
                                                                "Payment failed: No form URL received.",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        }
                                                    }

                                                    is DataResult.Failure -> {
                                                        Log.e("Payment", "Payment failed: ${paymentResult.errorMessage}")
                                                        Toast.makeText(
                                                            context,
                                                            "Payment failed: ${paymentResult.errorMessage}",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                }
                                            }
                                        } else {
                                            Log.e("SaveBooking", "Failed to retrieve booking list.")
                                            Toast.makeText(context, "Failed to retrieve booking ID.", Toast.LENGTH_LONG).show()
                                        }
                                    }

                                    is DataResult.Failure -> {
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
                        } else {
                            isLoading = false
                            Toast.makeText(context, "No valid booking data available.", Toast.LENGTH_LONG).show()
                        }
                    },

                    enabled = !isLoading,

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
                var amount by remember { mutableStateOf(BigDecimal.ZERO) }

                Button(
                    onClick = {
                        // Select the first booking from the mappedBookings
                        val selectedBooking = mappedBookings.firstOrNull()

                        if (selectedBooking != null) {
                            // Try to parse the price, handling invalid input
                            amount = try {
                                BigDecimal(price.toDouble())
                            } catch (e: NumberFormatException) {
                                Toast.makeText(context, "Invalid price format", Toast.LENGTH_LONG).show()
                                return@Button
                            }

                            // Call PaymentPayAvoirViewModel
                            paymentPayAvoirViewModel.PaymentPayAvoir(amount)

                            // Fetch and update balance
                            balanceViewModel.fetchAndBalance()

                            // Save the booking
                            saveBookingViewModel.SaveBooking(mappedBookings)

                            // Only observe the result for confirmBooking logic here
                            saveBookingViewModel.dataResult.observe(lifecycleOwner) { result ->
                                when (result) {
                                    is DataResult.Loading -> {
                                        isLoading = true
                                        Log.d("SaveBooking", "Saving booking...")
                                    }

                                    is DataResult.Success -> {
                                        isLoading = false
                                        Log.d("SaveBooking", "Booking saved successfully!")

                                        val bookingList = result.data as? List<SaveBookingResponse>
                                        if (!bookingList.isNullOrEmpty()) {
                                            val confirmBookingRequest = ConfirmBookingRequest(
                                                amount = amount,
                                                numberOfPart = mappedBookings.first().numberOfPart,
                                                payFromAvoir = true,
                                                privateExtrasIds = mappedBookings.first().privateExtrasIds ?: emptyList(),
                                                bookingIds = bookingList.mapNotNull { it.id },
                                                buyerId = mappedBookings.first().buyerId ?: "",
                                                couponIds = mappedBookings.first().couponIds ?: emptyMap(),
                                                sharedExtrasIds = mappedBookings.first().sharedExtrasIds ?: emptyList(),
                                                status = true,
                                                token = "",
                                                transactionId = "",
                                                userIds = mappedBookings.first().userIds ?: emptyList()
                                            )

                                            // Call ConfirmBooking API
                                            confirmBookingViewModel.GetPayment(confirmBookingRequest)
                                        } else {
                                            Toast.makeText(context, "No booking data available.", Toast.LENGTH_LONG).show()
                                        }
                                    }

                                    is DataResult.Failure -> {
                                        isLoading = false
                                        Toast.makeText(context, "Failed to save booking.", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }

                        }
                    },
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





class WebAppInterface(private val context: Context, private val navController: NavController) {
    @JavascriptInterface
    fun onPaymentSuccess(url: String) {
        // Navigate to PaymentSuccessScreen when payment is successful
        if (url.contains("paymentSuccess")) {
            Log.d("WebAppInterface", "Payment was successful: $url")
            Toast.makeText(context, "Payment Successful", Toast.LENGTH_LONG).show()
            navController.navigate("PaymentSuccessScreen")
        }
    }
}



@Composable
fun WebViewScreen(formUrl: String, navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    loadUrl(formUrl)
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
/*

@Composable
fun WebViewScreen(navController: NavController, formUrl: String) {
    val context = LocalContext.current

    AndroidView(factory = {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true // Enable DOM storage if required

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    url?.let {
                        if (it.contains("paymentSuccess")) {
                            Log.e("paymentSuccess", "Payment successful URL: $it")
                            Toast.makeText(context, "Payment Successful", Toast.LENGTH_LONG).show()
                            navController.navigate("PaymentSuccessScreen")
                            return true // Intercept the URL loading
                        }
                    }
                    return super.shouldOverrideUrlLoading(view, url)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Log.d("WebView", "Page finished loading: $url")
                }
            }

            loadUrl(formUrl)
        }
    })
}





// Show a success message when the payment is successful
fun showPaymentSuccessMessage(context: Context) {
    Toast.makeText(context, "Payment Successful!", Toast.LENGTH_LONG).show()
}


*/







