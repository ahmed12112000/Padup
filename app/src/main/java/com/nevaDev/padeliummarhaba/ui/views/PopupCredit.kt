package com.nevaDev.padeliummarhaba.ui.views

import android.util.Log
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nevaDev.padeliummarhaba.models.ReservationOption
import com.nevaDev.padeliummarhaba.viewmodels.BalanceViewModel
import com.nevaDev.padeliummarhaba.viewmodels.ConfirmBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.ErrorCreditViewModel
import com.nevaDev.padeliummarhaba.viewmodels.FindTermsViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPayAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.BalanceResponse
import com.padelium.domain.dto.ConfirmBookingRequest
import com.padelium.domain.dto.CreditErrorRequest
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.GetProfileResponse
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.dto.SaveBookingResponse
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun PopupCredit(
    onPayClick: () -> Unit,
    onCancelClick: () -> Unit,
    viewModel: GetProfileViewModel = hiltViewModel(),
    navController: NavController,
    errorCreditViewModel: ErrorCreditViewModel = hiltViewModel(),
    onTotalAmountCalculated: (Double, String) -> Unit,
    adjustedAmount: Double,
    totalExtrasCost: Double,
    showPopup: Boolean,
    onDismiss: () -> Unit,
    mappedBookingsJson: String,
    viewModel9: SharedViewModel,
    findTermsViewModel: FindTermsViewModel = hiltViewModel(),
    selectedDate: LocalDate,
    selectedReservation: ReservationOption,
    saveBookingViewModel: SaveBookingViewModel = hiltViewModel(),
    bookingId: String?,
) {
    if (!showPopup) return
    val confirmBookingViewModel: ConfirmBookingViewModel = hiltViewModel()
    val type = object : TypeToken<List<GetBookingResponse>>() {}.type
    val selectedParts by viewModel9.selectedParts.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    val paymentPayAvoirViewModel: PaymentPayAvoirViewModel = hiltViewModel()
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val paymentViewModel: PaymentViewModel = hiltViewModel()

    val mappedBookings: List<GetBookingResponse> = Gson().fromJson(mappedBookingsJson, type)
    val selectedBooking = mappedBookings.firstOrNull()

    val profileData by viewModel.profileData.observeAsState(DataResult.Loading)
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf(BigDecimal.ZERO) }

    val balanceViewModel: BalanceViewModel = hiltViewModel()
    val balanceData by balanceViewModel.dataResult.observeAsState(DataResult.Loading)
    val saveBookingResult by saveBookingViewModel.dataResult.observeAsState(DataResult.Loading)

    val totalAmountSelected = adjustedAmount + totalExtrasCost
    onTotalAmountCalculated(totalAmountSelected, "DT")
    var bookingId1 by remember { mutableStateOf<Long?>(null) }
    LaunchedEffect(bookingId) {
        bookingId?.toLongOrNull()?.let { id ->
            Log.d("PopupCredit", "Received booking ID: $id")
        }
    }
    var elapsedTime by remember { mutableStateOf(0f) }
    val totalTime = 240f
    val timeLeft = (totalTime - elapsedTime).toInt() // Calculate time left dynamically
    val progress = timeLeft / 180f

    val animatedColor by animateColorAsState(
        targetValue = when {
            timeLeft > 60 -> Color(0xFF4CAF50) // Green
            timeLeft > 30 -> Color(0xFFFFC107) // Yellow
            else -> Color(0xFFF44336) // Red
        },
        animationSpec = tween(durationMillis = 500)
    )

    val animatedProgress by animateFloatAsState(
        targetValue = elapsedTime / totalTime, // Progress grows from 0 to 1
        animationSpec = tween(durationMillis = 500)
    )

    val creditErrorRequest = bookingId?.toLongOrNull()?.let { id ->
        CreditErrorRequest(
            amount = BigDecimal.ZERO,
            bookingIds = listOf(id),
            buyerId = 0L,
            payFromAvoir = false,
            status = true,
            token = "",
            transactionId = 0L
        )
    } ?: CreditErrorRequest(
        amount = BigDecimal.ZERO,
        bookingIds = emptyList(), // Default empty list
        buyerId = 0L,
        payFromAvoir = false,
        status = false,
        token = "",
        transactionId = 0L
    )
    LaunchedEffect(bookingId) {
        bookingId?.toLongOrNull()?.let { id ->
            viewModel.fetchProfileData()
            balanceViewModel.fetchAndBalance()

            while (elapsedTime < totalTime) {
                delay(1000)
                elapsedTime += 1f
            }

            Log.d("POPUP_CREDIT", "Calling ErrorCredit with bookingId: $id")
            val request = CreditErrorRequest(
                amount = BigDecimal.ZERO,
                bookingIds = listOf(id),
                buyerId = 0L,
                payFromAvoir = false,
                status = true,
                token = "",
                transactionId = 0L
            )
            errorCreditViewModel.ErrorCredit(request)
            onDismiss()
        }
    }

    when (val result = profileData) {
        is DataResult.Success -> {
            val profile = result.data as? GetProfileResponse
            if (profile != null) {
                firstName = profile.firstName
                lastName = profile.lastName
            }
        }
        is DataResult.Failure -> Log.e("ProfileError", "Error fetching profile data")
        else -> Unit
    }

    when (val result = balanceData) {
        is DataResult.Success -> balance = result.data as? BigDecimal ?: BigDecimal.ZERO
        is DataResult.Failure -> Log.e("BalanceError", "Error fetching balance: ${result.errorMessage}")
        else -> Unit
    }


    Dialog(
        onDismissRequest = {
            if (creditErrorRequest != null) {
                errorCreditViewModel.ErrorCredit(creditErrorRequest)
            }
            onDismiss()
        },
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.BottomCenter // Align popup from bottom
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.65f) // Take half the screen
                    .padding(16.dp)
                    .animateContentSize(),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp), // Rounded top corners
                elevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.TopEnd
                    ) {
                        IconButton(
                            onClick = {
                                if (creditErrorRequest != null) {
                                    errorCreditViewModel.ErrorCredit(creditErrorRequest)
                                }
                                onDismiss()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.Red
                            )
                        }
                    }

                    Text(
                        text = "Paiement - Padelium Marhaba",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "$firstName $lastName", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Votre solde: $balance Crédits",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    val coroutineScope = rememberCoroutineScope()
                    val selectedPlayers by findTermsViewModel.selectedPlayers.observeAsState(initial = mutableListOf())

                    Button(
                        onClick = {
                            if (isLoading) return@Button

                            viewModel9.updateSelectedParts(selectedParts)

                            coroutineScope.launch {
                                viewModel9.selectedParts.collectLatest { currentSelectedParts ->
                                    val selectedBooking = mappedBookings.firstOrNull()

                                    if (selectedBooking != null) {


                                        val totalAmountSelected = adjustedAmount + totalExtrasCost

                                        if (totalAmountSelected <= 0) {
                                            isLoading = false
                                            return@collectLatest
                                        }

                                        val playerIds = selectedPlayers.toList()
                                        val totalAmountBigDecimal =
                                            BigDecimal.valueOf(totalAmountSelected)
                                                .setScale(0, RoundingMode.DOWN)
                                        val currency =
                                            selectedReservation.price.takeWhile { !it.isDigit() && it != '.' }
                                        onTotalAmountCalculated(
                                            totalAmountBigDecimal.toInt().toDouble(), currency
                                        )

                                        // Trigger PaymentPayAvoir with rounded amount
                                        paymentPayAvoirViewModel.PaymentPayAvoir(
                                            totalAmountBigDecimal
                                        )

                                        paymentPayAvoirViewModel.dataResult.observe(lifecycleOwner) { paymentResult ->
                                            when (paymentResult) {
                                                is DataResult.Loading -> {
                                                    Log.d(
                                                        "PAYMENT",
                                                        "Processing PaymentPayAvoir..."
                                                    )
                                                }

                                                is DataResult.Success -> {
                                                    isLoading = false
                                                    val payFromAvoirResponse = true
                                                    Log.d(
                                                        "PAYMENT",
                                                        "PaymentPayAvoir successful: $payFromAvoirResponse"
                                                    )

                                                    balanceViewModel.fetchAndBalance()

                                                    val paymentRequest = PaymentRequest(
                                                        amount = totalAmountSelected.toString(),
                                                        currency = selectedBooking.currencySymbol
                                                            ?: "EUR",
                                                        orderId = bookingId?.toLongOrNull()
                                                            ?.toString() ?: ""
                                                    )

                                                    paymentViewModel.Payment(paymentRequest)

                                                    // Observe payment result to ensure success before confirming booking
                                                    paymentViewModel.dataResult.observe(
                                                        lifecycleOwner
                                                    ) { paymentResult ->
                                                        when (paymentResult) {
                                                            is DataResult.Success -> {
                                                                isLoading = false

                                                                val confirmBookingRequest =
                                                                    ConfirmBookingRequest(
                                                                        amount = totalAmountBigDecimal,
                                                                        numberOfPart = currentSelectedParts,
                                                                        payFromAvoir = payFromAvoirResponse,
                                                                        privateExtrasIds = mappedBookings.first().privateExtrasIds
                                                                            ?: emptyList(),
                                                                        bookingIds = listOfNotNull(
                                                                            bookingId?.toLongOrNull()
                                                                        ),
                                                                        buyerId = mappedBookings.first().buyerId
                                                                            ?: "",
                                                                        couponIds = mappedBookings.first().couponIds
                                                                            ?: emptyMap(),
                                                                        sharedExtrasIds = mappedBookings.first().sharedExtrasIds
                                                                            ?: emptyList(),
                                                                        status = true,
                                                                        token = "",
                                                                        transactionId = "",
                                                                        userIds = mappedBookings.first().userIds
                                                                    )

                                                                confirmBookingViewModel.GetPayment(
                                                                    confirmBookingRequest
                                                                )

                                                                // Observe confirmBookingViewModel result
                                                                confirmBookingViewModel.dataResult.observe(
                                                                    lifecycleOwner
                                                                ) { confirmResult ->
                                                                    when (confirmResult) {
                                                                        is DataResult.Success -> {
                                                                            isLoading = false
                                                                            navController.navigate("PaymentSuccessScreen")
                                                                        }

                                                                        is DataResult.Failure -> {
                                                                            isLoading = false
                                                                        }

                                                                        is DataResult.Loading -> {
                                                                            Log.d(
                                                                                "BOOKING",
                                                                                "Confirming booking..."
                                                                            )
                                                                        }
                                                                    }
                                                                }
                                                            }

                                                            is DataResult.Failure -> {
                                                                isLoading = false
                                                            }

                                                            is DataResult.Loading -> {
                                                                Log.d(
                                                                    "PAYMENT",
                                                                    "Processing payment..."
                                                                )
                                                            }
                                                        }
                                                    }
                                                }

                                                is DataResult.Failure -> {
                                                    isLoading = false
                                                    Log.e(
                                                        "PAYMENT",
                                                        "PaymentPayAvoir failed: ${paymentResult.errorMessage}"
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                                  },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0054D8)),
                        shape = RoundedCornerShape(8.dp),
                        enabled = totalAmountSelected <= balance.toDouble()
                    ) {
                        Text(text = "Payer:  $totalAmountSelected Crédits", fontSize = 18.sp, fontWeight = FontWeight.Bold,color = Color.White)
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = {
                            if (creditErrorRequest != null) {
                                errorCreditViewModel.ErrorCredit(creditErrorRequest)
                            }
                            onDismiss()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Annuler et retourner à la page de recherche",
                            color = Color(0xFF0054D8),
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock, // Confidentiality icon
                                contentDescription = "Confidentialité",
                                tint = Color(0xFF0054D8),
                                modifier = Modifier.size(16.dp) // Adjust size as needed
                            )
                            Spacer(modifier = Modifier.width(4.dp)) // Space between icon and text
                            Text(
                                text = "Le paiement par compte Padelium est géré par la société DevoPro",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp)) // Space between the two rows

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AttachMoney, // Icon representing payment (you can replace with a more suitable one)
                                contentDescription = "Paiement sécurisé",
                                tint = Color(0xFF0054D8),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp)) // Space between icon and text
                            Text(
                                text = "Ce mode de paiement n'engendre pas de frais supplémentaires",
                                fontSize = 10.sp,
                                color = Color.Gray,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = "Timer",
                            tint = Color(0xFF0054D8)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Fermeture automatique dans ${timeLeft / 60}:${String.format("%02d", timeLeft % 60)}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }

                    LinearProgressIndicator(
                        progress = animatedProgress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        color = animatedColor
                    )
                }
            }
        }
    }
}




