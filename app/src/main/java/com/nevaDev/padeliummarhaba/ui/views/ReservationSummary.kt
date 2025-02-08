package com.nevaDev.padeliummarhaba.ui.views

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevaDev.padeliummarhaba.models.ReservationOption
import java.time.LocalDate
import java.time.format.DateTimeFormatter
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReservationSummary2(
    selectedDate: LocalDate,
    selectedTimeSlot: String,
    selectedReservation: ReservationOption,
    extrasCost: Int,
    selectedRaquette: String,
    includeBalls: Boolean,
    priceDetails: Pair<Double, String>, // Updated parameter
    onTotalAmountCalculated: (Double, String) -> Unit // Updated callback
) {
    val (price, currencySymbol) = priceDetails

    // Calculate the total amount
    val totalAmountSelected = remember { price + extrasCost }

    // Trigger the callback with total amount and currency symbol
    onTotalAmountCalculated(totalAmountSelected, currencySymbol)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = "Détails réservation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        HorizontalDivider(
            modifier = Modifier
                .width(900.dp)
                .padding(horizontal = 10.dp)
                .offset(y = -10.dp),
            color = Color.Gray,
            thickness = 1.dp
        )

        ReservationDetailRow(label = "Espace", value = selectedReservation.name)
        ReservationDetailRow(label = "Prix", value = "$currencySymbol${String.format("%.2f", price)}")
        ReservationDetailRow(
            label = "Date",
            value = DateTimeFormatter.ofPattern("EEEE, d MMM yyyy").format(selectedDate)
        )
        ReservationDetailRow(label = "Heure", value = selectedTimeSlot)

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Détails du Prix",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ReservationDetailRow(label = "Prix de Réservation", value = "$currencySymbol${String.format("%.2f", price)}")

        val extrasDetails = buildString {
            if (includeBalls) append("3 New Balls + ")
            append("${selectedRaquette} Raquettes")
        }
        ReservationDetailRow(label = "Extras", value = extrasDetails)

        ReservationDetailRow(label = "Total", value = "$currencySymbol${String.format("%.2f", totalAmountSelected)}")
    }
}







@Composable
fun ReservationDetailRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontWeight = FontWeight.Bold)
        Text(text = value, fontWeight = FontWeight.Medium)
    }
}