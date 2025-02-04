package com.nevaDev.padeliummarhaba.ui.views

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nevaDev.padeliummarhaba.models.ReservationOption
import com.nevaDev.padeliummarhaba.viewmodels.FindTermsViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PartnerPayViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PrivateExtrasViewModel
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PrivateExtrasResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale


@Composable
fun PartnerPaymentScreen(
    navController: NavController,
    viewModel4: PartnerPayViewModel = hiltViewModel(),

    ) {
    var isSupplementChecked by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("") }
    var additionalExtrasEnabled by remember { mutableStateOf(false) }
    var showExtrasSection by remember { mutableStateOf(false) } // State to track visibility
    var isLoading by remember { mutableStateOf(false) }
    var selectedExtras by remember { mutableStateOf<List<Triple<String, String, Int>>>(emptyList()) }
    var totalExtrasCost by remember { mutableStateOf(0.0) } // State to hold total cost of extras
    val time = remember { mutableStateOf<String?>(null) }
    var adjustedAmount by remember { mutableStateOf(0.0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        androidx.compose.material3.Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

                    ExtrasSection2(
                        onExtrasUpdate = { extras, cost ->
                            selectedExtras = extras
                            totalExtrasCost = cost
                        },
                        selectedParts = "1"
                    )
                }
        val selectedBookings by viewModel4.selectedBookings.observeAsState(emptyList())

        if (selectedBookings.isEmpty()) {
            Text("Loading reservation details...", fontSize = 20.sp)
        } else {
            androidx.compose.material3.Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(Color.White),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                ReservationSummary2(viewModel4 = viewModel4)
            }
        }



            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = null)
                Text(
                    text = "Acheter des crédits.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Updated Row for buttons to be in the same line
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), // optional padding for alignment
                    horizontalArrangement = Arrangement.spacedBy(
                        16.dp,
                        Alignment.CenterHorizontally
                    ) // Space between the buttons
                ) {
                    Button(
                        onClick = { /* Handle Payment */ },
                        enabled = !isLoading,
                        modifier = Modifier
                            .offset(x = -15.dp)
                            .height(48.dp)
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
                            androidx.compose.material3.Text(
                                text = "Carte Crédit",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    Button(
                        onClick = { /* Handle insufficient balance */ },
                        modifier = Modifier
                            // .offset(x= -25.dp)
                            .height(48.dp),
                        // .weight(1f), // Ensure both buttons take equal space
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
                            androidx.compose.material3.Text(
                                text = "Crédit Padelium",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }


        }
}



@Composable
fun ExtrasSection2(
    onExtrasUpdate: (List<Triple<String, String, Int>>, Double) -> Unit,
    viewModel4: PrivateExtrasViewModel = hiltViewModel(),
    selectedParts: String,
    findTermsViewModel: FindTermsViewModel = hiltViewModel(),
) {
    var additionalExtrasEnabled by remember { mutableStateOf(false) }
    val privateExtrasState by viewModel4.extrasState2.observeAsState()
    var selectedExtras by remember { mutableStateOf<List<Triple<String, String, Int>>>(emptyList()) }

    val sharedList = remember { mutableStateOf<MutableList<Long>>(mutableListOf()) }
    val privateList = remember { mutableStateOf<MutableList<Long>>(mutableListOf()) }

    LaunchedEffect( viewModel4) {
        viewModel4.PrivateExtras()
    }

    // Calculate totalExtrasCost dynamically based on selectedExtras
    val totalExtrasCost = selectedExtras.sumOf { it.second.toDouble() }

    // Notify ReservationSummary of the total amount

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = (-8).dp), // Reduced offset for tighter spacing
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        androidx.compose.material3.Text(
            text = "  Je commande des extras ?",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.weight(1f)
        )
        Switch(
            checked = additionalExtrasEnabled,
            onCheckedChange = { additionalExtrasEnabled = it },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color(0xFF0054D8),
                uncheckedThumbColor = Color.Gray,
                checkedTrackColor = Color(0xFF0054D8).copy(alpha = 0.5f),
                uncheckedTrackColor = Color.LightGray
            )
        )
    }

    if (additionalExtrasEnabled) {
        Spacer(modifier = Modifier.height(2.dp)) // Reduced height for tighter spacing

        when {
             privateExtrasState is DataResult.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

             privateExtrasState is DataResult.Success -> {
                val privateExtrasList =
                    (privateExtrasState as DataResult.Success).data as? List<PrivateExtrasResponse>

                // Reduced height for tighter spacing between cards
                Spacer(modifier = Modifier.height(1.dp))

                // Render private extras
                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) { // Reduced spacing between items

                    androidx.compose.material3.Text(
                        text = "  Article(s) réserver à mon usage",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    privateExtrasList?.forEach { privateExtra ->
                        val isPrivateExtraAdded =
                            selectedExtras.any { it.first == privateExtra.name }
                        ExtraItemCard(
                            extra = privateExtra,
                            isAdded = isPrivateExtraAdded,
                            onAddClick = { extraPrice ->
                                privateList.value.add(privateExtra.id)
                                Log.d(
                                    "ExtrasSection",
                                    "Updated privateList: ${privateList.value}"
                                )
                                findTermsViewModel.updatePrivateExtras(privateList.value)

                                selectedExtras += Triple(
                                    privateExtra.name,
                                    privateExtra.amount.toString(),
                                    privateExtra.currencyId.toInt()
                                )
                                onExtrasUpdate(selectedExtras, totalExtrasCost)
                            },
                            onRemoveClick = { extraPrice ->
                                privateList.value.remove(privateExtra.id)
                                Log.d(
                                    "ExtrasSection",
                                    "Updated privateList: ${privateList.value}"
                                )
                                findTermsViewModel.updatePrivateExtras(privateList.value)

                                selectedExtras =
                                    selectedExtras.filterNot { it.first == privateExtra.name }
                                onExtrasUpdate(selectedExtras, totalExtrasCost)
                            }
                        )

                    }
                    // Reduced height between cards
                    Spacer(modifier = Modifier.height(4.dp))

                }
            }
            else -> {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    androidx.compose.material3.Text(
                        text = "Failed to load extras",
                        color = Color.Red
                    )

                }
            }
        }
    }

}
@Composable
fun ReservationSummary2(
    viewModel4: PartnerPayViewModel = hiltViewModel(),
) {
    val selectedBookings by viewModel4.selectedBookings.observeAsState(emptyList())

    val booking = selectedBookings.firstOrNull()

    if (booking == null) {
        // Show loading or empty state if no booking is available yet
        return Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            Text("No reservation details available.", fontSize = 20.sp)
        }
    }

    // Display reservation details
    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        androidx.compose.material3.Text(
            text = "Détails Réservation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        ReservationDetailRow(label = "Espace", value = booking.bookingEstablishmentName)
        ReservationDetailRow(label = "Heure", value = booking.bookingDateStr)
        ReservationDetailRow(label = "Prix", value = booking.amount.toString())
        ReservationDetailRow(
            label = "Réservé par",
            value = "${booking.bookingCreatedFirstName} ${booking.bookingCreatedLastName}"
        )

        Spacer(modifier = Modifier.height(16.dp))

        androidx.compose.material3.Text(
            text = "Détails du Prix",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        ReservationDetailRow(label = "Prix de Réservation", value = "${booking.amount} DT")
        ReservationDetailRow(label = "Total", value = "${booking.amount} DT")
    }
}
