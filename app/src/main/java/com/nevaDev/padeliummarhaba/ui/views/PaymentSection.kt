package com.nevaDev.padeliummarhaba.ui.views

import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nevaDev.padeliummarhaba.viewmodels.PaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.dto.SaveBookingRequest
import com.nevadev.padeliummarhaba.R

@OptIn(ExperimentalMaterialApi::class)

@Composable
fun PaymentSection1(
    selectedDate: LocalDate,
    selectedTimeSlot: String,
    selectedReservation: ReservationOption,
    onExtrasUpdate: (Int, Int, Boolean) -> Unit,
    onPayWithCardClick: () -> Unit,
    totalAmount: String,
    navController: NavController,
    viewModel : SaveBookingViewModel = hiltViewModel(),
    saveBookingRequest: List<SaveBookingRequest>,
    viewModel1 : PaymentViewModel = hiltViewModel(),
    paymentRequest: PaymentRequest,


) {
    var showPayScreen by remember { mutableStateOf(false) }

    var additionalExtrasEnabled by remember { mutableStateOf(false) }
    val selectedExtras = remember { mutableStateListOf<Triple<String, String, Int>>() } // State for selected extras

    var partnerName by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var selectedParts by remember { mutableStateOf("1") }
    val options = listOf("1", "2", "3", "4")
    var phoneNumber by remember { mutableStateOf("") }
    var payParts by remember { mutableStateOf("1") }
    var payTotal by remember { mutableStateOf(false) }
    var extrasEnabled by remember { mutableStateOf(false) }
    var selectedRaquette by remember { mutableStateOf(1) }
    var includeBalls by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val context = LocalContext.current

    val extrasCost = (if (includeBalls) 5 else 0) + (selectedRaquette * 2)
    onExtrasUpdate(extrasCost, selectedRaquette, includeBalls)

    var totalExtrasCost by remember { mutableStateOf(0.0) }
    onExtrasUpdate(totalExtrasCost.toInt(), selectedRaquette, includeBalls)

    // Recalculate the total amount when extras are updated
    val totalAmountSelected = remember(totalExtrasCost) {
        val reservationAmount = selectedReservation.price.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0
        reservationAmount + totalExtrasCost
    }

    // Update the extras whenever they change
    onExtrasUpdate(totalExtrasCost.toInt(), selectedRaquette, includeBalls)

    // Handle the addition of extras
    val onTotalAmountCalculated: (Int) -> Unit = { totalAmountSelected ->
        // Handle the total amount calculation
        Log.d("TotalAmount", "The total amount is $totalAmountSelected")
    }

    viewModel.dataResult.observe(lifecycleOwner) { result ->
        isLoading = false

        when (result) {
            is DataResult.Loading -> {
                Log.e("TAG", "Loading")
            }
            is DataResult.Success -> {
                Log.e("TAG", "Success")
            }
            is DataResult.Failure -> {
                isLoading = false
                Log.e("TAG", "Failure - Error Code: ${result.exception},${result.errorCode}, Message: ${result.errorMessage}")
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())

    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Requis pour votre réservation", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(18.dp))

                HorizontalDivider(modifier = Modifier .width(900.dp).padding(horizontal = 10.dp).offset(y = -10.dp),
                    color = Color.Gray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "    Numéro de téléphone", fontSize = 15.sp,fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(18.dp))

                HorizontalDivider(modifier = Modifier .width(900.dp).padding(horizontal = 10.dp).offset(y = -10.dp),
                    color = Color.Gray, thickness = 1.dp)
                // Phone number input with pre-filled number and "Modifier" button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BasicTextField(
                        value = phoneNumber,
                        onValueChange = {
                            if (it.all { char -> char.isDigit() } && it.length <= 8) {
                                phoneNumber = it

                            }
                        },
                        modifier = Modifier
                            .offset(x = 3.dp)
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
                                // Phone Icon
                                Icon(
                                    imageVector = Icons.Default.Phone,
                                    contentDescription = "Phone Icon",
                                    modifier = Modifier.padding(8.dp)
                                )
                                androidx.compose.material.Text(
                                    text = "|",
                                    fontSize = 29.sp,
                                    color = Color.Black,
                                    modifier = Modifier.offset(x = -8.dp, y = -2.dp)
                                )
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    innerTextField()
                                }


                            }
                        }
                    )

                    Spacer(modifier = Modifier.width(10.dp))


                    Button(
                        onClick = {
                        },
                        modifier = Modifier
                            .offset(x=28.dp)
                            .width(120.dp)
                            .height(48.dp)
                            .border(1.dp, Color(0xFF0054D8), RoundedCornerShape(13.dp)),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.White, RoundedCornerShape(10.dp))
                                .fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Modifier",
                                color = Color(0xFF0054D8),
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp
                            )
                        }
                    }

                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Je veux payer pour",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.width(100.dp))

                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded }
                    ) {
                        TextField(
                            value = selectedParts,
                            onValueChange = { selectedParts = it },
                            readOnly = true,
                            modifier = Modifier
                                .widthIn(min = 30.dp).width(50.dp)
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
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            options.forEach { option ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedParts = option
                                        expanded = false
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(20.dp)
                                        .padding(horizontal = 2.dp)
                                ) {
                                    Text(
                                        text = option,
                                        fontSize = 16.sp,
                                        modifier = Modifier.align(Alignment.CenterVertically)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = "Parts",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                    HorizontalDivider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp),
                        color = Color.Gray,
                        thickness = 1.dp
                    )

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            var extrasEnabled by remember { mutableStateOf(false) }
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "    Sélectionnez votre partenaire",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Switch(
                        checked = extrasEnabled,
                        onCheckedChange = { extrasEnabled = it }, // Toggle the extrasEnabled state
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
                        .width(900.dp)
                        .padding(horizontal = 10.dp)
                        .offset(y = -10.dp),
                    color = Color.Gray,
                    thickness = 1.dp
                )

                if (extrasEnabled) { // Conditional rendering based on the switch state
                    Column {
                        Text(
                            text = "Votre partenaire doit avoir un compte sur PADELIUM",
                            color = Color.Gray,
                            fontSize = 16.sp,
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = partnerName,
                            onValueChange = { partnerName = it },
                            label = {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.CenterStart // Align placeholder text to the center vertically.
                                ) {
                                    Text("Taper le nom de votre partenaire")
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp), // Slightly increase height for better alignment.
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                backgroundColor = Color.White
                            ),
                            shape = RoundedCornerShape(15.dp)
                        )
                    }
                }
            } }

        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp), // Added padding to separate the card from the screen edges
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(6.dp) // Slightly increased elevation for better visual separation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Title and Switch
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

                // Horizontal Divider
                HorizontalDivider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    color = Color.Gray,
                    thickness = 1.dp
                )

                // Extras Section
                if (additionalExtrasEnabled) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Article(s) réserver à mon usage",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    var extrasCost by remember { mutableStateOf(0.0) } // State for total cost of extras

                    // Extras List
                    val extras: List<Triple<String, String, Int>> = listOf(
                        Triple("Une raquette", "5 DT", R.drawable.raquettebl),
                        Triple("Eau", "5 DT", R.drawable.eau),
                        Triple("EXTRA", "10 DT", R.drawable.star)
                    )

                    extras.forEachIndexed { index, (name, price, iconRes) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (index % 2 == 0) Color(0xFFF8F8F8) else Color.White) // Alternating row colors
                                .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = iconRes),
                                    contentDescription = name,
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFF0F0F0))
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(text = name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    Text(text = price, fontSize = 14.sp, color = Color.Gray)
                                }
                            }
                            IconButton(onClick = {
                                selectedExtras.add(Triple(name, price, iconRes))
                                val extraPrice = price.replace("[^\\d.]".toRegex(), "").toDoubleOrNull() ?: 0.0
                                totalExtrasCost += extraPrice
                            })
                            {
                                Icon(
                                    painter = painterResource(id = R.drawable.plus),
                                    contentDescription = "Add",
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .background(Color(0xFF0054D8), shape = CircleShape)
                                        .padding(8.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }


        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            ReservationSummary(
                selectedDate = selectedDate,
                selectedTimeSlot = selectedTimeSlot ?: "Not selected",
                selectedReservation = selectedReservation ?: ReservationOption("Default", "Not selected", "100.0", "Not selected"),
                extrasCost = extrasCost,
                selectedExtras = selectedExtras, // Pass the entire list of extras
                selectedRaquette = selectedRaquette.toString(),
                includeBalls = includeBalls,
                amountSelected = selectedReservation?.price?.replace("[^\\d.]".toRegex(), "")?.toDoubleOrNull() ?: 0.0,
                onTotalAmountCalculated = { totalAmountSelected ->
                    Log.d("TotalAmountSelected", "The total amount is $totalAmountSelected")
                }
            )
            val displayedPrice = "${selectedReservation?.price?.replace("[^\\d.]".toRegex(), "")?.toDoubleOrNull() ?: 0.0} DT"



        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth().offset(x=-10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    onPayWithCardClick()
                    Log.e("TAG", "onclick")

                    var isLoading = true

                    viewModel.SaveBooking(saveBookingRequest)

                    viewModel.dataResult.observe(lifecycleOwner) { result ->
                        when (result) {
                            is DataResult.Loading -> {
                                isLoading = true

                            }
                            is DataResult.Success -> {
                                Log.d("SaveBooking", "SaveBooking successful: ${result.data}")  // Access data from Success

                                isLoading = false


                                viewModel1.Payment(paymentRequest)

                                viewModel1.dataResult.observe(lifecycleOwner) { paymentResult ->
                                    when (paymentResult) {
                                        is DataResult.Loading -> {
                                            isLoading = true

                                        }
                                        is DataResult.Success -> {
                                            Log.d("Payment", "Payment successful: ${paymentResult.data}") // Log the success

                                            isLoading = false

                                            val paymentUrl = "https://test.clictopay.com/payment/merchants/CLICTOPAY/payment_fr.html?mdOrder=dd79368f-6e10-71a4-8641-4ce60008f1b8"
                                            navController.navigate("WebViewScreen?paymentUrl=${Uri.encode(paymentUrl)}")
                                        }
                                        is DataResult.Failure -> {
                                            Log.e("Payment", "Payment failed: ${paymentResult.errorMessage}") // Log the failure

                                            isLoading = false

                                            Toast.makeText(
                                                context,
                                                "Payment failed: ${paymentResult.errorMessage}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                            }
                            is DataResult.Failure -> {
                                isLoading = false


                                Toast.makeText(
                                    context,
                                    "Booking failed: ${result.errorMessage}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                border = BorderStroke(2.dp, Color(0xFF0054D8))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Payment,
                        contentDescription = "Card Payment",
                        tint = Color(0xFF0054D8)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Card Crédit",
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }

            Button(
                onClick = {  },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                border = BorderStroke(2.dp, Color(0xFF0054D8))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Money,
                        contentDescription = "Credits Payment",
                        tint = Color(0xFF0054D8)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Crédit Padelium",
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
@Composable
fun WebViewScreen(paymentUrl: String, navController: NavController) {
    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { context ->
                WebView(context).apply {
                    settings.javaScriptEnabled = true
                    loadUrl(paymentUrl)
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close",
                tint = Color.Black
            )
        }
    }
}

fun calculateTotalAmount(): Int {
    // Your logic to calculate the total amount based on selected options
    return 100 // Example static value, replace this with actual calculation
}


