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
import com.nevaDev.padeliummarhaba.viewmodels.PartnerPayViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentParCreditViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPayAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentViewModel
import com.nevaDev.padeliummarhaba.viewmodels.SaveBookingViewModel
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.BalanceResponse
import com.padelium.domain.dto.ConfirmBookingRequest
import com.padelium.domain.dto.CreditErrorRequest
import com.padelium.domain.dto.GetBookingResponse
import com.padelium.domain.dto.GetProfileResponse
import com.padelium.domain.dto.PaymentParCreditRequest
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
fun PopupCreditPartner(
    navController: NavController,
    showPopup: Boolean,
    onDismiss: () -> Unit,
    viewModel4: PartnerPayViewModel = hiltViewModel(),
    partnerPayId: String?,
    viewModel2: PaymentParCreditViewModel = hiltViewModel(),
    viewModel: GetProfileViewModel = hiltViewModel(),
    totalPrice: BigDecimal,

) {
    val dataResult by viewModel2.dataResult.observeAsState()

    LaunchedEffect(partnerPayId) {
        partnerPayId?.let {
            viewModel4.partnerPay(it.toLong())
        }
    }
    LaunchedEffect(dataResult) {
        when (dataResult) {
            is DataResult.Success -> {
                navController.navigate("main_screen")
            }
            is DataResult.Failure -> {
                Log.e("PaymentButton", "Payment failed: ${(dataResult as DataResult.Failure).errorMessage}")
            }
            else -> {
            }
        }
    }


    val privateList = remember { mutableStateOf<MutableList<Long>>(mutableListOf()) }

    if (!showPopup) return
    val confirmBookingViewModel: ConfirmBookingViewModel = hiltViewModel()
    val type = object : TypeToken<List<GetBookingResponse>>() {}.type
    var isLoading by remember { mutableStateOf(false) }
    val paymentPayAvoirViewModel: PaymentPayAvoirViewModel = hiltViewModel()
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val paymentViewModel: PaymentViewModel = hiltViewModel()

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var balance by remember { mutableStateOf(BigDecimal.ZERO) }

    val balanceViewModel: BalanceViewModel = hiltViewModel()
    val balanceData by balanceViewModel.dataResult.observeAsState(DataResult.Loading)
    val profileData by viewModel.profileData.observeAsState(DataResult.Loading)


    var bookingId1 by remember { mutableStateOf<Long?>(null) }

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




    LaunchedEffect(viewModel) {
            viewModel.fetchProfileData()
            balanceViewModel.fetchAndBalance()

            while (elapsedTime < totalTime) {
                delay(1000)
                elapsedTime += 1f
            }


            onDismiss()

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
                    .fillMaxHeight(0.75f) // Take half the screen
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
                                onDismiss( )


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
                    Spacer(modifier = Modifier.height(16.dp)) // Increased spacer height
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "$firstName $lastName", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp)) // Increased spacer height

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
                    Spacer(modifier = Modifier.height(16.dp)) // Increased spacer height
                    val coroutineScope = rememberCoroutineScope()

                    Button(
                        onClick = {
                            if (isLoading) return@Button
                            viewModel4.partnerPayResponse.observeForever { response ->
                                response?.let {


                                    val paymentParCreditRequest = PaymentParCreditRequest(
                                        id = partnerPayId?.toLongOrNull() ?:0L,
                                        privateExtrasIds = privateList.value.map { it as Long? }
                                    )

                                    viewModel2.PaymentParCredit(paymentParCreditRequest)
                                }
                            }

                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0054D8)),
                        shape = RoundedCornerShape(8.dp),
                        enabled = totalPrice <= BigDecimal(balance.toString())
                    ) {
                        Text(text = "Payer:  $totalPrice Crédits", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)

                    }
                    Spacer(modifier = Modifier.height(24.dp)) // Increased spacer height

                    TextButton(
                        onClick = {
                            onDismiss( )
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
                    Spacer(modifier = Modifier.height(24.dp)) // Increased spacer height

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
                    Spacer(modifier = Modifier.height(16.dp)) // Increased spacer height

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
                            .padding(top = 16.dp), // Adjusted padding for better visibility
                        color = animatedColor
                    )
                }
            }
        }
    }
}




