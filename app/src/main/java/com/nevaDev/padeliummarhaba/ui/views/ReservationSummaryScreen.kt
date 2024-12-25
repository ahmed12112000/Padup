package com.nevaDev.padeliummarhaba.ui.views

import android.util.Log
import androidx.compose.foundation.layout.Column
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
import androidx.navigation.NavController
import com.nevaDev.padeliummarhaba.models.ReservationOption
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun ReservationSummary2(
    selectedDate: LocalDate,
    selectedTimeSlot: String,
    selectedReservation: ReservationOption,
    extrasCost: Int,
    selectedExtras: List<Triple<String, String, Int>>,
    selectedRaquette: String,
    includeBalls: Boolean,
    amountSelected: Pair<Double, String>?, // Amount and currency
    establishmentName: String,
    onTotalAmountCalculated: (Double, String) -> Unit,
) {
    val baseAmount = amountSelected?.first ?: 0.0
    val currencySymbol = amountSelected?.second ?: "DT"

    val totalExtrasCost = selectedExtras.sumOf { (_, priceString, _) ->
        priceString.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0
    }

    val totalAmount = remember(baseAmount, totalExtrasCost) { baseAmount + totalExtrasCost }
    onTotalAmountCalculated(totalAmount, currencySymbol)

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Détails réservation", fontSize = 20.sp, fontWeight = FontWeight.Bold)
        HorizontalDivider(Modifier.padding(vertical = 8.dp))

        ReservationDetailRow(label = "Espace", value = selectedReservation.name)
        ReservationDetailRow("Prix", "${selectedTimeSlot} ")
        ReservationDetailRow("Date", selectedDate.format(DateTimeFormatter.ofPattern("EEEE, d MMM yyyy")))
        ReservationDetailRow("Heure", currencySymbol)

        Spacer(Modifier.height(16.dp))
        Text("Détails du Prix", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        ReservationDetailRow("Prix de Réservation", "${currencySymbol} ${String.format("%.2f", baseAmount)}")

        // Display extras
        val extrasSummary = selectedExtras.joinToString(", ") { (name, price, _) -> "$name ($price)" }
        ReservationDetailRow("Extras", if (extrasSummary.isEmpty()) "0.0" else extrasSummary)

        ReservationDetailRow("Total", "${currencySymbol} ${String.format("%.2f", totalAmount)}")

        // Call onTotalAmountCalculated again to pass the total amount and currency symbol
    }
}