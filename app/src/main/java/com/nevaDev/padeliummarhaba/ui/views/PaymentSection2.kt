package com.nevaDev.padeliummarhaba.ui.views

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.nevaDev.padeliummarhaba.models.ReservationOption
import android.net.Uri
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payment
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.nevaDev.padeliummarhaba.viewmodels.PaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.dto.SaveBookingRequest
import com.nevadev.padeliummarhaba.R


/*
@Composable
fun PhoneNumberInput(
    phoneNumber: String,
    onValueChange: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Numéro de téléphone", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(
                modifier = Modifier.width(900.dp).padding(horizontal = 10.dp).offset(y = -10.dp),
                color = Color.Gray, thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = phoneNumber,
                    onValueChange = { newNumber ->
                        if (newNumber.all { char -> char.isDigit() } && newNumber.length <= 8) {
                            onValueChange(newNumber)
                        }
                    },
                    modifier = Modifier.offset(x = 3.dp).width(150.dp).padding(vertical = 4.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier.border(1.dp, Color.Gray, RoundedCornerShape(4.dp)).fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Phone Icon",
                                modifier = Modifier.padding(8.dp)
                            )
                            Box(modifier = Modifier.fillMaxWidth()) {
                                innerTextField()
                            }
                        }
                    }
                )
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = { /* Handle button click */ },
                    modifier = Modifier.fillMaxWidth().height(48.dp).border(1.dp, Color(0xFF0054D8), RoundedCornerShape(13.dp)),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                ) {
                    Text(text = "Modifier", color = Color(0xFF0054D8), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PaymentOptions(
    selectedParts: String,
    onPartsChange: (String) -> Unit,
    payTotal: Boolean,
    onTotalChange: (Boolean) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Je veux payer pour", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(18.dp))
            HorizontalDivider(
                modifier = Modifier.width(900.dp).padding(horizontal = 10.dp).offset(y = -10.dp),
                color = Color.Gray, thickness = 1.dp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExposedDropdownMenuBox(
                    expanded = false, // You can manage this state in the parent composable
                    onExpandedChange = { /* Handle dropdown visibility */ }
                ) {
                    TextField(
                        value = selectedParts,
                        onValueChange = { newParts -> onPartsChange(newParts) },
                        readOnly = true,
                        modifier = Modifier.width(50.dp).border(1.dp, Color.Unspecified, RoundedCornerShape(13.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = Color.White,
                            cursorColor = Color.Transparent,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        placeholder = { Text("Select parts") }
                    )
                    Text(text = "parts")
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "OU")
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Checkbox(
                            checked = payTotal,
                            onCheckedChange = { newTotal -> onTotalChange(newTotal) },
                            colors = CheckboxDefaults.colors(checkedColor = Color.Blue)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "total")
                    }
                }
            }
        }
    }
}
@Composable
fun PartnerSelection(
    partnerName: String,
    onPartnerNameChange: (String) -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Sélectionnez votre partenaire", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Switch(
                    checked = false, // You can manage this state in the parent composable
                    onCheckedChange = { /* Handle switch change */ }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(
                modifier = Modifier.width(900.dp).padding(horizontal = 10.dp).offset(y = -10.dp),
                color = Color.Gray, thickness = 1.dp
            )
            Text(text = "Votre partenaire doit avoir un compte sur PADELIUM", color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = partnerName,
                onValueChange = { newPartnerName -> onPartnerNameChange(newPartnerName) },
                label = { Text("Taper le nom de votre partenaire") },
                modifier = Modifier.fillMaxWidth().height(40.dp).border(1.dp, Color(0xFF0054D8), RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = Color.White)
            )
        }
    }
}
@Composable
fun ExtrasSelection(
    selectedRaquette: Int,
    onRaquetteChange: (Int) -> Unit,
    includeBalls: Boolean,
    onBallsChange: (Boolean) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) } // State to manage dropdown visibility
    var selectedRaquette by remember { mutableStateOf(1) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Des extras?", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Switch(
                    checked = false, // You can manage this state in the parent composable
                    onCheckedChange = { /* Handle switch change */ }
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(
                modifier = Modifier.width(900.dp).padding(horizontal = 10.dp).offset(y = -10.dp),
                color = Color.Gray, thickness = 1.dp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier.border(1.dp, Color.Unspecified, RoundedCornerShape(13.dp))
                    ) {
                        Text(text = selectedRaquette.toString())
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        (1..4).forEach { number ->
                            DropdownMenuItem(
                                onClick = {
                                    selectedRaquette = number
                                    expanded = false
                                    onRaquetteChange(number)
                                },
                                modifier = Modifier.padding(8.dp), // Example modifier
                                enabled = true, // Example enabled state
                                contentPadding = PaddingValues(8.dp), // Example padding
                                interactionSource = remember { MutableInteractionSource() }, // Example interaction source
                                content = {
                                    Text(text = number.toString()) // Use content to specify the text
                                }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Raquette")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = includeBalls,
                        onCheckedChange = { newBalls -> onBallsChange(newBalls) }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "3 New Balls")
                }
            }
        }
    }
}

@Composable
fun PaymentSection(
    selectedDate: LocalDate,
    selectedTimeSlot: String,
    selectedReservation: ReservationOption,
    onExtrasUpdate: (Int, Int, Boolean) -> Unit,
    onPayWithCardClick: () -> Unit,
) {
    var phoneNumber by remember { mutableStateOf("") }
    var selectedParts by remember { mutableStateOf("1") }
    var payTotal by remember { mutableStateOf(false) }
    var partnerName by remember { mutableStateOf("") }
    var selectedRaquette by remember { mutableStateOf(1) }
    var includeBalls by remember { mutableStateOf(false) }
    val extrasCost = (if (includeBalls) 5 else 0) + (selectedRaquette * 2)
    onExtrasUpdate(extrasCost, selectedRaquette, includeBalls)

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        PhoneNumberInput(phoneNumber, { phoneNumber = it })
        Spacer(modifier = Modifier.height(16.dp))
        PaymentOptions(selectedParts, { selectedParts = it }, payTotal, { payTotal = it })
        Spacer(modifier = Modifier.height(16.dp))
        PartnerSelection(partnerName, { partnerName = it })
        Spacer(modifier = Modifier.height(16.dp))
        ExtrasSelection(
            selectedRaquette,
            { selectedRaquette = it },
            includeBalls,
            { includeBalls = it })
        Spacer(modifier = Modifier.height(16.dp))
        // Reservation Summary and Payment Buttons
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            ReservationSummary(
                selectedDate = selectedDate,
                selectedTimeSlot = selectedTimeSlot ?: "Not selected",
                selectedReservation = selectedReservation ?: ReservationOption(
                    "Default",
                    "Not selected",
                    "100.0",
                    "Not selected"
                ),
                extrasCost = extrasCost,
                selectedRaquette = selectedRaquette.toString(),
                includeBalls = includeBalls,
                priceDetails = selectedReservation?.let {
                    val price = it.price.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0
                    val currencySymbol =
                        it.price.replace("[\\d.]".toRegex(), "") // Extract currency symbol
                    Pair(price, currencySymbol)
                } ?: Pair(0.0, "$"),
                onTotalAmountCalculated = { totalAmountSelected, currencySymbol ->
                    Log.d(
                        "TotalAmountSelected",
                        "The total amount is $totalAmountSelected $currencySymbol"
                    )
                }
            )
        }
    } }
*/