package com.nevaDev.padeliummarhaba.ui.views

import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.nevaDev.padeliummarhaba.viewmodels.TimeSlot
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.dto.SaveBookingRequest
import com.padelium.data.dto.GetBookingResponseDTO
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.PlanningDTO
import java.math.BigDecimal
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun GetBookingResponse.toDTO(): GetBookingResponseDTO {
    val startFormatted = this.start?.let {
        Instant.parse(it).atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    } ?: "2024-12-20 9:30" // Default value if null

    val endFormatted = this.end?.let {
        Instant.parse(it).atZone(ZoneId.systemDefault())
            .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
    } ?: "2024-12-20 9:30" // Default value if null

    val fromFormatted = this.from?.let {
        Instant.parse(it).atZone(ZoneId.of("UTC"))
            .format(DateTimeFormatter.ISO_INSTANT)
    } ?: Instant.now().atZone(ZoneId.of("UTC"))
        .format(DateTimeFormatter.ISO_INSTANT) // Default to current time if null

    return GetBookingResponseDTO(
        // Map all fields appropriately
        aamount = this.aamount ?: BigDecimal.ZERO,
        amount = this.amount ?: 0.0,
        amountfeeTrans = this.amountfeeTrans ?: BigDecimal.ZERO,
        bookingAnnulationDTOSet = this.bookingAnnulationDTOSet ?: emptyList(),
        isClient = this.isClient ?: true,
        closeTime = this.closeTime?.toString() ?: Instant.now().toString(),
        orderId = this.orderId ?: 0L,
        id = this.id ?: 0L,

        couponCode = this.couponCode ?: "",
        currencyId = this.currencyId ?: 0L,
        currencySymbol = this.currencySymbol ?: "",
        decimalNumber = this.decimalNumber ?: 0,
        description = this.description ?: "",
        end = endFormatted,
        establishmentDTO = this.establishmentDTO,
        establishmentPacksDTO = this.establishmentPacksDTO ?: emptyList(),
        establishmentPacksId = this.establishmentPacksId ?: 0L,
        EstablishmentPictureDTO = this.EstablishmentPictureDTO ?: emptyList(),
        facadeUrl = this.facadeUrl ?: "",
        from = fromFormatted,
        HappyHours = this.HappyHours ?: emptyList(),
        mgAmount = this.mgAmount ?: BigDecimal.ZERO,
        moyFeed = this.moyFeed ?: 0.0,
        numberOfPart = this.numberOfPart ?: 0.0,
        numberOfPlayer = this.numberOfPlayer ?: 0,
        openTime = this.openTime?.toString() ?: Instant.now().toString(),
        payFromAvoir = this.payFromAvoir ?: false,
        plannings = this.plannings ?: emptyList(),
        privateExtrasIds = this.privateExtrasIds ?: emptyList(),
        ramountfeeTrans = this.ramountfeeTrans ?: BigDecimal.ZERO,
        reduction = this.reduction ?: BigDecimal.ZERO,
        reductionAmount = this.reductionAmount ?: BigDecimal.ZERO,
        reductionSecondAmount = this.reductionSecondAmount ?: BigDecimal.ZERO,
        reductionaAmount = this.reductionaAmount ?: BigDecimal.ZERO,
        reductionaSecondAmount = this.reductionaSecondAmount ?: BigDecimal.ZERO,
        rsamountfeeTrans = this.rsamountfeeTrans ?: BigDecimal.ZERO,
        samountfeeTrans = this.samountfeeTrans ?: BigDecimal.ZERO,
        searchDate = this.searchDate ?: "",
        secondAamount = this.secondAamount ?: BigDecimal.ZERO,
        secondAmount = this.secondAmount ?: BigDecimal.ZERO,
        secondReduction = this.secondReduction ?: 0,
        sharedExtrasIds = this.sharedExtrasIds ?: emptyList(),
        to = this.to?.let { Instant.parse(it.toString()).toString() } ?: Instant.now().toString(),
        start = startFormatted,
        totalFeed = this.totalFeed ?: 0,
        users = this.users ?: emptyList(),
        usersIds = this.usersIds ?: emptyList(),
        withSecondPrice = this.withSecondPrice ?: false,
    )
}

@Composable
fun PaymentSection10(
    selectedDate: LocalDate,
    selectedTimeSlot: String,
    selectedReservation: ReservationOption,
    onExtrasUpdate: (Int, Int, Boolean) -> Unit,
    onPayWithCardClick: () -> Unit,
    totalAmount: String,
    navController: NavController,
    viewModel: SaveBookingViewModel = hiltViewModel(),
    getBookingResponseDTO: GetBookingResponseDTO,
    filteredTimeSlots: List<TimeSlot>,
    isUserLoggedIn: Boolean,
    bookingViewModel: BookingViewModel,
    // sharedModel: SharedModel = hiltViewModel(),
    paymentRequest: PaymentRequest,
) {
    //  val mappedBookings by sharedModel.mappedBookings.collectAsState(initial = emptyMap())
    var showLoginPopup by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        PhoneNumberSection()
        Spacer(modifier = Modifier.height(16.dp))
        ExtrasDetailsSection(
            selectedReservation = selectedReservation,
            onExtrasUpdate = onExtrasUpdate,
            selectedDate = selectedDate,
            selectedTimeSlot = selectedTimeSlot
        )
        Spacer(modifier = Modifier.height(16.dp))
        PaymentOptionsSection(
            availablePlannings = getBookingResponseDTO.plannings.filter { planning ->
                filteredTimeSlots.any { slot -> slot.time == planning.fromStr }
            },
            navController = navController,
            bookingViewModel = bookingViewModel,
            isUserLoggedIn = isUserLoggedIn,
            // sharedModel = sharedModel,
            saveBookingViewModel = viewModel,
            showLoginPopupCallback = { showLoginPopup = true },
            getBookingResponseDTO = getBookingResponseDTO,

            )
    }
}
@Composable
fun PhoneNumberSection() {
    var phoneNumber by remember { mutableStateOf("") }

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
                    .padding(horizontal = 10.dp),
                color = Color.Gray,
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Numéro de téléphone",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(18.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                PhoneNumberInput(phoneNumber) { phoneNumber = it }
                Spacer(modifier = Modifier.width(10.dp))
                ModifierButton()
            }
        }
    }
}

@Composable
fun PhoneNumberInput(phoneNumber: String, onPhoneNumberChange: (String) -> Unit) {
    BasicTextField(
        value = phoneNumber,
        onValueChange = { if (it.all { char -> char.isDigit() } && it.length <= 8) onPhoneNumberChange(it) },
        modifier = Modifier
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
                Icon(imageVector = Icons.Default.Phone, contentDescription = null, modifier = Modifier.padding(8.dp))
                Text("|", fontSize = 29.sp, color = Color.Black, modifier = Modifier.offset(x = -8.dp))
                Box(modifier = Modifier.fillMaxWidth()) { innerTextField() }
            }
        }
    )
}

@Composable
fun ModifierButton() {
    Button(
        onClick = { /* Handle modification */ },
        modifier = Modifier
            .width(120.dp)
            .height(48.dp)
            .border(1.dp, Color(0xFF0054D8), RoundedCornerShape(13.dp)),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        shape = RoundedCornerShape(15.dp)
    ) {
        Text(text = "Modifier", color = Color(0xFF0054D8), fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}

@Composable
fun ExtrasDetailsSection(
    selectedReservation: ReservationOption,
    onExtrasUpdate: (Int, Int, Boolean) -> Unit,
    selectedDate: LocalDate,
    selectedTimeSlot: String,

    ) {
    var selectedExtras by remember { mutableStateOf<List<Triple<String, String, Int>>>(emptyList()) }
    var totalExtrasCost by remember { mutableStateOf(0.0) }
    var selectedRaquette by remember { mutableStateOf(1) }
    var includeBalls by remember { mutableStateOf(false) }

    ExtrasSection(
        onExtrasUpdate = { extras, cost ->
            selectedExtras = extras
            totalExtrasCost = cost
        }
    )
    ReservationSummary(
        selectedDate = selectedDate,
        selectedTimeSlot = selectedTimeSlot ?: "Not selected",
        selectedReservation = selectedReservation ?: ReservationOption(
            "Default",
            "Not selected",
            "100.0",
            "Not selected"
        ),
        selectedExtras = selectedExtras, // Pass the selectedExtras list directly
        selectedRaquette = selectedRaquette.toString(),
        includeBalls = includeBalls,
        amountSelected = selectedReservation?.let {
            val price =
                it.price.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0
            val currencySymbol =
                it.price.replace("[\\d.]".toRegex(), "") // Extract currency symbol
            Pair(price, currencySymbol)
        },
        onTotalAmountCalculated = { totalAmountSelected, currencySymbol ->
            Log.d(
                "TotalAmountSelected",
                "The total amount is $totalAmountSelected and currency symbol is $currencySymbol"
            )
        }
    )
}


@Composable
fun PaymentOptionsSection(
    availablePlannings: List<PlanningDTO>,
    navController: NavController,
    bookingViewModel: BookingViewModel,
    isUserLoggedIn: Boolean,
    //sharedModel: SharedModel,
    saveBookingViewModel: SaveBookingViewModel,
    showLoginPopupCallback: () -> Unit,
    getBookingResponseDTO: GetBookingResponseDTO,

    ) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        availablePlannings.forEach { planning ->
            PaymentButton(
                label = "Card Crédit",
                icon = Icons.Default.Payment,
                onClick = {
                     /*  handleCardClick(
                           planning = planning,
                           navController = navController,
                           bookingViewModel = bookingViewModel,
                           isUserLoggedIn = isUserLoggedIn,
                           onLoginRequired = showLoginPopupCallback,
                           saveBookingViewModel = saveBookingViewModel,
                          // sharedModel = sharedModel,
                           getBookingResponseDTO = getBookingResponseDTO,

                           ) */
                }
            )
        }
        PaymentButton(
            label = "Crédit Padelium",
            icon = Icons.Default.Money,
            onClick = { /* Handle Crédit Padelium */ }
        )
    }
}

@Composable
fun PaymentButton(label: String, icon: ImageVector, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            // .weight(1f)
            .padding(8.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
        border = BorderStroke(2.dp, Color(0xFF0054D8))
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF0054D8))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label, fontWeight = FontWeight.Bold, color = Color.Gray)
        }
    }
}









