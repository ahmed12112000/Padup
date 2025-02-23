package com.nevaDev.padeliummarhaba.ui.views

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nevaDev.padeliummarhaba.ui.activities.SharedViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileByIdViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetReservationViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetStatusesViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PartnerPayViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PrivateExtrasViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SharedExtrasViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.GetReservationIDResponse
import com.padelium.domain.dto.GetReservationResponse
import com.padelium.domain.dto.GetStatusesResponse
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale



@Composable
fun SummaryScreen(
    viewModel: GetReservationViewModel = hiltViewModel(),
    viewModel1: GetProfileByIdViewModel = hiltViewModel(),
    viewModel2: GetStatusesViewModel = hiltViewModel(),
    navController: NavController,
    navigateToLogin: (String) -> Unit,
    ) {
    var showFilterMenu by remember { mutableStateOf(false) }
    val reservations = remember { mutableStateOf<List<GetReservationResponse>>(emptyList()) }
    val reservations1 = remember { mutableStateOf<List<GetStatusesResponse>>(emptyList()) }

    var selectedReservation by remember { mutableStateOf<GetReservationResponse?>(null) }
    var selectedReservation1 by remember { mutableStateOf<GetReservationIDResponse?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    val reservationData by viewModel.ReservationsData.observeAsState()
    val profilesData by viewModel1.profilesData.observeAsState()
    val reservationData1 by viewModel2.ReservationsData1.observeAsState()



    LaunchedEffect(reservationData) {
        if (reservationData is DataResultBooking.Success) {
            reservations.value =
                (reservationData as DataResultBooking.Success<List<GetReservationResponse>>).data
        }
    }

    LaunchedEffect(profilesData) {
        if (profilesData is DataResultBooking.Success) {
            selectedReservation1 =
                (profilesData as DataResultBooking.Success<GetReservationIDResponse>).data
        }
    }

    LaunchedEffect(reservationData1) {
        if (reservationData1 is DataResultBooking.Success) {
            reservations1.value =
                (reservationData1 as DataResultBooking.Success<List<GetStatusesResponse>>).data
        }
    }

    LaunchedEffect(Unit) {
        viewModel.GetReservation()
    }

    LaunchedEffect(sheetState.isVisible) {
        if (sheetState.isVisible) {
            coroutineScope.launch {
                sheetState.show() // Ensure the sheet properly displays
            }
        }
    }

    ModalBottomSheetLayout(
        sheetContent = {
            selectedReservation?.let { reservation ->
                selectedReservation1?.let { reservation1 ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.87f) // Ensure height is properly set
                    ) {
                        ReservationCard1(reservation, reservation1, bookingStatusCode = reservation.bookingStatusCode)
                    }
                }
            }
        },
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "Mes réservations",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 1.dp,
                color = Color.Gray
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(
                        width = 1.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Box {
                    OutlinedButton(
                        onClick = { showFilterMenu = !showFilterMenu },
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = "Filter",
                            tint = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Filtrer", color = Color.Gray)
                    }
                    DropdownMenu(
                        expanded = showFilterMenu,
                        onDismissRequest = { showFilterMenu = false }
                    ) {
                        DropdownMenuItem(onClick = { showFilterMenu = false }) {
                            Text("By Date")
                        }
                        DropdownMenuItem(onClick = { showFilterMenu = false }) {
                            Text("By Name")
                        }
                        DropdownMenuItem(onClick = { showFilterMenu = false }) {
                            Text("By Type")
                        }
                    }
                }

                Divider(
                    color = Color.LightGray,
                    modifier = Modifier
                        .height(24.dp)
                        .width(1.dp)
                )

                OutlinedButton(onClick = {
                    reservations.value = reservations.value.reversed()
                    coroutineScope.launch {
                        sheetState.hide() // Hide the bottom sheet
                        selectedReservation = null // Reset selected reservation
                        selectedReservation1 = null // Reset selected reservation 1
                    }
                }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Sort,
                        contentDescription = "Sort",
                        tint = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Sort", color = Color.Gray)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            reservations.value.forEach { reservation ->
                ReservationCard(
                    reservation = reservation,
                    onClick = {
                        selectedReservation = reservation
                        viewModel1.fetchProfileById(reservation.id)
                        coroutineScope.launch {
                            sheetState.show() // Show the bottom sheet when a reservation is selected
                        }
                    },
                    viewModel1 = viewModel1,
                    bookingStatusCode = reservation.bookingStatusCode,
                    onShowDialog = { showDialog = true },
                    navController = navController
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }

    // Show dialog if needed
    if (showDialog) {
        selectedReservation?.let { reservation ->
            selectedReservation1?.let { reservation1 ->
                Dialog(onDismissRequest = { showDialog = false }) {
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(36.dp)
                    ) {
                        ReservationCard1(reservation, reservation1, bookingStatusCode = reservation.bookingStatusCode)
                    }
                }
            }
        }
    }
}

@Composable
fun ReservationCard(
    reservation: GetReservationResponse,
    onClick: () -> Unit,
    viewModel1: GetProfileByIdViewModel,
    bookingStatusCode: String,
    onShowDialog: () -> Unit,
    viewModel3: PrivateExtrasViewModel = hiltViewModel(),
    navController: NavController,
    viewModel2: GetProfileByIdViewModel = hiltViewModel(),
    viewModel4: PartnerPayViewModel = hiltViewModel(),
    viewModel5: PartnerPayViewModel = hiltViewModel(),

    ) {
    val privateExtrasState by viewModel3.extrasState2.observeAsState()
    val sharedList = remember { mutableStateOf<MutableList<Long>>(mutableListOf()) }
    val privateList = remember { mutableStateOf<MutableList<Long>>(mutableListOf()) }
    val bookingDate = reservation.bookingDate // Assuming this is a String like "22-01-2025"
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.FRANCE)
    var selectedReservation by remember { mutableStateOf<GetReservationResponse?>(null) }
    val coroutineScope = rememberCoroutineScope()

    var selectedReservation1 by remember { mutableStateOf<GetReservationIDResponse?>(null) }
    val date = LocalDate.parse(bookingDate, formatter)

    // Extract first 3 letters of the day and month in French
    val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.FRANCE).replaceFirstChar { it.uppercaseChar() }
    val month = date.month.getDisplayName(TextStyle.SHORT, Locale.FRANCE).replaceFirstChar { it.uppercaseChar() }

    val formattedDate = "$dayOfWeek ${date.dayOfMonth} $month ${date.year}"
    var showDialog by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    var showExtrasSection by remember { mutableStateOf(false) } // State to track visibility
    val profilesData by viewModel1.profilesData.observeAsState()
    val profileData by viewModel2.profilesData.observeAsState()
    val partnerPayResponse by viewModel5.partnerPayResponse.observeAsState()

    LaunchedEffect(partnerPayResponse) {
        partnerPayResponse?.let { response ->
            Log.d("ReservationCard", "PartnerPay response updated: $response")
            // Perform any necessary UI updates or side effects when partnerPayResponse updates
        }
    }


    LaunchedEffect(profilesData) {
        if (profilesData is DataResultBooking.Success) {
            selectedReservation1 =
                (profilesData as DataResultBooking.Success<GetReservationIDResponse>).data
        }
    }

    LaunchedEffect( viewModel3) {
        viewModel3.PrivateExtras()
    }
    Card(
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp) // Reduce padding to shrink card size
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(6.dp)) { // Reduce padding inside
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = reservation.establishmentName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                val formattedAmount = if (reservation.localAmount == 0.0) {
                    "0"
                } else {
                    String.format("%.2f", reservation.localAmount)
                }

                Text(
                    text = "$formattedAmount ${reservation.currencyFromSymbol}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(4.dp)) // Reduce spacing

            Column {
                Text(
                    text = formattedDate, // Use formatted date
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "${reservation.fromStrTime} - ${reservation.toStrTime}",
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(10.dp)) // Reduce spacing

            Text(
                text = reservation.bookingStatusName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(when (reservation.bookingStatusCode) {
                        "PAY" -> Color(0xFF4CAF50) // Green for paid
                        "WAIT" -> Color(0xFFFBC02D) // Yellow for waiting
                        else -> Color(0xFF4CAF50) // Default to green for unknown
                    })
                    .padding(vertical = 4.dp, horizontal = 8.dp)
            )
            Spacer(modifier = Modifier.height(-6.dp)) // Reduce spacing

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (selectedReservation != null && selectedReservation1 != null) {
                    LaunchedEffect(sheetState) {
                        sheetState.show() // Show the bottom sheet when a reservation is selected
                    }
                }
                if (reservation.activePayment) {
                    IconButton(
                        onClick = {
                            viewModel3.PrivateExtras()
                            viewModel2.fetchProfileById(reservation.id)
                            viewModel4.partnerPay(reservation.id)
                            Log.d("ASMMMMMMMMMA", "PartnerPay response updated: ${reservation.id}")
                            navController.navigate("PartnerPaymentScreen/${reservation.id}") // Navigate directly with reservation.id



                        },
                        modifier = Modifier.size(24.dp) // Match view icon size
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachMoney, // Dollar sign icon
                            contentDescription = "Payment Active",
                            modifier = Modifier.size(18.dp), // Smaller icon
                            tint = Color.Black // Highlight it with green
                        )
                    }
                }
                IconButton(
                    onClick = {
                        viewModel1.fetchProfileById(reservation.id)
                        onClick()
                        coroutineScope.launch {
                            sheetState.hide() // Hide the bottom sheet
                            selectedReservation = null // Reset selected reservation
                            selectedReservation1 = null // Reset selected reservation 1
                        }
                    },
                    modifier = Modifier.size(24.dp) // Reduce icon size
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = "View",
                        modifier = Modifier.size(18.dp) // Smaller icon
                    )
                }
            }
        }
    }

}


@Composable
fun ReservationCard1(
    reservation: GetReservationResponse,
    reservation1: GetReservationIDResponse,
    bookingStatusCode: String
) {
    val bookingDate = reservation.bookingDate
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.FRANCE)
    val date = LocalDate.parse(bookingDate, formatter)
    val formattedDate = date.format(DateTimeFormatter.ofPattern("EEEE d MMM yyyy", Locale.FRANCE))
    val formattedCreatedDate = date.format(DateTimeFormatter.ofPattern("EEEE d MMM yyyy", Locale.FRANCE))

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Section (Establishment Details)
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    androidx.compose.material3.Icon(
                        painter = painterResource(id = R.drawable.logopadelium),
                        contentDescription = "Establishment Icon",
                        modifier = Modifier
                            .size(60.dp)
                            .background(Color(0xFF0054D8), shape = CircleShape)
                            .padding(5.dp),
                        tint = Color.Unspecified
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = reservation.establishmentName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "${reservation.sellAmount} ${reservation.currencyFromSymbol}",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color.Black
                        )

                        Text(
                            text = reservation.bookingStatusName,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(when (reservation.bookingStatusCode) {
                                    "PAY" -> Color(0xFF4CAF50) // Green for paid
                                    "WAIT" -> Color(0xFFFBC02D) // Yellow for waiting
                                    else -> Color(0xFF4CAF50) // Default to green for unknown
                                })
                                .padding(vertical = 4.dp, horizontal = 8.dp)
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = formattedDate,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Gray
                        )
                        Text(
                            text = ", ${reservation.fromStrTime} - ${reservation.toStrTime}",
                            fontWeight = FontWeight.Normal,
                            fontSize = 17.sp,
                            color = Color.Gray
                        )
                    }
                }
            }
        }

        // Section for Payment Info
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Payé par",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // First Section (Reservations and Payment Info)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    reservation1.bookingUsersPaymentListDTO.forEach { user ->
                        val userName =
                            if (user.userFirstName == null && user.userLastName == null) "Invité" else "${user.userFirstName} ${user.userLastName}".trim()
                        val paymentStatus = when (user.bookingUsersPaymentStatusCode) {
                            "PAY" -> "Confirmée"
                            "WAIT" -> "En attente de paiement"
                            else -> "Inconnu"
                        }
                        val paymentColor =
                            if (user.bookingUsersPaymentStatusCode == "PAY") Color(0xFF4CAF50) else Color(0xFFFFC107)

                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Left side: User Name
                            Text(userName, fontSize = 20.sp, color = Color.Gray)

                            // Right side: Amount and Payment Status
                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "${user.amountstr} Crédits",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp)) // Spacing between the amount and payment status
                                Text(
                                    text = paymentStatus,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = paymentColor
                                )
                            }
                        }
                        Divider()
                    }
                }
            }
        }

        // Section for Reservation Details
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                "Réservation",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    TableRow(label = "Référence", value = reservation.reference, isBold = true)
                    Divider()
                    TableRow(label = "Créé le", value = formattedCreatedDate, isBold = true)
                }
            }
        }
    }
}



@Composable
fun TableRow(label: String, value: String, isBold: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, fontSize = 16.sp, color = Color.LightGray)
        Text(value, fontSize = 16.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = Color.LightGray)
    }
}



@Composable
fun TableRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Gray
        )
        Text(
            text = value,
            fontWeight = FontWeight.Bold,
            fontSize = 19.sp,
            color = Color.Gray
        )
    }
}


@Composable
fun StatusBadge(bookingStatusName: String) {
    val (backgroundColor, statusText) = when (bookingStatusName) {
        "PAY" -> Pair(Color(0xFF4CAF50), "Payée") // Green for paid
        "WAIT" -> Pair(Color(0xFFFBC02D), "En attente de paiement") // Yellow for waiting
        else -> Pair(Color(0xFF4CAF50), "nnnnnn") //Green for paid
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(
            text = statusText,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

@Composable
fun StatusBadge1(statusCode: String) {
    val (backgroundColor, statusText) = when (statusCode) {
        "PAY" -> Pair(Color(0xFF4CAF50), "Validée") // Green for paid
        "WAIT" -> Pair(Color(0xFFFBC02D), "En attente de paiement") // Yellow for waiting
        else -> Pair(Color(0xFF4CAF50), "Not Defined") //Green for paid
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(color = Color.White)
            .padding(vertical = 4.dp, horizontal = 8.dp)
    ) {
        Text(
            text = statusText,
            color = (backgroundColor),
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp
        )
    }
}

/*
@Preview(showBackground = true)
@Composable
fun SummaryScreenPreview() {
    SummaryScreen()
}

 */


@Preview
@Composable
fun PreviewReservationCard1() {
    val mockReservation = GetReservationResponse(
        id = 1L,
        from = "10:00",
        to = "11:30",
        annulationDate = "",
        sellAmount = 50.0,
        purchaseAmount = 0.0,
        numberOfPlayer = 4,
        reference = "123456",
        description = "Reservation de test",
        isRefundable = true,
        created = "",
        updated = "",
        createdBy = 1L,
        updatedBy = 1L,
        currencyFromId = 1L,
        currencyToId = 1L,
        bookingStatusId = 1L,
        establishmentId = 1L,
        userId = 1L,
        userLogin = "testuser",
        establishmentName = "Padel Club",
        bookingStatusName = "",
        userEmail = "user@example.com",
        currencyFromSymbol = "€",
        gainFromManager = 0.0,
        bookingStatusCode = "CONF",
        userPhone = "123456789",
        cancelBook = false,
        cancel = false,
        isonline = true,
        activityName = "Padel",
        cityName = "Paris",
        establishmentCode = "PAD123",
        localAmount = 50.0,
        reduction = 0,
        showcancel = false,
        showfeedBack = false,
        bookingDate = "22-01-2025",
        token = "",
        paymentError = false,
        paymentprog = false,
        amountToPay = 50.0,
        sobflousCode = "",
        couponId = 0L,
        isCoupon = false,
        couponValue = "",
        couponCode = "",
        establishmentPacksId = 0L,
        establishmentTypeCode = "",
        isConfirmed = true,
        isFromEvent = false,
        establishmentPacksFirstTitle = "",
        establishmentPacksSecondTitle = "",
        usersIds = listOf(),
        fromStr = "10:00",
        toStr = "11:30",
        fromStrTime = "10:00",
        toStrTime = "11:30",
        activePayment = false,
        isWaitForPay = false,
        bookingLabelId = 1L,
        bookingLabelName = "",
        bookingLabelColors = "",
        sharedExtrasIds = listOf(),
        privateExtrasIds = listOf(),
        privateExtrasLocalIds = mutableMapOf(),
        userFirstName = "John",
        userLastName = "Doe",
        extras = listOf(),
        numberOfPart = 1,
        createdStr = ""
    )
    val mockReservation1 = GetReservationIDResponse(
        id = 1L,
        from = "10:00",
        to = "11:30",
        //annulationDate = ,
        sellAmount = 50.0.toBigDecimal(),
        purchaseAmount = 0.0.toBigDecimal(),
        numberOfPlayer = 4,
        reference = "123456",
        description = "Reservation de test",
        isRefundable = true,
        created = "",
        updated = "",
        createdBy = 1L,
        updatedBy = 1L,
        currencyFromId = 1L,
        currencyToId = 1L,
        bookingStatusId = 1L,
        establishmentId = 1L,
        userId = 1L,
        userLogin = "testuser",
        establishmentName = "Padel Club",
        bookingStatusName = "",
        userEmail = "user@example.com",
        currencyFromSymbol = "€",
        gainFromManager = 0L,
        bookingStatusCode = "CONF",
        userPhone = "123456789",
        cancelBook = false,
        cancel = false,
        isonline = true,
        activityName = "Padel",
        cityName = "Paris",
        establishmentCode = "PAD123",
        localAmount = 50.0.toBigDecimal(),
        reduction = 0,
        showcancel = false,
        showfeedBack = false,
        bookingDate = "22-01-2025",
        token = "",
        paymentError = false,
        paymentprog = false,
        amountToPay = 50.0.toBigDecimal(),
        sobflousCode = "",
        couponId = 0L,
        isCoupon = false,
        couponValue = "",
        couponCode = "",
        establishmentPacksId = 0L,
        establishmentTypeCode = "",
        isConfirmed = true,
        isFromEvent = false,
        establishmentPacksFirstTitle = "",
        establishmentPacksSecondTitle = "",
        usersIds = listOf(),
        fromStr = "10:00",
        toStr = "11:30",
        fromStrTime = "10:00",
        toStrTime = "11:30",
        activePayment = false,
        isWaitForPay = false,
        bookingLabelId = 1L,
        bookingLabelName = "",
        bookingLabelColors = "",
        sharedExtrasIds = listOf(),
        privateExtrasIds = listOf(),
        privateExtrasLocalIds = mutableMapOf(),
        userFirstName = "John",
        userLastName = "Doe",
        extras = listOf(),
        numberOfPart = 1,
        createdStr = "",
        bookingUsersPaymentListDTO = listOf(),
        annulationDate = Instant.now(),

        )
         val mockReservation2 = GetStatusesResponse(
        id = 1L,
        name = "10:00",
        code = "11:30",
        created = "123456",
        updated = "123456",
        isshow = false
         )
    ReservationCard1(reservation = mockReservation, reservation1 = mockReservation1, bookingStatusCode ="",
    )
}
