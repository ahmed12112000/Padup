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

@Composable
fun SummaryScreen() {
    var showFilterMenu by remember { mutableStateOf(false) }
    var reservations = remember { mutableStateOf(listOf(
        Reservation("SPX12345", "Mercedes", "18/09/2024", "21:30 - 23:00", "80 DT", "Confirmé", "RAHMOUNI Omar", true),
        Reservation("PAR11225", "Mercedes", "18/09/2024", "21:30 - 23:00", "80 DT", "Annulée", "RAHMOUNI Omar", false),
        Reservation("SPX12345", "Mercedes", "18/09/2024", "21:30 - 23:00", "80 DT", "Confirmé", "RAHMOUNI Omar", true)
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

        // Filter and Sort Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .border(width = 1.dp, color = Color.LightGray, shape = RoundedCornerShape(16.dp)) // Rounded border
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Filter Button with Dropdown
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
                        // Add filter logic here (e.g., filter by date)
                        showFilterMenu = false
                    }) {
                        Text("By Date")
                    }
                    DropdownMenuItem(onClick = {
                        // Add filter logic here (e.g., filter by name)
                        showFilterMenu = false
                    }) {
                        Text("By Name")
                    }
                    DropdownMenuItem(onClick = {
                        // Add your own filtering option
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

            // Sort Button
            OutlinedButton(onClick = {
                reservations.value = reservations.value.reversed() // Reverse the reservation list
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

        // Display the reservation list
        reservations.value.forEach { reservation ->
            ReservationCard(reservation)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}


@Composable
fun ReservationCard(reservation: Reservation) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp)) // Adding a border
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Reference Number Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,

            ) {
                Text(
                    text = "Référence N° :",
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    color = Color.Gray,
                    style = TextStyle(textDecoration = TextDecoration.Underline) // Underline the text

                )
                Text(
                    text = reservation.reference,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp,
                    style = TextStyle(textDecoration = TextDecoration.Underline) // Underline the text

                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Space, Price, Date, and Creator Information
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
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

                    StatusBadge(reservation.isConfirmed) // Badge with status
                }

                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Date & Heure: ${reservation.date} | ${reservation.time}",
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Créé par: ${reservation.createdBy}",
                        fontWeight = FontWeight.Normal,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Action Icons Row
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
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(imageVector = Icons.Default.Visibility, contentDescription = "View") // Example Icon for View
                }
            }
        }
    }
}


@Composable
fun StatusBadge(isConfirmed: Boolean) {
    val backgroundColor = if (isConfirmed) Color(0xFF4CAF50) else Color(0xFFF44336) // Match green and red colors
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
