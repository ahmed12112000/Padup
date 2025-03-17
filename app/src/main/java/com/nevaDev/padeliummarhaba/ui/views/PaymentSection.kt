package com.nevaDev.padeliummarhaba.ui.views

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payment
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nevaDev.padeliummarhaba.viewmodels.BalanceViewModel
import com.nevaDev.padeliummarhaba.viewmodels.ConfirmBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.ErrorCreditViewModel
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
import com.padelium.data.dto.SaveBookingRequestt
import com.padelium.domain.dto.CreditErrorRequest
import com.padelium.domain.dto.EstablishmentDTO
import com.padelium.domain.dto.EstablishmentDTOoo
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.GetPaymentRequest
import com.padelium.domain.dto.GetProfileResponse
import com.padelium.domain.dto.PaymentResponse
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.padelium.domain.dto.SaveBookingResponse // Adjust import as necessary
import com.padelium.domain.dto.test
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import java.math.RoundingMode

fun List<GetBookingResponseDTO>.toDomain(): List<GetBookingResponse> {
    val formatterOutput = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
    val formatterWithMillis = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")



    return this.map { dto ->
        val searchDate = dto.searchDate?.let {
            LocalDate.parse(it, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        } ?: LocalDate.now() // Use current date if searchDate is null
        Log.e("DateConversion", "Processing searchDate: $searchDate")
        val firstPlanning = dto.plannings?.firstOrNull()

        val startFormatted = firstPlanning?.fromStr?.let { timeStr ->
            try {
                val formattedTimeStr = timeStr.padStart(5, '0') // Ensure "HH:mm" format
                val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                LocalDateTime.of(searchDate, time).format(formatterOutput) // Use searchDate
            } catch (e: Exception) {
                Log.e("DateConversion", "Error parsing startFormatted", e)
                "2024-12-20 09:30" // Fallback value
            }
        } ?: "2024-12-20 09:30"
        val endFormatted = firstPlanning?.toStr?.let { timeStr ->
            try {
                val formattedTimeStr = timeStr.padStart(5, '0') // Ensure "HH:mm" format
                val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                LocalDateTime.of(searchDate, time).format(formatterOutput) // Use searchDate
            } catch (e: Exception) {
                Log.e("DateConversion", "Error parsing endFormatted", e)
                "2024-12-20 09:30" // Fallback value
            }
        } ?: "2024-12-20 09:30"

        val fromFormatted = firstPlanning?.fromStr?.let { timeStr ->
            try {
                val formattedTimeStr = timeStr.padStart(5, '0') // Ensure correct length
                val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                LocalDateTime.of(searchDate, time).format(formatterWithMillis) // Use searchDate
            } catch (e: Exception) {
                Log.e("DateConversion", "Error parsing fromFormatted", e)
                "2024-12-20T09:30:00.000Z" // Fallback value
            }
        } ?: "2024-12-20T09:30:00.000Z"

        val toFormatted = firstPlanning?.toStr?.let { timeStr ->
            try {
                val formattedTimeStr = timeStr.padStart(5, '0') // Ensure correct length
                val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                LocalDateTime.of(searchDate, time).format(formatterWithMillis) // Use searchDate
            } catch (e: Exception) {
                Log.e("DateConversion", "Error parsing toFormatted", e)
                "2024-12-20T09:30:00.000Z" // Fallback value
            }
        } ?: "2024-12-20T09:30:00.000Z"

        val formattedAmount = dto.amount?.stripTrailingZeros()?.toPlainString()?.let {
            if (it.contains(".")) {
                it.split(".")[0] // Remove the decimal part
            } else {
                it // No decimal part, return as is
            }
        } ?: "0"

        val formattedAamount = dto.aamount?.stripTrailingZeros()?.toPlainString()?.let {
            if (it.contains(".")) {
                it.split(".")[0] // Remove the decimal part
            } else {
                it // No decimal part, return as is
            }
        } ?: "0"

        GetBookingResponse(
            aamount = BigDecimal(formattedAamount),
            amount = BigDecimal(formattedAmount),

            amountfeeTrans = dto.amountfeeTrans ?: BigDecimal.ZERO,
            bookingAnnulationDTOSet = dto.bookingAnnulationDTOSet ?: emptySet(),

            end = endFormatted,
            establishmentDTO = dto.establishmentDTO,
            sharedExtrasIds = dto.sharedExtrasIds ?: emptyList(),
            privateExtrasIds = dto.privateExtrasIds ?: emptyList(),
            plannings = dto.plannings ?: emptyList(),


            from =fromFormatted,

            numberOfPart =  dto.numberOfPart ?: 0,
            currencyId = dto.currencyId ?: 0L,

            payFromAvoir = dto.payFromAvoir ?: false,

            searchDate = dto.searchDate ?: "",

            to =toFormatted,
            start = startFormatted,
            //  users = dto.users ?: emptyList(),
            userIds = dto.userIds ,
            // orderId = dto.orderId ?: 0L,
            //   id = dto.id ?: 0L,
            //   buyerId = dto.buyerId ?: "",
            //  couponIds = dto.couponIds ?: emptyMap(),
            establishmentPacksDTO = dto.establishmentPacksDTO ?: emptyList(),

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
    getProfileViewModel: GetProfileViewModel = hiltViewModel()
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    Log.e("BBBBBBBBBBBBBDateeeeeeee", "Failed to save booking: ${selectedDate}")
    var isPhoneNumberEmpty by remember { mutableStateOf(phoneNumber.isEmpty()) }
    var showPopup by remember { mutableStateOf(false) }
    val isPhoneNumberValid = phoneNumber.isNotEmpty() && phoneNumber.length == 8 && phoneNumber.all { it.isDigit() }

    // var selectedParts by remember { mutableStateOf("1") }
    var amountSelected by remember { mutableStateOf(Pair(0.0, "DT")) }
    var adjustedAmount by remember { mutableStateOf(0.0) }
    var adjustedSharedExtrasAmount by remember { mutableStateOf(0.0) }
    var totalSharedExtrasCost by remember { mutableStateOf(0.0) }
    val selectedParts by viewModel9.selectedParts.collectAsState()
    var totalExtrasCost by remember { mutableStateOf(0.0) }

    // Deserialize JSON into List<GetBookingResponse>
    val type = object : TypeToken<List<GetBookingResponse >>() {}.type
    val mappedBookings: List<GetBookingResponse > = Gson().fromJson(mappedBookingsJson, type)
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
    val updatePhoneResult by updatePhoneViewModel.dataResult.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current
    var showMessage by remember { mutableStateOf(false) }

    LaunchedEffect(updatePhoneResult) {
        if (updatePhoneResult is DataResult.Success) {
            showMessage = true
            //   Toast.makeText(context, "Phone number updated successfully ✅", Toast.LENGTH_LONG).show()
            delay(3000)
            showMessage = false
        }
    }

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
    LaunchedEffect(Unit) {
        getProfileViewModel.fetchProfileData()
    }


    val profileData by getProfileViewModel.profileData.observeAsState(DataResult.Loading)
    when (val result = profileData) {
        is DataResult.Success -> {
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
            //  Text(text = "Loading profile data...")
        }
        is DataResult.Failure -> {
            //  Text(text = "Error: ${result.errorMessage}")
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
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp)) // Add some space between the cards

        if (showMessage) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Success Icon",
                    tint = Color(0xFF28A745),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Numéro changé avec succès",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        // Card for the phone number input
        // Card for the phone number display


        Spacer(modifier = Modifier.height(16.dp))



        // Card for the phone number display
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Phone,
                        contentDescription = "Phone Icon",
                        modifier = Modifier.padding(8.dp)
                    )
                    Box(
                        modifier = Modifier
                            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                            .padding(8.dp)
                            .width(160.dp)
                    ) {
                        Text(
                            text = if (phoneNumber.isEmpty()) "* Obligatoire" else phoneNumber,
                            color = if (phoneNumber.isEmpty()) Color.Red else Color.Black,
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
                Button(
                    onClick = {
                        showPopup = true
                    },
                    shape = RoundedCornerShape(13.dp),
                    border = BorderStroke(1.dp, Color(0xFF0054D8)),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                    modifier = Modifier.size(width = 120.dp, height = 45.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),  // Ensure it takes up the full space of the button
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (phoneNumber.isEmpty()) "Ajouter" else "Modifier",
                            color = Color(0xFF0054D8),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            textAlign = TextAlign.Center
                        )
                    }



                    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                    val popupHeight = screenHeight / 4


                    if (showPopup) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.BottomCenter // Align the content to the bottom center
                        ) {
                            Dialog(onDismissRequest = { showPopup = false }) {
                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(20.dp), // Padding around the dialog
                                    shape = RoundedCornerShape(12.dp),
                                    color = Color.White,
                                    elevation = 8.dp
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        // Phone number input field in the popup
                                        OutlinedTextField(
                                            value = phoneNumber,
                                            onValueChange = { newValue ->
                                                // Only update if the new value is numeric and length is within the limit
                                                if (newValue.all { it.isDigit() } && newValue.length <= 8) {
                                                    phoneNumber = newValue
                                                }
                                            },
                                            label = { Text("Numéro de téléphone") },
                                            modifier = Modifier.fillMaxWidth(),
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                            isError = phoneNumber.isEmpty() || phoneNumber.length != 8 || !phoneNumber.all { it.isDigit() }, // Display error if not valid
                                            shape = RoundedCornerShape(10.dp),
                                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                                focusedBorderColor = Color.Gray,
                                                unfocusedBorderColor = Color.Gray,
                                                focusedLabelColor = Color.Gray,
                                            )
                                        )

                                        // Validation message for invalid phone number
                                        if (phoneNumber.isEmpty() || phoneNumber.length != 8 || !phoneNumber.all { it.isDigit() }) {
                                            Text(
                                                text = "* Numéro de téléphone non valide",
                                                color = Color.Red,
                                                style = MaterialTheme.typography.body2,
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))

                                        // Modifier button inside the popup
                                        Button(
                                            onClick = {
                                                // Validate phone number before proceeding
                                                if (phoneNumber.length == 8 && phoneNumber.all { it.isDigit() }) {
                                                    val phoneBody = RequestBody.create(
                                                        "text/plain".toMediaTypeOrNull(), phoneNumber
                                                    )
                                                    updatePhoneViewModel.UpdatePhone(phoneBody)
                                                    showPopup = false
                                                } else {
                                                    // If the phone number is not valid, show an error message
                                                    // You can trigger the error message in the TextField or another place
                                                }
                                            },
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(13.dp),
                                            colors = ButtonDefaults.buttonColors(
                                                backgroundColor = Color(0xFF0054D8)
                                            ),
                                        ) {
                                            Text(
                                                text = "Sauvegarder",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold,
                                            )
                                        }
                                    }
                                }
                            }
                        }

                    }
                }}}
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
                horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally) // Space between the buttons
            ) {

                val coroutineScope = rememberCoroutineScope()
                val selectedPlayers by findTermsViewModel.selectedPlayers.observeAsState(initial = mutableListOf())
                val sharedExtras by findTermsViewModel.sharedExtras.observeAsState(initial = mutableListOf())
                val privateExtras by findTermsViewModel.privateExtras.observeAsState(initial = mutableListOf())
                val formatterOutput: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                val formatterWithMillis: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")



                Button(
                    onClick = {

                        if (isLoading) return@Button
                        Log.d(
                            "OLFA",
                            "Selected Players before extracting IDs: ${selectedPlayers.joinToString { "ID=${it}" }}"
                        )

                        viewModel9.updateSelectedParts(selectedParts)

                        coroutineScope.launch {
                            viewModel9.selectedParts.collectLatest { currentSelectedParts ->
                                val selectedBooking = mappedBookings.firstOrNull()

                                if (selectedBooking != null) {
                                    val searchDate = selectedDate
                                    Log.e("DateConversion", "Processing searchDate: $searchDate")

                                    val firstPlanning = selectedBooking.plannings?.firstOrNull()

                                    val startFormatted = firstPlanning?.fromStr?.let { timeStr ->
                                        try {
                                            val formattedTimeStr = timeStr.padStart(5, '0')
                                            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                                            LocalDateTime.of(searchDate, time).format(formatterOutput)
                                        } catch (e: Exception) {
                                            Log.e("DateConversion", "Error parsing startFormatted", e)
                                            searchDate.atTime(9, 30).format(formatterOutput)
                                        }
                                    } ?: searchDate.atTime(9, 30).format(formatterOutput)

                                    val endFormatted = firstPlanning?.toStr?.let { timeStr ->
                                        try {
                                            val formattedTimeStr = timeStr.padStart(5, '0')
                                            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                                            LocalDateTime.of(searchDate, time).format(formatterOutput)
                                        } catch (e: Exception) {
                                            Log.e("DateConversion", "Error parsing endFormatted", e)
                                            searchDate.atTime(9, 30).format(formatterOutput)
                                        }
                                    } ?: searchDate.atTime(9, 30).format(formatterOutput)

                                    val fromFormatted = firstPlanning?.fromStr?.let { timeStr ->
                                        try {
                                            val formattedTimeStr = timeStr.padStart(5, '0')
                                            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                                            LocalDateTime.of(searchDate, time).format(formatterWithMillis)
                                        } catch (e: Exception) {
                                            Log.e("DateConversion", "Error parsing fromFormatted", e)
                                            searchDate.atTime(9, 30).format(formatterWithMillis)
                                        }
                                    } ?: searchDate.atTime(9, 30).format(formatterWithMillis)

                                    val toFormatted = firstPlanning?.toStr?.let { timeStr ->
                                        try {
                                            val formattedTimeStr = timeStr.padStart(5, '0')
                                            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                                            LocalDateTime.of(searchDate, time).format(formatterWithMillis)
                                        } catch (e: Exception) {
                                            Log.e("DateConversion", "Error parsing toFormatted", e)
                                            searchDate.atTime(9, 30).format(formatterWithMillis)
                                        }
                                    } ?: searchDate.atTime(9, 30).format(formatterWithMillis)


                                    val totalAmountSelected = adjustedAmount + totalExtrasCost

                                    if (totalAmountSelected <= 0) {
                                        isLoading = false
                                        return@collectLatest
                                    }
                                    val formattedAmount = selectedBooking.amount?.stripTrailingZeros()?.toPlainString()?.let {
                                        if (it.contains(".")) it else it.toInt().toString()
                                    } ?: "0"

                                    val formattedAamount = selectedBooking.aamount?.stripTrailingZeros()?.toPlainString()?.let {
                                        if (it.contains(".")) it else it.toInt().toString()
                                    } ?: "0"

                                    val playerIds = selectedPlayers.toList()
                                    Log.d("DEBUG", "Converted Players List: $playerIds")



                                    val updatedMappedBookings = mappedBookings.mapIndexed { index, booking ->

                                        if (index == 0) {
                                            booking.copy(
                                                numberOfPart = currentSelectedParts,
                                                userIds = playerIds,
                                                searchDate = searchDate.toString(),
                                                start = startFormatted,
                                                end = endFormatted,
                                                from = fromFormatted,
                                                to = toFormatted,
                                                sharedExtrasIds = sharedExtras,
                                                privateExtrasIds = privateExtras,
                                                amount = BigDecimal(formattedAmount),
                                                aamount = BigDecimal(formattedAamount),

                                            )
                                        } else {
                                            booking
                                        }
                                    }
                                    Log.d("OLFA", "Formatted Values: start=$startFormatted, end=$updatedMappedBookings")

                                    val totalAmountBigDecimal = BigDecimal.valueOf(totalAmountSelected)
                                    val currency = selectedReservation.price.takeWhile { !it.isDigit() && it != '.' }
                                    onTotalAmountCalculated(totalAmountSelected, currency)

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
                                                        currency =  "DT",
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
                                                                isLoading = false

                                                                val paymentResponse = paymentResult.data as? PaymentResponse
                                                                val formUrl = paymentResponse?.formUrl

                                                                if (!formUrl.isNullOrEmpty()) {
                                                                    val encodedUrl = Uri.encode(formUrl)
                                                                    val userIdsString = selectedPlayers.joinToString(",")
                                                                    val encodedUserIds = Uri.encode(userIdsString)
                                                                    val SharedListString = sharedExtras.joinToString(",")
                                                                    val encodedSharedList = Uri.encode(SharedListString)
                                                                    val PrivateListString = privateExtras.joinToString(",")
                                                                    val encodedPrivateList = Uri.encode(PrivateListString)
                                                                    val encodedBookingId = Uri.encode(bookingId)
                                                                    val navigationRoute =
                                                                        "WebViewScreen?paymentUrl=$encodedUrl&numberOfPart=$currentSelectedParts&userIds=$encodedUserIds&sharedList=$encodedSharedList&privateList=$encodedPrivateList&bookingId=$encodedBookingId"

                                                                    // Navigate to WebViewScreen with the form URL
                                                                    navController.navigate(
                                                                        navigationRoute
                                                                    )
                                                                } else {
                                                                    isLoading = false

                                                                }
                                                            }

                                                            is DataResult.Failure -> {
                                                                isLoading = false
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    isLoading = false
                                                }
                                            }

                                            is DataResult.Failure -> {
                                                isLoading = false

                                            }
                                        }
                                    }
                                } else {
                                    isLoading = false

                                }
                            }
                        }
                    },

                    enabled = (phoneNumber.isEmpty() &&  updatePhoneResult is DataResult.Success && phoneNumber.length == 8 && phoneNumber.all { it.isDigit() } ) || (phoneNumber.isNotEmpty() && phoneNumber.length == 8 && phoneNumber.all { it.isDigit() }) ,

                    modifier = Modifier
                        .height(48.dp).offset(x = -28.dp)
                        .weight(1.2f), // Ensure both buttons take equal space
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0054D8)),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, // Aligns items vertically in the center
                        horizontalArrangement = Arrangement.Center // Centers the content horizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Payment,
                            contentDescription = "Card Payment",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp).offset(x=-7.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp)) // Increased spacing for better visibility
                        Text(
                            text = "Carte Crédit",
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                var amount by remember { mutableStateOf(BigDecimal.ZERO) }
                var showPopup by remember { mutableStateOf(false) }
                var bookingId by remember { mutableStateOf<String?>(null) }
                var hasFetchedSelectedParts by remember { mutableStateOf(false) }
                var hasFetchedBooking by remember { mutableStateOf(false) }
                var hasFetchedPaymentPayAvoir by remember { mutableStateOf(false) }
                Log.d("AMMMMMMMMMMMMMMMMMMMMM", "showPopup:  $showPopup")
                LaunchedEffect(showPopup) {
                    if (!showPopup) {
                        delay(300) // Allow state reset before next opening
                    }
                }
                Button(
                    onClick = {
                        if (isLoading) return@Button // Prevent clicking while processing
                        Log.d("BPPPPPPPPPPPPPPPPP", "showPopup:  $showPopup")
                        val selectedBooking = mappedBookings.firstOrNull()
                        if (!hasFetchedSelectedParts) {
                            viewModel9.updateSelectedParts(selectedParts)
                            hasFetchedSelectedParts = true
                        }

                        coroutineScope.launch {
                            viewModel9.selectedParts.collectLatest { currentSelectedParts ->
                                if (hasFetchedBooking) return@collectLatest // Prevent multiple booking calls


                                val selectedBooking = mappedBookings.firstOrNull()

                                if (selectedBooking != null) {
                                    val searchDate = selectedDate
                                    val totalAmountSelected = adjustedAmount + totalExtrasCost
                                    val firstPlanning = selectedBooking.plannings?.firstOrNull()
                                    val startFormatted = firstPlanning?.fromStr?.let { timeStr ->
                                        try {
                                            val formattedTimeStr = timeStr.padStart(5, '0') // Ensure "HH:mm" format
                                            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                                            LocalDateTime.of(searchDate, time).format(formatterOutput)
                                        } catch (e: Exception) {
                                            Log.e("DateConversion", "Error parsing startFormatted", e)
                                            searchDate.atTime(9, 30).format(formatterOutput) // Fallback
                                        }
                                    } ?: searchDate.atTime(9, 30).format(formatterOutput)

                                    val endFormatted = firstPlanning?.toStr?.let { timeStr ->
                                        try {
                                            val formattedTimeStr = timeStr.padStart(5, '0') // Ensure "HH:mm" format
                                            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                                            LocalDateTime.of(searchDate, time).format(formatterOutput)
                                        } catch (e: Exception) {
                                            Log.e("DateConversion", "Error parsing endFormatted", e)
                                            searchDate.atTime(9, 30).format(formatterOutput) // Fallback
                                        }
                                    } ?: searchDate.atTime(9, 30).format(formatterOutput)

                                    val fromFormatted = firstPlanning?.fromStr?.let { timeStr ->
                                        try {
                                            val formattedTimeStr = timeStr.padStart(5, '0') // Ensure correct length
                                            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                                            LocalDateTime.of(searchDate, time).format(formatterWithMillis)
                                        } catch (e: Exception) {
                                            Log.e("DateConversion", "Error parsing fromFormatted", e)
                                            searchDate.atTime(9, 30).format(formatterWithMillis) // Fallback
                                        }
                                    } ?: searchDate.atTime(9, 30).format(formatterWithMillis)

                                    val toFormatted = firstPlanning?.toStr?.let { timeStr ->
                                        try {
                                            val formattedTimeStr = timeStr.padStart(5, '0') // Ensure correct length
                                            val time = LocalTime.parse(formattedTimeStr, DateTimeFormatter.ofPattern("HH:mm"))
                                            LocalDateTime.of(searchDate, time).format(formatterWithMillis)
                                        } catch (e: Exception) {
                                            Log.e("DateConversion", "Error parsing toFormatted", e)
                                            searchDate.atTime(9, 30).format(formatterWithMillis) // Fallback
                                        }
                                    } ?: searchDate.atTime(9, 30).format(formatterWithMillis)
                                    if (totalAmountSelected <= 0) {
                                        isLoading = false
                                        return@collectLatest
                                    }

                                    val formattedAmount = selectedBooking.amount?.stripTrailingZeros()?.toPlainString()?.let {
                                        if (it.contains(".")) it else it.toInt().toString()
                                    } ?: "0"

                                    val formattedAamount = selectedBooking.aamount?.stripTrailingZeros()?.toPlainString()?.let {
                                        if (it.contains(".")) it else it.toInt().toString()
                                    } ?: "0"

                                    val playerIds = selectedPlayers.toList()
                                    val totalAmountBigDecimal = BigDecimal.valueOf(totalAmountSelected).setScale(0, RoundingMode.DOWN)
                                    val currency = selectedReservation.price.takeWhile { !it.isDigit() && it != '.' }
                                    onTotalAmountCalculated(totalAmountBigDecimal.toInt().toDouble(), currency)


                                    // Trigger PaymentPayAvoir with rounded amount
                                    paymentPayAvoirViewModel.PaymentPayAvoir(totalAmountBigDecimal)

                                    if (!hasFetchedPaymentPayAvoir) {
                                        paymentPayAvoirViewModel.PaymentPayAvoir(totalAmountBigDecimal)
                                        hasFetchedPaymentPayAvoir = true
                                    }

                                    paymentPayAvoirViewModel.dataResult.observe(lifecycleOwner) { paymentResult ->
                                        when (paymentResult) {
                                            is DataResult.Loading -> {
                                                Log.d("PAYMENT", "Processing PaymentPayAvoir...")
                                            }

                                            is DataResult.Success -> {
                                                isLoading = false
                                                val payFromAvoirResponse = true


                                                Log.d("FormattedAmount", "formattedAmount: $formattedAmount")

                                                val updatedMappedBookings = mappedBookings.mapIndexed { index, booking ->
                                                    if (index == 0) {
                                                        booking.copy(
                                                            numberOfPart = currentSelectedParts,
                                                            userIds = playerIds,
                                                            payFromAvoir = payFromAvoirResponse,
                                                            searchDate = searchDate.toString(),
                                                            start = startFormatted,
                                                            end = endFormatted,
                                                            from = fromFormatted,
                                                            to = toFormatted,
                                                            sharedExtrasIds = sharedExtras,
                                                            privateExtrasIds = privateExtras,
                                                            amount = BigDecimal(formattedAmount),
                                                            aamount = BigDecimal(formattedAamount),
                                                            establishmentPacksDTO = emptyList()
                                                        )
                                                    } else {
                                                        booking
                                                    }
                                                }
                                                Log.d("AFFFFFFFFFFFFFAAAAAA", "Formatted Values: start=$startFormatted, end=$updatedMappedBookings")

                                                balanceViewModel.fetchAndBalance()
                                                if (!hasFetchedBooking) {
                                                    saveBookingViewModel.SaveBooking(updatedMappedBookings)
                                                    hasFetchedBooking = true
                                                }


                                            saveBookingViewModel.dataResult.observe(lifecycleOwner) { result ->
                                                when (result) {
                                                    is DataResult.Loading -> {
                                                        Log.d("SaveBooking", "Saving booking...")
                                                    }

                                                    is DataResult.Success -> {
                                                        isLoading = false
                                                        Log.d("SaveBooking", "Booking saved successfully!")

                                                        val bookingList = result.data as? List<SaveBookingResponse>
                                                        if (!bookingList.isNullOrEmpty()) {
                                                            val firstBooking = bookingList[0]

                                                            bookingId = firstBooking.id.toString()
                                                            Log.d("SaveBooking", "Extracted Booking ID: $bookingId")

                                                        }
                                                    }

                                                    is DataResult.Failure -> {
                                                        isLoading = false
                                                        Log.e("SaveBooking", "Failed to save booking: ${result.errorMessage}")
                                                    }
                                                    }
                                                }
                                            }
// on click only
                                            is DataResult.Failure -> {
                                                isLoading = false
                                                Log.e("PAYMENT", "PaymentPayAvoir failed: ${paymentResult.errorMessage}")
                                            }
                                        }
                                    }
                                }

                            }
                        }
                        showPopup = true
                    },
                    enabled = phoneNumber.length == 8 && phoneNumber.all { it.isDigit() },
                    modifier = Modifier
                        .height(48.dp).offset(x=-7.dp)
                        .weight(1.3f), // Ensure both buttons take equal space
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0054D8)),
                    shape = RoundedCornerShape(13.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically, // Aligns items vertically in the center
                        horizontalArrangement = Arrangement.Center // Centers the content horizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Money,
                            contentDescription = "Credits Payment",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp).offset(x = -7.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "Crédit Padelium",
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                }
                if (showPopup) {
                    PopupCredit(
                        onPayClick = {
                            // Handle pay click logic here
                            showPopup = false // Close the popup after payment
                        },
                        onCancelClick = { showPopup = false }, // Close the popup
                        viewModel = hiltViewModel(), // Pass the GetProfileViewModel
                        navController = navController, // Pass the NavController
                        errorCreditViewModel = hiltViewModel(), // Pass the ErrorCreditViewModel
                        onTotalAmountCalculated = { amount, currency -> },
                        adjustedAmount = adjustedAmount, // Pass adjustedAmount
                        totalExtrasCost = totalExtrasCost, // Pass totalExtrasCost
                        showPopup = showPopup, // Toggle state correctly
                        onDismiss = { showPopup = false }, // Handle dismissal
                        mappedBookingsJson = mappedBookingsJson, // Pass mapped bookings data
                        viewModel9 = viewModel9, // Pass shared view model
                        findTermsViewModel = hiltViewModel(), // Pass FindTermsViewModel
                        selectedDate = selectedDate, // Pass selected date
                        selectedReservation = selectedReservation, // Pass selected reservation
                        saveBookingViewModel = hiltViewModel(), // Pass SaveBookingViewModel
                        bookingId = bookingId,
                    )
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
    bookingIds: String,
    onReservationClicked: (LocalDate) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()
    val dataResult by getPaymentViewModel.dataResult.observeAsState()
    val selectedPlayers by findTermsViewModel.selectedPlayers.observeAsState(initial = mutableListOf())
    val userIdsList = userIds.split(",").mapNotNull { it.toLongOrNull() }
    val sharedListIds = sharedList.split(",").mapNotNull { it.toLongOrNull() }
    val privateListIds = privateList.split(",").mapNotNull { it.toLongOrNull() }
    val isWebViewExpanded = remember { mutableStateOf(true) }
    val lastLoadedUrl = remember { mutableStateOf(formUrl) }
    val bookingIdsState = remember { mutableStateOf("") } // Mutable state for booking IDs
    val errorCreditViewModel: ErrorCreditViewModel = hiltViewModel()
    val bookingIdsList = bookingIds.split(",").mapNotNull { it.toLongOrNull() }
    var isButtonClicked by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    Log.d("WebViewScreen", "Selected Parts: $numberOfPart")
    Log.d("WebViewScreen", "Extracted userIds: $userIdsList")
    Log.e("Faaaaaaaaaaaaaaaaaaaaaaaaa", "Selected Parts: $bookingIdsList")

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .border(2.dp, Color.Gray, RoundedCornerShape(12.dp))
    ) {
        AndroidView(factory = {
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                        url?.let {
                            if (it.contains("paymentSuccess")) {
                                Log.e("paymentSuccess", "Payment successful URL: $it")
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
        }, modifier = Modifier.fillMaxSize()
        )

        IconButton(
            onClick = {
                val creditErrorRequest = CreditErrorRequest(
                    amount = BigDecimal.ZERO,
                    bookingIds = bookingIdsList,
                    buyerId = 0L,
                    payFromAvoir = false,
                    status = true,
                    token = "",
                    transactionId = 0L
                )
                coroutineScope.launch {
                    errorCreditViewModel.ErrorCredit(creditErrorRequest)
                }
                val selectedDate = LocalDate.now()

                if (!isButtonClicked) {
                    isButtonClicked = true

                    selectedDate?.let { date ->
                        isLoading = true
                        onReservationClicked(date)
                    } ?: Log.e("MainScreen", "Selected date is null")
                }
                val selectedTimeSlot = extractSelectedTimeSlot(lastLoadedUrl.value) ?: ""

                isWebViewExpanded.value = false
                //   navController.navigate("reservation_options/$selectedDate/$selectedTimeSlot")
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .background(Color.White, shape = CircleShape)
        ) {
            Icon(imageVector = Icons.Default.Close, contentDescription = "Close WebView", tint = Color.Black)
        }
    }

    // Observe the dataResult from the errorCreditViewModel
    val errorDataResult by errorCreditViewModel.dataResult.observeAsState()
    errorDataResult?.let { result ->
        when (result) {
            is DataResult.Loading -> {
                Log.d("WebViewScreen", "Processing error credit request...")
            }
            is DataResult.Success -> {
                Log.d("WebViewScreen", "Error credit processed successfully.")
            }
            is DataResult.Failure -> {
                Log.e("WebViewScreen", "Error processing credit: ${result.exception}")
            }
        }
    }

    dataResult?.let { result ->
        when (result) {
            is DataResult.Loading -> {
                Log.d("WebViewScreen", "Fetching payment details...")
            }
            is DataResult.Success -> {
                navController.navigate("PaymentSuccessScreen")
            }
            is DataResult.Failure -> {
                Log.e("WebViewScreen", "Error fetching payment details", result.exception)
            }
        }
    }
}
fun extractSelectedDate(url: String): String? {
    return Uri.parse(url).getQueryParameter("selectedDate")
}

fun extractSelectedTimeSlot(url: String): String? {
    return Uri.parse(url).getQueryParameter("selectedTimeSlot")
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






















