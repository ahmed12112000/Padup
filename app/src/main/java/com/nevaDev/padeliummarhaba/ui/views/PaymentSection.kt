package com.nevaDev.padeliummarhaba.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.CookieManager
import android.webkit.JavascriptInterface
import android.webkit.WebResourceRequest
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
import androidx.compose.foundation.layout.size
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.os.bundleOf
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.nevaDev.padeliummarhaba.viewmodels.BalanceViewModel
import com.nevaDev.padeliummarhaba.viewmodels.ConfirmBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.ExtrasViewModel
import com.nevaDev.padeliummarhaba.viewmodels.FindTermsViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetEmailViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetManagerViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetPaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPayAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.UpdatePhoneViewModel
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PaymentRequest
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.data.dto.GetPaymentRequestDTO
import com.padelium.domain.dto.ConfirmBookingRequest
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.GetPaymentRequest
import com.padelium.domain.dto.GetProfileResponse
import com.padelium.domain.dto.PaymentResponse
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.padelium.domain.dto.SaveBookingResponse // Adjust import as necessary
import com.padelium.domain.dto.bookingIds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.math.RoundingMode
import java.util.UUID

fun List<GetBookingResponseDTO>.toDomain(): List<GetBookingResponse> {
    val formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val formatterWithMillis = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")



    return this.map { dto ->
        val startFormatted = dto.plannings?.firstOrNull()?.fromStr?.let { timeStr ->
            val formattedTimeStr = if (timeStr.length == 4) "0$timeStr" else timeStr
            val currentDate = LocalDate.now()
            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
            LocalDateTime.of(currentDate, time).format(formatterOutput)
        } ?: "2024-12-20 09:30"

        val endFormatted = dto.plannings?.firstOrNull()?.toStr?.let { timeStr ->
            val formattedTimeStr = if (timeStr.length == 4) "0$timeStr" else timeStr
            val currentDate = LocalDate.now()
            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
            LocalDateTime.of(currentDate, time).format(formatterOutput)
        } ?: "2024-12-20 09:30"

        val fromFormatted = dto.plannings?.firstOrNull()?.fromStr?.let { timeStr ->
            val formattedTimeStr = if (timeStr.length == 4) "0$timeStr" else timeStr
            val currentDate = LocalDate.now()
            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
            LocalDateTime.of(currentDate, time).format(formatterWithMillis)
        } ?: "2024-12-20T09:30:00.000Z"

        val toFormatted = dto.plannings?.firstOrNull()?.toStr?.let { timeStr ->
            val formattedTimeStr = if (timeStr.length == 4) "0$timeStr" else timeStr
            val currentDate = LocalDate.now()
            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
            LocalDateTime.of(currentDate, time).format(formatterWithMillis)
        } ?: "2024-12-20T09:30:00.000Z"

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
            from =fromFormatted,
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
            to =toFormatted,
            start = startFormatted,
            totalFeed = dto.totalFeed ?: 0,
            users = dto.users ?: emptyList(),
            userIds = dto.userIds ,
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
fun  PaymentSection1(
    selectedDate: LocalDate,
    selectedReservation: ReservationOption,
    onExtrasUpdate: (Int, Int, Boolean) -> Unit,
    navController: NavController,
    viewModel: SaveBookingViewModel = hiltViewModel(),
    bookingViewModel: GetBookingViewModel,
    price: String,
    selectedTimeSlot: String,
    mappedBookingsJson: String,
    onTotalAmountCalculated: (Double, String) -> Unit,
    viewModel9: SharedViewModel,
    findTermsViewModel: FindTermsViewModel = hiltViewModel(),
    updatePhoneViewModel: UpdatePhoneViewModel = hiltViewModel(),
    viewModel3: GetProfileViewModel = hiltViewModel()

) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val profileData by viewModel3.profileData.observeAsState(DataResult.Loading)

    // var selectedParts by remember { mutableStateOf("1") }
    var amountSelected by remember { mutableStateOf(Pair(0.0, "DT")) }
    var adjustedAmount by remember { mutableStateOf(0.0) }
    var adjustedSharedExtrasAmount by remember { mutableStateOf(0.0) }
    var totalSharedExtrasCost by remember { mutableStateOf(0.0) }
    val selectedParts by viewModel9.selectedParts.collectAsState()
    var totalExtrasCost by remember { mutableStateOf(0.0) }

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
    var extrasEnabled by remember { mutableStateOf(false) }
    var selectedRaquette by remember { mutableStateOf(1) }
    var includeBalls by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val extrasCost = (if (includeBalls) 5 else 0) + (selectedRaquette * 2)
    onExtrasUpdate(extrasCost, selectedRaquette, includeBalls)


    //onExtrasUpdate(totalExtrasCost.toInt(), selectedRaquette, includeBalls)
    //onExtrasUpdate(totalExtrasCost.toInt(), selectedRaquette, includeBalls)


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
    LaunchedEffect(Unit) {
        viewModel3.fetchProfileData()
    }
    when (val result = profileData) {
        is DataResult.Success -> {
            // Extract profile data into variables
            val profile = result.data as? GetProfileResponse
            if (profile != null && !firstName.isNotEmpty()) {
                firstName = profile.firstName
                lastName = profile.lastName
                phoneNumber = profile.phone
            } else {
                androidx.compose.material.Text(text = "")
            }
        }
        is DataResult.Loading -> {
            androidx.compose.material.Text(text = "Loading profile data...")
        }
        is DataResult.Failure -> {
            androidx.compose.material.Text(text = "Error: ${result.errorMessage}")
        }
    }





    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Card for the header text
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Requis pour votre réservation",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Add some space between the cards

        // Card for the phone number input
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = {
                            // Allow digits, the plus sign (+), and restrict the length to 8 characters
                            if ((it.all { char -> char.isDigit() || char == '+' }) && it.length <= 8) {
                                // Ensure that the plus sign only appears at the beginning
                                if (it.count { char -> char == '+' } <= 1 && (it.indexOf('+') == 0 || !it.contains('+'))) {
                                    phoneNumber = it
                                }
                            }
                        },
                        modifier = Modifier
                            .offset(x = 3.dp)
                            .width(200.dp)
                            .height(55.dp)
                            .padding(vertical = 4.dp),

                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Phone Icon",
                                modifier = Modifier.padding(8.dp)
                            )
                        },
                        placeholder = { Text("Phone Number") }, // Optional placeholder
                        shape = RoundedCornerShape(13.dp),
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Color.Gray,
                            unfocusedBorderColor = Color.Gray,
                            backgroundColor = Color.Transparent
                        )
                    )

                    Spacer(modifier = Modifier.width(10.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = 15.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {

                            Button(
                                onClick = {
                                    val phoneBody = RequestBody.create(
                                        "text/plain".toMediaTypeOrNull(), phoneNumber
                                    )
                                    updatePhoneViewModel.UpdatePhone(phoneBody)
                                },
                                shape = RoundedCornerShape(13.dp),
                                border =  BorderStroke(1.dp, Color(0xFF0054D8)),
                                modifier = Modifier
                                    .fillMaxWidth(0.93f)
                                    .padding(1.dp),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),

                            ) {
                                Text(
                                    text = "Modifier",
                                    color = Color(0xFF0054D8),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            }

                    }
                }
            }


        }

        /*
          modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f), // Ensure both buttons take equal space
                         colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        shape = RoundedCornerShape(13.dp)
         */
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
            viewModel2 = viewModel2,
            amountSelected = Pair(
                selectedReservation.price.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0,
                selectedReservation.price.takeWhile { !it.isDigit() && it != '.' }
            ),
            onAdjustedAmountUpdated = { newAdjustedAmount, newAdjustedSharedExtrasAmount ->
                adjustedAmount = newAdjustedAmount
                adjustedSharedExtrasAmount = newAdjustedSharedExtrasAmount
                Log.d("MECH RAGEL", "Calculated: $adjustedAmount, Shared Extras: $adjustedSharedExtrasAmount")
            },
            onPartsSelected = { newSelectedParts ->
                // Handle parts selection update here if needed
                Log.d("PaymentSection1", "Newly Selected Parts: $newSelectedParts")
            },
            viewModel1 = viewModel9
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            // ExtrasSection Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                var selectedExtras by remember { mutableStateOf<List<Triple<String, String, Int>>>(emptyList()) }

                ExtrasSection(
                    onExtrasUpdate = { extras, cost ->
                        selectedExtras = extras
                        totalExtrasCost = extras.sumOf { it.third * it.second.toDouble() }
                        Log.d("ExtrasSection", "Extras: $extras, Total Cost: $totalExtrasCost")
                    },
                    selectedParts = selectedParts.toString(),
                )
            }

            // Spacer between ExtrasSection and ReservationSummary
            Spacer(modifier = Modifier.height(16.dp))

            // ReservationSummary Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {

                ReservationSummary(
                    selectedDate = selectedDate,
                    selectedTimeSlot = selectedTimeSlot.toString(), // Pass selected time
                    selectedReservation = selectedReservation, // Pass selected reservation
                    selectedExtras = selectedExtras,
                    amountSelected = Pair(
                        selectedReservation.price.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0,
                        selectedReservation.price.takeWhile { !it.isDigit() && it != '.' }
                    ),
                    onTotalAmountCalculated = { totalAmount ->
                        // Handle the total amount calculated if needed
                    },
                    price = price,
                    time = time.toString(),
                    navController = navController,
                    adjustedAmount = adjustedAmount, // Pass updated adjustedAmount
                    adjustedSharedExtrasAmount = adjustedSharedExtrasAmount,
                    totalSharedExtrasCost = totalSharedExtrasCost,
                    totalExtrasCost = totalExtrasCost // Pass the calculated totalExtrasCost
                )
                Log.d("ReservationSummary", "Updated Adjusted Amount: $adjustedSharedExtrasAmount")
            }

        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp), // optional padding for alignment
                horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally) // Space between the buttons
            ) {

                val coroutineScope = rememberCoroutineScope()
                val selectedPlayers by findTermsViewModel.selectedPlayers.observeAsState(initial = mutableListOf())
                val sharedExtras by findTermsViewModel.sharedExtras.observeAsState(initial = mutableListOf())
                val privateExtras by findTermsViewModel.privateExtras.observeAsState(initial = mutableListOf())
                Log.d("PaymentSection1", "Shared Extras: $sharedExtras")
                Log.d("PaymentSection1", "Private Extras: $privateExtras")

                Log.d("IMAAAAAAAAD", "Private Extras: $totalExtrasCost")

                Button(
                    onClick = {

                        isLoading = true
                        Log.d(
                            "OLFA",
                            "Selected Players before extracting IDs: ${selectedPlayers.joinToString { "ID=${it}" }}"
                        )

                        // Update selected parts first
                        viewModel9.updateSelectedParts(selectedParts)

                        // Launch a coroutine to collect the latest selected parts
                        coroutineScope.launch {
                            viewModel9.selectedParts.collectLatest { currentSelectedParts ->
                                val selectedBooking = mappedBookings.firstOrNull()

                                if (selectedBooking != null) {

                                    val totalAmountSelected = adjustedAmount + totalExtrasCost
                                    Log.d("OLFA", "Total Amount Selected: $totalAmountSelected")

                                    if (totalAmountSelected <= 0) {
                                        Toast.makeText(
                                            context,
                                            "Total amount not calculated or is zero",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        isLoading = false
                                        return@collectLatest
                                    }


                                    val playerIds = selectedPlayers.toList()
                                    Log.d("DEBUG", "Converted Players List: $playerIds")

                                    Log.d(
                                        "mouchABD",
                                        "Extracted userIds: ${selectedPlayers.joinToString { "ID=${it}" }}"
                                    )


                                    val updatedMappedBookings =
                                        mappedBookings.mapIndexed { index, booking ->
                                            if (index == 0) {
                                                booking.copy(
                                                    numberOfPart = currentSelectedParts,
                                                    userIds = playerIds
                                                )
                                            } else {
                                                booking
                                            }
                                        }
                                    Log.d("mouchABD", "Extracted userIds: $selectedPlayers")

                                    val totalAmountBigDecimal =
                                        BigDecimal.valueOf(totalAmountSelected)
                                    val currency =
                                        selectedReservation.price.takeWhile { !it.isDigit() && it != '.' }
                                    onTotalAmountCalculated(totalAmountSelected, currency)


                                    paymentPayAvoirViewModel.PaymentPayAvoir(totalAmountBigDecimal)
                                    saveBookingViewModel.SaveBooking(updatedMappedBookings)

                                    saveBookingViewModel.dataResult.observe(lifecycleOwner) { result ->
                                        when (result) {
                                            is DataResult.Loading -> {
                                                Log.d("SaveBooking", "Saving booking...")
                                            }

                                            is DataResult.Success -> {
                                                isLoading = false
                                                Log.d("SaveBooking", "Booking saved successfully!")

                                                val bookingList =
                                                    result.data as? List<SaveBookingResponse>
                                                if (!bookingList.isNullOrEmpty()) {
                                                    val firstBooking = bookingList[0]
                                                    val bookingId = firstBooking.id.toString()
                                                    Log.d(
                                                        "FRIH",
                                                        "numberOfPart after booking save: ${firstBooking.numberOfPart}"
                                                    )

                                                    // Create a PaymentRequest using the updated amount
                                                    val paymentRequest = PaymentRequest(
                                                        amount = totalAmountSelected.toString(),
                                                        currency = selectedBooking.currencySymbol
                                                            ?: "EUR",
                                                        orderId = bookingId
                                                    )

                                                    // Process the payment
                                                    paymentViewModel.Payment(paymentRequest)
                                                    paymentViewModel.dataResult.observe(
                                                        lifecycleOwner
                                                    ) { paymentResult ->
                                                        when (paymentResult) {
                                                            is DataResult.Loading -> {
                                                                Log.d(
                                                                    "Payment",
                                                                    "Processing payment..."
                                                                )
                                                            }

                                                            is DataResult.Success -> {
                                                                Log.d(
                                                                    "Payment",
                                                                    "Payment processed successfully!"
                                                                )
                                                                val paymentResponse =
                                                                    paymentResult.data as? PaymentResponse
                                                                val formUrl =
                                                                    paymentResponse?.formUrl

                                                                if (!formUrl.isNullOrEmpty()) {
                                                                    val encodedUrl =
                                                                        Uri.encode(formUrl)
                                                                    val userIdsString =
                                                                        selectedPlayers.joinToString(
                                                                            ","
                                                                        ) // Convert the list to a comma-separated string
                                                                    val encodedUserIds =
                                                                        Uri.encode(userIdsString)
                                                                    val SharedListString =
                                                                        sharedExtras.joinToString(",")
                                                                    val encodedSharedList =
                                                                        Uri.encode(SharedListString) //  privateExtras
                                                                    val PrivateListString =
                                                                        privateExtras.joinToString(",")
                                                                    val encodedPrivateList =
                                                                        Uri.encode(PrivateListString)

                                                                    val navigationRoute =
                                                                        "WebViewScreen?paymentUrl=$encodedUrl&numberOfPart=$currentSelectedParts&userIds=$encodedUserIds&sharedList=$encodedSharedList&privateList=$encodedPrivateList"
                                                                    Log.d(
                                                                        "NavigationDebug",
                                                                        "Navigating to: $navigationRoute"
                                                                    )

                                                                    // Navigate to WebViewScreen with the form URL
                                                                    navController.navigate(
                                                                        navigationRoute
                                                                    )
                                                                } else {
                                                                    Log.e(
                                                                        "Payment",
                                                                        "No form URL found in the response."
                                                                    )
                                                                    Toast.makeText(
                                                                        context,
                                                                        "Payment failed: No form URL received.",
                                                                        Toast.LENGTH_LONG
                                                                    ).show()
                                                                }
                                                            }

                                                            is DataResult.Failure -> {
                                                                Toast.makeText(
                                                                    context,
                                                                    "Payment failed: ${paymentResult.errorMessage}",
                                                                    Toast.LENGTH_LONG
                                                                ).show()
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    Toast.makeText(
                                                        context,
                                                        "Failed to retrieve booking ID.",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            }

                                            is DataResult.Failure -> {
                                                isLoading = false
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
                                    Toast.makeText(
                                        context,
                                        "No valid booking data available.",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                    },

                    enabled = !isLoading,

                    modifier = Modifier
                        .height(48.dp).offset(x = -16.dp)
                        .weight(1f), // Ensure both buttons take equal space
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0054D8)),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Payment,
                            contentDescription = "Card Payment",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Carte Crédit",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
                var amount by remember { mutableStateOf(BigDecimal.ZERO) }

                Button(
                    onClick =  {
                        val selectedBooking = mappedBookings.firstOrNull()
                        viewModel9.updateSelectedParts(selectedParts)

                        coroutineScope.launch {
                            viewModel9.selectedParts.collectLatest { currentSelectedParts ->
                                val selectedBooking = mappedBookings.firstOrNull()

                                if (selectedBooking != null) {
                                    val totalAmountSelected = adjustedAmount + totalExtrasCost
                                    Log.d("OLFA", "Total Amount Selected: $totalAmountSelected")

                                    if (totalAmountSelected <= 0) {
                                        Toast.makeText(
                                            context,
                                            "Total amount not calculated or is zero",
                                            Toast.LENGTH_LONG
                                        ).show()
                                        isLoading = false
                                        return@collectLatest
                                    }

                                    val playerIds = selectedPlayers.toList()
                                    val totalAmountBigDecimal = BigDecimal.valueOf(totalAmountSelected).setScale(0, RoundingMode.DOWN) // Remove decimals
                                    val currency = selectedReservation.price.takeWhile { !it.isDigit() && it != '.' }
                                    onTotalAmountCalculated(totalAmountBigDecimal.toInt().toDouble(), currency) // Convert back to double if needed

                                    // Trigger PaymentPayAvoir with rounded amount
                                    paymentPayAvoirViewModel.PaymentPayAvoir(totalAmountBigDecimal)

                                    paymentPayAvoirViewModel.dataResult.observe(lifecycleOwner) { paymentResult ->
                                        when (paymentResult) {
                                            is DataResult.Loading -> {
                                                isLoading = true
                                                Log.d("PAYMENT", "Processing PaymentPayAvoir...")
                                            }

                                            is DataResult.Success -> {
                                                isLoading = false
                                                val payFromAvoirResponse = true // Payment success implies true
                                                Log.d("PAYMENT", "PaymentPayAvoir successful: $payFromAvoirResponse")

                                                val updatedMappedBookings = mappedBookings.mapIndexed { index, booking ->
                                                    if (index == 0) {
                                                        booking.copy(
                                                            numberOfPart = currentSelectedParts,
                                                            userIds = playerIds,
                                                            payFromAvoir = payFromAvoirResponse // Updated dynamically
                                                        )
                                                    } else {
                                                        booking
                                                    }
                                                }

                                                balanceViewModel.fetchAndBalance()
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

                                                                val paymentRequest = PaymentRequest(
                                                                    amount = totalAmountSelected.toString(),
                                                                    currency = selectedBooking.currencySymbol ?: "EUR",
                                                                    orderId = bookingId
                                                                )

                                                                paymentViewModel.Payment(paymentRequest)

                                                                val confirmBookingRequest = ConfirmBookingRequest(
                                                                    amount = totalAmountBigDecimal,
                                                                    numberOfPart = currentSelectedParts,
                                                                    payFromAvoir = payFromAvoirResponse,
                                                                    privateExtrasIds = mappedBookings.first().privateExtrasIds ?: emptyList(),
                                                                    bookingIds = bookingList.mapNotNull { it.id },
                                                                    buyerId = mappedBookings.first().buyerId ?: "",
                                                                    couponIds = mappedBookings.first().couponIds ?: emptyMap(),
                                                                    sharedExtrasIds = mappedBookings.first().sharedExtrasIds ?: emptyList(),
                                                                    status = true,
                                                                    token = "",
                                                                    transactionId = "",
                                                                    userIds = mappedBookings.first().userIds
                                                                )

                                                                confirmBookingViewModel.GetPayment(confirmBookingRequest)
                                                            } else {
                                                                Toast.makeText(
                                                                    context,
                                                                    "No booking data available.",
                                                                    Toast.LENGTH_LONG
                                                                ).show()
                                                            }
                                                        }

                                                        is DataResult.Failure -> {
                                                            isLoading = false
                                                            Toast.makeText(
                                                                context,
                                                                "Failed to save booking.",
                                                                Toast.LENGTH_LONG
                                                            ).show()
                                                        }
                                                    }
                                                }
                                            }

                                            is DataResult.Failure -> {
                                                isLoading = false
                                                Log.e("PAYMENT", "PaymentPayAvoir failed: ${paymentResult.errorMessage}")
                                                Toast.makeText(
                                                    context,
                                                    "Payment processing failed.",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .weight(1f), // Ensure both buttons take equal space
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0054D8)),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Money,
                            contentDescription = "Credits Payment",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Crédit Padelium",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
            }

        }
    }
}


class SharedViewModel : ViewModel() {
    private val _selectedParts = MutableStateFlow(4)
    val selectedParts: StateFlow<Int> get() = _selectedParts

    fun updateSelectedParts(parts: Int) {
        _selectedParts.value = parts
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun WebViewScreen(
    navController: NavController,
    formUrl: String,
    getPaymentViewModel: GetPaymentViewModel,
    getManagerViewModel: GetManagerViewModel,
    getEmailViewModel: GetEmailViewModel,
    numberOfPart: Int,
    findTermsViewModel: FindTermsViewModel = hiltViewModel(),
    userIds: String,
    sharedList: String,
    privateList: String,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val dataResult by getPaymentViewModel.dataResult.observeAsState()
    val selectedPlayers by findTermsViewModel.selectedPlayers.observeAsState(initial = mutableListOf())
    val userIdsList = userIds.split(",").mapNotNull { it.toLongOrNull() }
    val sharedListIds = sharedList.split(",").mapNotNull { it.toLongOrNull() }
    val privateListIds = privateList.split(",").mapNotNull { it.toLongOrNull() }

    Log.d("WebViewScreen", "Selected Parts: $numberOfPart")
    Log.d("WebViewScreen", "Extracted userIds: $userIdsList")

    AndroidView(factory = {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    url?.let {
                        if (it.contains("paymentSuccess")) {
                            Log.e("paymentSuccess", "Payment successful URL: $it")
                            Toast.makeText(context, "Payment Successful", Toast.LENGTH_LONG).show()
                            navController.navigate("PaymentSuccessScreen")
                            return true
                        }
                    }
                    return super.shouldOverrideUrlLoading(view, url)
                }

                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
                    val url = request.url.toString()
                    Log.d("WebViewScreen", "Loading URL: $url")

                    val originalUri = Uri.parse(formUrl)
                    val numberOfPartValue = originalUri.getQueryParameter("numberOfPart") ?: numberOfPart.toString()

                    val newUri = Uri.parse(url).buildUpon()
                        .appendQueryParameter("numberOfPart", numberOfPartValue)
                        .build()
                    Log.d("WebViewScreen", "New URL: ${newUri}")

                    val orderId = extractOrderId(url)
                    val bookingIds = extractBookingIds(url)
                    val numberOfPartValueNew = extractNumberOfPart(newUri.toString())

                    if (orderId.isNotEmpty()) {
                        val request = GetPaymentRequest(
                            bookingIds = bookingIds,
                            couponIds = extractCouponIds(newUri.toString()),
                            numberOfPart = numberOfPartValueNew,
                            orderId = orderId,
                            privateExtrasIds = privateListIds,
                            sharedExtrasIds = sharedListIds,
                            userIds = userIdsList
                        )

                        Log.d("WebViewScreen", "Sending GetPayment request: $request")
                        coroutineScope.launch {
                            try {
                                getPaymentViewModel.GetPayment2(request)
                                getManagerViewModel.GetManager(bookingIds)
                                getEmailViewModel.GetEmail(bookingIds)
                                navController.navigate("PaymentSuccessScreen")
                            } catch (e: Exception) {
                                Log.e("WebViewScreen", "Error processing payment: $e")
                                Toast.makeText(context, "An error occurred: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        }
                    } else {
                        Log.e("WebViewScreen", "Order ID not found in URL")
                    }

                    view?.loadUrl(newUri.toString())
                    return true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Log.d("WebView", "Page finished loading: $url")
                }
            }

            val uri = Uri.parse(formUrl)
            val updatedUrl = if (uri.getQueryParameter("numberOfPart").isNullOrEmpty()) {
                "$formUrl&numberOfPart=$numberOfPart"
            } else {
                val newUri = uri.buildUpon()
                    .appendQueryParameter("numberOfPart", numberOfPart.toString())
                    .build()
                newUri.toString()
            }
            Log.d("WebViewScreen", "Final URL passed to WebView: $updatedUrl")
            loadUrl(updatedUrl)
        }
    })

    dataResult?.let { result ->
        when (result) {
            is DataResult.Loading -> {
                Log.d("WebViewScreen", "Fetching payment details...")
            }
            is DataResult.Success -> {
                Toast.makeText(context, "Payment details fetched successfully!", Toast.LENGTH_LONG).show()
                navController.navigate("PaymentSuccessScreen")
            }
            is DataResult.Failure -> {
                Toast.makeText(
                    context,
                    "Failed to fetch payment details: ${result.errorMessage} (Code: ${result.errorCode})",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("WebViewScreen", "Error fetching payment details", result.exception)
            }
        }
    }
}










// Utility functions with improvements
fun extractOrderId(url: String): String {
    val uri = Uri.parse(url)
    return uri.getQueryParameter("orderId") ?: ""
}



fun extractNumberOfPart(url: String): Int {
    val uri = Uri.parse(url)
    val numberOfPartRaw = uri.getQueryParameter("numberOfPart")
    Log.d("extractNumberOfPart", "Raw numberOfPart: $numberOfPartRaw")
    return numberOfPartRaw?.toIntOrNull() ?: 1
}


fun extractBookingIds(url: String): List<Long> {
    val uri = Uri.parse(url)
    val ids = uri.getQueryParameter("bookingIds")
    if (ids != null) {
        Log.d("extractBookingIds", "Raw bookingIds parameter: $ids")
        return ids.split(",").mapNotNull { it.trim().toLongOrNull() }
    }

    // Fallback: Extract ID from path
    val pathSegments = uri.pathSegments
    Log.d("extractBookingIds", "Path segments: $pathSegments")
    val fallbackId = pathSegments.getOrNull(3)?.toLongOrNull()
    return if (fallbackId != null) listOf(fallbackId) else emptyList()
}



fun extractCouponIds(url: String): Map<Long, Long> {
    val uri = Uri.parse(url)
    val ids = uri.getQueryParameter("couponIds")
    return ids?.split(",")?.mapNotNull {
        val pair = it.split(":")
        if (pair.size == 2) {
            val key = pair[0].toLongOrNull()
            val value = pair[1].toLongOrNull()
            if (key != null && value != null) key to value else null
        } else null
    }?.toMap() ?: emptyMap()
}

fun extractPrivateExtrasIds(url: String): List<Long?> {
    val uri = Uri.parse(url)
    val ids = uri.getQueryParameter("privateExtrasIds")
    return ids?.split(",")?.map { it.toLongOrNull() } ?: emptyList()
}

fun extractSharedExtrasIds(url: String): List<Long?> {
    val uri = Uri.parse(url)
    val ids = uri.getQueryParameter("sharedExtrasIds")
    return ids?.split(",")?.map { it.toLongOrNull() } ?: emptyList()
}

fun extractUserIds(url: String): List<Long> {
    val uri = Uri.parse(url)
    val ids = uri.getQueryParameter("userIds")
    if (ids != null) {
        Log.d("extractUser Ids", "Raw userIds parameter: $ids")
        return ids.split(",").mapNotNull { it.trim().toLongOrNull() }
    }

    // Fallback: Extract ID from path
    val pathSegments = uri.pathSegments
    Log.d("extractUser Ids", "Path segments: $pathSegments")
    val fallbackId = pathSegments.getOrNull(3)?.toLongOrNull()
    return if (fallbackId != null) listOf(fallbackId) else emptyList()
}






















