package com.nevaDev.padeliummarhaba.ui.views

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.nevaDev.padeliummarhaba.viewmodels.PaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.dto.SaveBookingRequest

@Composable
fun PayScreen(totalAmount: String,
              navController: NavController,
              onPayWithCardClick: () -> Unit,
              viewModel : SaveBookingViewModel = hiltViewModel(),
              saveBookingRequest: List<SaveBookingRequest>,
              viewModel1 : PaymentViewModel = hiltViewModel(),
              paymentRequest: PaymentRequest
)
{
    var selectedTab by remember { mutableStateOf(0) }
    var cardNumber by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var cardholderName by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }
    var isEmailChecked by remember { mutableStateOf(true) }
    var optionalMessage by remember { mutableStateOf("") }
    val saveBookingViewModel: SaveBookingViewModel = hiltViewModel()
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val scrollState = rememberScrollState()
    val paymentResult by viewModel1.dataResult.observeAsState()
    val saveBookingResult by viewModel.dataResult.observeAsState()


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
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        TabRow(
            selectedTabIndex = selectedTab,
            backgroundColor = Color.Transparent,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab])
                        .height(4.dp),
                    color = Color(0xFF0054D8)
                )
            }
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, if (selectedTab == 0) Color(0xFF0054D8) else Color.Transparent, RoundedCornerShape(8.dp))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.a123),
                        contentDescription = "Card Crédit Icon",
                        modifier = Modifier.size(20.dp),
                        tint = if (selectedTab == 0) Color.Unspecified else Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Card Crédit",
                        modifier = Modifier.padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == 0) Color(0xFF0054D8) else Color.Gray
                    )
                }
            }

            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .border(1.dp, if (selectedTab == 1) Color(0xFF0054D8) else Color.Transparent, RoundedCornerShape(8.dp))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.a123),
                        contentDescription = "Crédit Padelium Icon",
                        modifier = Modifier.size(20.dp),
                        tint = if (selectedTab == 1) Color.Unspecified else Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Crédit Padelium",
                        modifier = Modifier.padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == 1) Color(0xFF0054D8) else Color.Gray
                    )
                }
            }
        }




        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("Numéro de la carte") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = month,
                onValueChange = { month = it },
                label = { Text("Mois") },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(24.dp)
            )
            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Année") },
                modifier = Modifier.weight(1f),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            label = { Text("Code de sûreté") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(24.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = cardholderName,
            onValueChange = { cardholderName = it },
            label = { Text("Le nom du détenteur") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = isEmailChecked,
                onCheckedChange = { isEmailChecked = it },
                colors = CheckboxDefaults.colors(checkedColor = Color(0xFFD7F057))
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Adresse e-mail", fontSize = 16.sp)
        }

        OutlinedTextField(
            value = emailAddress,
            onValueChange = { emailAddress = it },
            label = { Text("Taper votre adresse e-mail") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                onPayWithCardClick() // Trigger any custom logic passed for payment with card

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
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0054D8)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = "Paiement $totalAmount DT",
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = optionalMessage,
            onValueChange = { optionalMessage = it },
            label = { Text("Taper votre message (optionnel)") },
            modifier = Modifier
                .fillMaxWidth()
                .size(250.dp),
            shape = RoundedCornerShape(24.dp)
        )
    }}


/*
@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    PayScreen(totalAmount = "100", navController = rememberNavController(),
        onPayWithCardClick = {
            // Handle pay with card click in the preview
        },)
}
*/
