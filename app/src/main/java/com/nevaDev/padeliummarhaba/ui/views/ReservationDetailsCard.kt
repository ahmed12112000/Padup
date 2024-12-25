package com.nevaDev.padeliummarhaba.ui.views


import android.util.Log
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.rememberImagePainter
import com.nevaDev.padeliummarhaba.models.ReservationOption
import com.nevaDev.padeliummarhaba.viewmodels.ExtrasViewModel

import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.ExtrasResponse
import java.math.BigDecimal

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ReservationDetailsCard(
    selectedExtras: MutableList<Triple<String, String, Int>>,
    onExtrasUpdate: (List<Triple<String, String, Int>>, Double) -> Unit,
    selectedReservation: ReservationOption,
    viewModel2: ExtrasViewModel = hiltViewModel(),
) {
    var partnerName by remember { mutableStateOf("") }
    var selectedParts by remember { mutableStateOf("1") }
    var extrasEnabled by remember { mutableStateOf(false) }
    var includeBalls by remember { mutableStateOf(false) }
    var selectedRaquette by remember { mutableStateOf(1) }
    var totalExtrasCost by remember { mutableStateOf(0.0) } // Track total cost of extras

    // Fetch extra data
    val extrasState by viewModel2.extrasState.observeAsState()

    LaunchedEffect(viewModel2) {
        viewModel2.Extras() // Triggering the data fetch for extras
    }

    // Calculate total reservation amount including extras
    val totalAmountSelected = remember(totalExtrasCost) {
        val reservationAmount = selectedReservation.price.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0
        reservationAmount + totalExtrasCost
    }

    Column {
        // Card for selecting parts
        ReservationCard(title = "Je veux payer pour") {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(text = "Je veux payer pour", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(100.dp))
                ExposedDropdownMenuBox(
                    expanded = extrasEnabled,
                    onExpandedChange = { extrasEnabled = !extrasEnabled }
                ) {
                    TextField(
                        value = selectedParts,
                        onValueChange = { selectedParts = it },
                        readOnly = true,
                        modifier = Modifier
                            .widthIn(min = 30.dp)
                            .width(50.dp)
                            .border(1.dp, Color.Gray, RoundedCornerShape(13.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            cursorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        placeholder = { Text("Select parts") }
                    )
                    ExposedDropdownMenu(
                        expanded = extrasEnabled,
                        onDismissRequest = { extrasEnabled = false }
                    ) {
                        listOf("1", "2", "3", "4").forEach { option ->
                            DropdownMenuItem(onClick = {
                                selectedParts = option
                                extrasEnabled = false
                            }) {
                                Text(text = option)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = "Parts", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Card for partner selection
        ReservationCard(title = "Sélectionnez votre partenaire") {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Sélectionnez votre partenaire", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Switch(
                    checked = extrasEnabled,
                    onCheckedChange = { extrasEnabled = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFF0054D8),
                        uncheckedThumbColor = Color.Gray,
                        checkedTrackColor = Color(0xFF0054D8).copy(alpha = 0.5f),
                        uncheckedTrackColor = Color.LightGray
                    )
                )
            }
            if (extrasEnabled) {
                Text("Votre partenaire doit avoir un compte sur PADELIUM", fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = partnerName,
                    onValueChange = { partnerName = it },
                    label = { Text("Taper le nom de votre partenaire") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(15.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Call ExtrasSection
        ExtrasSection(
            onExtrasUpdate = { updatedExtras, updatedTotalCost ->
                selectedExtras.clear()
                selectedExtras.addAll(updatedExtras)
                totalExtrasCost = updatedTotalCost

                // Update parent with new extras and total cost
                onExtrasUpdate(selectedExtras, totalExtrasCost)
            }
        )
    }
}

@Composable
fun ExtrasSection(
    onExtrasUpdate: (List<Triple<String, String, Int>>, Double) -> Unit,
    viewModel2: ExtrasViewModel = hiltViewModel()
) {
    var additionalExtrasEnabled by remember { mutableStateOf(false) }
    var totalExtrasCost by remember { mutableStateOf(0.0) }
    val extrasState by viewModel2.extrasState.observeAsState()
    var selectedExtras by remember { mutableStateOf<List<Triple<String, String, Int>>>(emptyList()) }

    LaunchedEffect(viewModel2) {
        viewModel2.Extras() // Trigger the data fetch for extras
    }

    ReservationCard(
        title = "Je commande des extras?",
        content = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Je commande des extras?",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
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

            Spacer(modifier = Modifier.height(12.dp))

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                color = Color.Gray,
                thickness = 1.dp
            )

            if (additionalExtrasEnabled) {
                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Article(s) réserver à mon usage",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                when (val state = extrasState) {
                    is DataResult.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(), // Fill the available space
                            contentAlignment = Alignment.Center // Center content
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is DataResult.Success -> {
                        val extrasList = state.data as? List<ExtrasResponse>
                        extrasList?.forEach { extra ->
                            val isExtraAdded = selectedExtras.any { it.first == extra.name } // Check if extra is selected

                            ExtraItemCard(
                                extra = extra,
                                isAdded = isExtraAdded,
                                onAddClick = { extraPrice ->
                                    totalExtrasCost += extraPrice

                                    // Add the selected extra to the list
                                    selectedExtras = selectedExtras + Triple(
                                        extra.name,
                                        extra.amount.toString(),
                                        extra.currencyId.toInt()
                                    )

                                    // Pass updated state to the parent
                                    onExtrasUpdate(selectedExtras, totalExtrasCost)
                                },
                                onRemoveClick = { extraPrice ->
                                    totalExtrasCost -= extraPrice

                                    // Remove the extra from the selected list
                                    selectedExtras = selectedExtras.filterNot { it.first == extra.name }

                                    // Pass updated state to the parent
                                    onExtrasUpdate(selectedExtras, totalExtrasCost)
                                }
                            )
                        }
                    }
                    is DataResult.Failure -> {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Failed to load extras: ${state.errorMessage}",
                                color = Color.Red
                            )
                        }
                    }
                    else -> {}
                }
            }
        }
    )
}



@Composable
fun ReservationCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(88.dp))
            content()
        }
    }
}

@Composable
fun ExtraItemCard(
    extra: ExtrasResponse,
    isAdded: Boolean,
    onAddClick: (Double) -> Unit,
    onRemoveClick: (Double) -> Unit
) {
    var addedState by remember { mutableStateOf(isAdded) }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    ) {
        val painter = rememberImagePainter(
            data = extra.picture,
            builder = {
                error(R.drawable.star)
            }
        )

        Image(
            painter = painter,
            contentDescription = extra.name,
            modifier = Modifier.size(35.dp)
        )
        Column(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(1f)
        ) {
            Text(extra.name, fontWeight = FontWeight.Bold)
            Text("Amount: ${extra.amount} ${extra.currencyName}")
        }

        IconButton(onClick = {
            val extraPrice = extra.amount.toDouble() // Convert to Double
            if (addedState) {
                onRemoveClick(extraPrice) // Remove the extra
            } else {
                onAddClick(extraPrice) // Add the extra
            }
            addedState = !addedState // Toggle the state
        }) {
            Icon(
                painter = painterResource(
                    id = if (addedState) R.drawable.moins else R.drawable.plus
                ),
                contentDescription = if (addedState) "Remove" else "Add",
                tint = Color.Unspecified,
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = if (addedState) Color.White else Color(0xFF0054D8),
                        shape = CircleShape
                    )
                    .padding(8.dp)
            )
        }
    }
}





