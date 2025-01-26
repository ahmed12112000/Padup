package com.nevaDev.padeliummarhaba.ui.views

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Share
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
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileByIdViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetReservationViewModel
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.GetReservationIDResponse
import com.padelium.domain.dto.GetReservationResponse
import kotlinx.coroutines.launch



@Composable
fun SummaryScreen(
    viewModel: GetReservationViewModel = hiltViewModel(),
    viewModel1: GetProfileByIdViewModel = hiltViewModel()
) {
    var showFilterMenu by remember { mutableStateOf(false) }
    val reservations = remember { mutableStateOf<List<GetReservationResponse>>(emptyList()) }
    var selectedReservation by remember { mutableStateOf<GetReservationResponse?>(null) }
    var selectedReservation1 by remember { mutableStateOf<GetReservationIDResponse?>(null) }
    var showDialog by remember { mutableStateOf(false) } // Manage dialog state here

    val sheetState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()

    // Observe the reservation data
    val reservationData by viewModel.ReservationsData.observeAsState()
    val profilesData by viewModel1.profilesData.observeAsState()

    // On data change, update reservations list
    LaunchedEffect(reservationData) {
        if (reservationData is DataResultBooking.Success) {
            reservations.value =
                (reservationData as DataResultBooking.Success<List<GetReservationResponse>>).data
        }
    }

    // Observe profiles data to update selectedReservation1
    LaunchedEffect(profilesData) {
        if (profilesData is DataResultBooking.Success) {
            selectedReservation1 = (profilesData as DataResultBooking.Success<GetReservationIDResponse>).data
        }
    }

    // Fetch reservations when screen is launched
    LaunchedEffect(Unit) {
        viewModel.GetReservation()
    }

    ModalBottomSheetLayout(
        sheetContent = {
            selectedReservation?.let { reservation ->
                selectedReservation1?.let { reservation1 ->
                    ReservationCard1(reservation, reservation1)
                }
            }
        },
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
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

            // Filter and Sort Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(16.dp))
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

            // Use the dynamic list of reservations
            reservations.value.forEach { reservation ->
                ReservationCard(
                    reservation = reservation,
                    onClick = {
                        selectedReservation = reservation
                        // Fetch profile data based on the selected reservation
                        viewModel1.fetchProfileById(reservation.id)
                        coroutineScope.launch { sheetState.show() }
                    },
                    viewModel1 = viewModel1,
                    onShowDialog = { showDialog = true } // Pass the dialog state handler
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
                            .padding(16.dp)
                    ) {
                        ReservationCard1(reservation, reservation1)
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
    onShowDialog: () -> Unit // New parameter for showing the dialog

) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = reservation.establishmentName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Text(
                    text = "${reservation.localAmount} ${reservation.currencyFromSymbol}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${reservation.bookingDate} | ${reservation.fromStrTime} - ${reservation.toStrTime}",
                fontWeight = FontWeight.Normal,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            StatusBadge(reservation.isConfirmed)

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth().offset(y = -40.dp),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = {
                    viewModel1.fetchProfileById(reservation.id)
                    onClick()
                }) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = "View")
                }
            }
        }
    }

}


@Composable
fun ReservationCard1(reservation: GetReservationResponse,reservation1 : GetReservationIDResponse) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .fillMaxHeight(0.95f)
            .padding(8.dp),
        // verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Top Section
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = reservation.establishmentName,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 26.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${reservation.bookingDate}",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${reservation.fromStrTime} - ${reservation.toStrTime}",
                fontWeight = FontWeight.Bold,
                fontSize = 17.sp
            )
        }

        Spacer(modifier = Modifier.height(26.dp))

        // Middle Section (Table Layout with Dividers)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, Color.LightGray), shape = RoundedCornerShape(8.dp))
        ) {
            // Row 1: Référence
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Référence N° :",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Gray
                )
                Text(
                    text = reservation.reference,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = Color.Gray

                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)

            // Row 2: État
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "État :",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Gray

                )
                Text(
                    text = reservation.bookingStatusName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = Color.Gray

                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)

            // Row 3: Prix
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Prix :",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Gray

                )
                Text(
                     text = "${reservation.sellAmount}  ${reservation.currencyFromSymbol}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Gray

                )
            }
            Divider(color = Color.LightGray, thickness = 1.dp)

            // Row 4: Extra
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Créé par :",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Gray

                )
                Text(
                    text = reservation.userEmail,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Gray

                )
            }
            // Row 1: Référence
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Payée par :",
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color.Gray
                )

                Log.d("BookingUsersList", "Users: ${reservation1.bookingUsersPaymentListDTO}")

                if (reservation1.bookingUsersPaymentListDTO.isNotEmpty()) {
                    Column {
                        reservation1.bookingUsersPaymentListDTO.forEach { user ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val userName = if (user.userFirstName == null && user.userLastName == null) {
                                    "Invite"
                                } else {
                                    "${user.userFirstName ?: ""} ${user.userLastName ?: ""}".trim()
                                }
                                Text(
                                    text = userName,
                                    fontSize = 18.sp,
                                    color = Color.Gray
                                )

                                val paymentStatus = when (user.bookingUsersPaymentStatusCode) {
                                    "PAY" -> "Payée"
                                    "WAIT" -> "En attente de paiement"
                                    else -> "Unknown"
                                }
                                Text(
                                    text = "${user.amountstr}",
                                    fontSize = 18.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    text = paymentStatus,
                                    fontSize = 16.sp,
                                    color = if (user.bookingUsersPaymentStatusCode == "PAY") Color.Green else Color.Red
                                )
                            }
                        }
                    }
                } else {
                    Text(
                        text = "No payments available",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                }
            }



            Divider(color = Color.LightGray, thickness = 1.dp)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bottom Section with Actions
        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
            }

        }
    }
}






@Composable
fun StatusBadge(isConfirmed: Boolean) {
    val backgroundColor = if (isConfirmed) Color(0xFF4CAF50) else Color(0xFFF44336)
    val statusText = if (isConfirmed) "Confirmée" else "Annulée"

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .padding(vertical = 4.dp, horizontal = 8.dp)
        //.offset(y = 5.dp)
    ) {
        Text(text = statusText, color = Color.White, fontWeight = FontWeight.Bold)
    }
}



@Preview(showBackground = true)
@Composable
fun SummaryScreenPreview() {
    SummaryScreen()
}


    // Display the ReservationCard1

