package com.nevaDev.padeliummarhaba.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch

@Composable
fun SummaryScreen() {
    var showFilterMenu by remember { mutableStateOf(false) }
    var reservations = remember { mutableStateOf(listOf(
        Reservation("SPX12345", "Mercedes", "18/09/2024", "21:30 - 23:00", "80 DT", "Confirmé", " ", true),
        //Reservation("PAR11225", "Mercedes", "18/09/2024", "21:30 - 23:00", "80 DT", "Annulée", " ", false),
        //Reservation("SPX12345", "Mercedes", "18/09/2024", "21:30 - 23:00", "80 DT", "Confirmé", " ", true)
    )) }

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
        Divider(modifier = Modifier.padding(vertical = 8.dp), thickness = 1.dp, color = Color.Gray)

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
                    DropdownMenuItem(onClick = {
                        showFilterMenu = false
                    }) {
                        Text("By Date")
                    }
                    DropdownMenuItem(onClick = {
                        showFilterMenu = false
                    }) {
                        Text("By Name")
                    }
                    DropdownMenuItem(onClick = {
                        showFilterMenu = false
                    }) {
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

        reservations.value.forEach { reservation ->
            ReservationCard(reservation)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun ReservationCard(reservation: Reservation) {
    // State for managing the visibility of the bottom sheet
    var showDialog by remember { mutableStateOf(false) }

    // Modal Bottom Sheet Layout

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,

            ) {


            }


            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = " ${reservation.space}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(7.dp))
                    Text(
                        text = " ${reservation.date} , ${reservation.time} ",
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(7.dp))



                    StatusBadge(reservation.isConfirmed)
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = " ${reservation.price}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    IconButton(onClick = {
                        showDialog = true
                    }) {
                        Icon(imageVector = Icons.Default.Visibility, contentDescription = "View")
                    }
                }
            }
        }
    }

    // Dialog displaying ReservationCard1 when showDialog is true
    if (showDialog) {
        Dialog(onDismissRequest = { showDialog = false }) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                elevation = 8.dp,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                // ReservationCard1 content inside the dialog
                ReservationCard1(reservation)
            }
        }
    }
}

@Composable
fun ReservationCard1(reservation: Reservation) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Référence N° :",
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = Color.Gray,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
                Text(
                    text = reservation.reference,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    style = TextStyle(textDecoration = TextDecoration.Underline)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Espace: ${reservation.space}",
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Prix: ${reservation.price}",
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "État:",
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    StatusBadge(reservation.isConfirmed)
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = " ${reservation.date} | ${reservation.time}",
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = " ${reservation.createdBy}",
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Share, contentDescription = "Share")
                }
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }

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


data class Reservation(
    val reference: String,
    val space: String,
    val date: String,
    val time: String,
    val price: String,
    val status: String,
    val createdBy: String,
    val isConfirmed: Boolean
)

@Preview(showBackground = true)
@Composable
fun SummaryScreenPreview() {
    SummaryScreen()
}
@Preview(showBackground = true)
@Composable
fun PreviewReservationCard1() {
    val sampleReservation = Reservation(
        reference = "12345",
        space = "Meeting Room A",
        price = "$200",
        isConfirmed = true,
        date = "2024-12-03",
        time = "10:00 AM",
        createdBy = "John Doe",
        status = "Doe"
    )
    ReservationCard1(reservation = sampleReservation)
}
