package com.nevaDev.padeliummarhaba.ui.views

import android.net.Uri
import android.net.http.SslError
import android.util.Log
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nevaDev.padeliummarhaba.viewmodels.ConfirmBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.ErrorCreditViewModel
import com.nevaDev.padeliummarhaba.viewmodels.FindTermsViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PartnerPayViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentParCreditViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPartBookingViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentPartViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PrivateExtrasViewModel
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.CreditErrorRequest
import com.padelium.domain.dto.PaymentParCreditRequest
import com.padelium.domain.dto.PaymentPartBookingRequest
import com.padelium.domain.dto.PaymentRequest
import com.padelium.domain.dto.PaymentResponse
import com.padelium.domain.dto.PrivateExtrasResponse
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate


@Composable
fun PartnerPaymentScreen(
    navController: NavController,
    viewModel4: PartnerPayViewModel = hiltViewModel(),
    viewModel3: PaymentPartViewModel = hiltViewModel(),
    partnerPayId: String?,
    viewModel2: PaymentParCreditViewModel = hiltViewModel(),

    ) {
    var showPopup by remember { mutableStateOf(false) }
    val viewModel: GetProfileViewModel = hiltViewModel()
    var totalPrice by remember { mutableStateOf(BigDecimal.ZERO) }
    var isLoading by remember { mutableStateOf(false) }
    val partnerPayResponse by viewModel4.partnerPayResponse.observeAsState()
    var selectedExtras by remember { mutableStateOf<List<Triple<String, String, Int>>>(emptyList()) }
    var totalExtrasCost by remember { mutableStateOf(0.0) }
    val context = LocalContext.current
    val privateList = remember { mutableStateOf<MutableList<Long>>(mutableListOf()) }

    LaunchedEffect(partnerPayId) {
        partnerPayId?.let {
            viewModel4.partnerPay(it.toLong())
        }
    }
    fun updateExtras(newExtras: List<Triple<String, String, Int>>, newTotalExtrasCost: Double) {
        selectedExtras = newExtras
        totalExtrasCost = newTotalExtrasCost
    }

    fun handleTotalPriceCalculated(newTotalPrice: BigDecimal) {
        totalPrice = newTotalPrice
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Extras Section
        androidx.compose.material3.Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            ExtrasSection2(onExtrasUpdate = ::updateExtras, privateList = privateList)
        }

        // Reservation Summary Section
        androidx.compose.material3.Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            // Call ReservationSummary2 to display it in the card
            partnerPayResponse?.let { response ->
                ReservationSummary2(
                    viewModel4 = viewModel4,
                    selectedExtras = selectedExtras,
                    totalExtrasCost = totalExtrasCost,
                    onTotalPriceCalculated = { newTotalPrice ->
                        handleTotalPriceCalculated(newTotalPrice) // Handle the updated total price
                    }
                )

            } ?: run {
                // Optionally show a placeholder or loading state if no reservation is selected
                Text(
                    text = "No reservation selected",
                    modifier = Modifier.padding(16.dp),
                    //  style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        // Purchase Credits Section
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

        // Buttons for Payment
        Row(
            modifier = Modifier
                .fillMaxWidth(),
                //.offset(x = 10.dp, ),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()

                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                var errorMessage by remember { mutableStateOf("") }
                Row(modifier = Modifier.weight(1.1f)) {

                    Button(
                        onClick = {
                            viewModel4.partnerPayResponse.observeForever { response ->
                                response?.let {
                                    val totalAmount =
                                        (response.amount + totalExtrasCost.toBigDecimal()).setScale(
                                            2,
                                            RoundingMode.HALF_UP
                                        )

                                    val paymentRequest = PaymentRequest(
                                        amount = totalAmount.toString(),
                                        currency = "DT",
                                        orderId = response.id.toString()
                                    )

                                    viewModel3.PaymentPart(paymentRequest)

                                    viewModel3.dataResult.observeForever { paymentResult ->
                                        when (paymentResult) {
                                            is DataResult.Loading -> {
                                                Log.d("Payment", "Processing payment...")
                                                errorMessage =
                                                    "" // Reset error message when loading
                                            }

                                            is DataResult.Success -> {
                                                Log.d("Payment", "Payment processed successfully!")

                                                val paymentResponse =
                                                    paymentResult.data as? PaymentResponse
                                                val formUrl = paymentResponse?.formUrl
                                                val orderId = paymentResponse?.orderId
                                                val encodedBookingId =
                                                    Uri.encode(paymentRequest.orderId)
                                                val privateListString =
                                                    privateList.value.joinToString(",")
                                                val encodedPrivateList =
                                                    Uri.encode(privateListString)
                                                val encodedPartnerPayId =
                                                    Uri.encode(partnerPayId ?: "")

                                                if (!formUrl.isNullOrEmpty() && !orderId.isNullOrEmpty()) {
                                                    val encodedUrl = Uri.encode(formUrl)

                                                    val navigationRoutee =
                                                        "WebViewScreen2?formUrl=$encodedUrl&orderId=$orderId&BookingId=$encodedBookingId&privateList=$encodedPrivateList&encodedPartnerPayId=$encodedPartnerPayId"

                                                    Log.d(
                                                        "NavigationDebug",
                                                        "Navigating to: $navigationRoutee"
                                                    )
                                                    navController.navigate(navigationRoutee)
                                                    errorMessage =
                                                        "" // Clear error message when successful
                                                } else {
                                                    Log.e(
                                                        "Payment",
                                                        "No form URL found in the response."
                                                    )
                                                    errorMessage =
                                                        "Cette réservation n'est pas disponible pour le moment."
                                                }
                                            }

                                            is DataResult.Failure -> {
                                                Log.e(
                                                    "Payment",
                                                    "Payment failed: ${paymentResult.errorMessage}"
                                                )
                                                errorMessage =
                                                    "Payment failed: ${paymentResult.errorMessage}"
                                            }
                                        }
                                    }
                                }
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .offset(x = -15.dp)
                            .height(48.dp)
                            .weight(1f),
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
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }

                }
                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Row(modifier = Modifier.weight(1.3f)) {

                    Button(
                        onClick = {

                            showPopup = true

                        },
                        modifier = Modifier
                            .height(48.dp),
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
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (showPopup) {
                        PopupCreditPartner(
                            navController = navController, // Pass the NavController
                            showPopup = showPopup, // Toggle state correctly
                            onDismiss = { showPopup = false }, // Handle dismissal
                            viewModel4 = viewModel4, // Pass shared view model
                            //  bookingId = bookingId,
                            partnerPayId = partnerPayId,
                            viewModel = viewModel,
                            totalPrice = totalPrice, // Pass the updated totalPricee to the popup

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
    findTermsViewModel: FindTermsViewModel = hiltViewModel(),
    privateList: MutableState<MutableList<Long>>, // Pass privateList from Parent

) {
    var additionalExtrasEnabled by remember { mutableStateOf(false) }
    val privateExtrasState by viewModel4.extrasState2.observeAsState()
    var selectedExtras by remember { mutableStateOf<List<Triple<String, String, Int>>>(emptyList()) }


    LaunchedEffect(viewModel4) {
        viewModel4.PrivateExtras()
    }

    val totalExtrasCost by remember { derivedStateOf { selectedExtras.sumOf { it.second.toDouble() } } }

    Row(
        modifier = Modifier.fillMaxWidth().offset(y = (-8).dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
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
        Spacer(modifier = Modifier.height(2.dp))

        when (privateExtrasState) {
            is DataResult.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is DataResult.Success -> {
                val privateExtrasList =
                    (privateExtrasState as DataResult.Success).data as? List<PrivateExtrasResponse>

                Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                    Text(
                        text = "  Article(s) réserver à mon usage",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    privateExtrasList?.forEach { privateExtra ->
                        val isPrivateExtraAdded = privateList.value.contains(privateExtra.id)

                        ExtraItemCard(
                            extra = privateExtra,
                            isAdded = isPrivateExtraAdded,
                            onAddClick = { extraPrice ->
                                privateList.value = (privateList.value + privateExtra.id).toMutableList()
                                findTermsViewModel.updatePrivateExtras(privateList.value)
                                Log.d("ExtrasSection2", "Added private extra ID: ${privateExtra.id}, List: $privateList")

                                selectedExtras += Triple(
                                    privateExtra.name,
                                    privateExtra.amount.toString(),
                                    privateExtra.currencyId.toInt()
                                )
                                onExtrasUpdate(selectedExtras, totalExtrasCost)
                            },
                            onRemoveClick = { extraPrice ->
                                privateList.value = (privateList.value - privateExtra.id).toMutableList()
                                findTermsViewModel.updatePrivateExtras(privateList.value)
                                Log.d("ExtrasSection2", "Removed private extra ID: ${privateExtra.id}, List: $privateList")

                                selectedExtras = selectedExtras.filterNot { it.first == privateExtra.name }
                                onExtrasUpdate(selectedExtras, totalExtrasCost)
                            }
                        )
                    }
                }
            }
            else -> {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = "Failed to load extras", color = Color.Red)
                }
            }
        }
    }
}

@Composable
fun ReservationSummary2(
    viewModel4: PartnerPayViewModel = hiltViewModel(),
    selectedExtras: List<Triple<String, String, Int>>,
    totalExtrasCost: Double,
    onTotalPriceCalculated: (BigDecimal) -> Unit // Callback to pass totalPrice

    ) {
    val storedPartnerPayResponse by viewModel4.partnerPayResponse.observeAsState()



    LaunchedEffect(storedPartnerPayResponse) {
        storedPartnerPayResponse?.let { response ->
            Log.d("ReservationCarddddd", "PartnerPay response updated: $response")
        }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(16.dp)
    ) {
        Text(
            text = "Détails Réservation",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        storedPartnerPayResponse?.let { booking ->
            val totalPrice = (booking.amount + BigDecimal(totalExtrasCost)).setScale(2, RoundingMode.HALF_UP)
            onTotalPriceCalculated(totalPrice)
            ReservationDetailRow(label = "Espace", value = booking.bookingEstablishmentName ?: "N/A")
            ReservationDetailRow(label = "Heure", value = booking.bookingDateStr ?: "N/A")
            ReservationDetailRow(label = "Prix", value = "${booking.amount} DT")
            ReservationDetailRow(
                label = "Réservé par",
                value = "${booking.bookingCreatedFirstName ?: "N/A"} ${booking.bookingCreatedLastName ?: "N/A"}"
            )
            ReservationDetailRow(
                label = "Joueurs",
                value = "${booking.bookingCreatedFirstName ?: "N/A"} ${booking.bookingCreatedLastName ?: "N/A"} ${booking.userFirstName ?: "N/A"} ${booking.userLastName ?: "N/A"}"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Extras Sélectionnés",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            selectedExtras.forEach { extra ->
                ReservationDetailRow(label = extra.first, value = "${extra.second} DT")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Détails du Prix",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            ReservationDetailRow(label = "Prix de Réservation", value = "${booking.amount} DT")
            ReservationDetailRow(label = "Extras Total", value = "$totalExtrasCost DT")
            ReservationDetailRow(label = "Total", value = "$totalPrice DT")

        }
    }

}


@Composable
fun WebViewScreen2(
    navController: NavController,
    viewmodel: PaymentPartBookingViewModel,
    formUrl: String,
    onReservationClicked: (LocalDate) -> Unit,

    ) {
    val backStackEntry = navController.currentBackStackEntry
    val context = LocalContext.current
    val BookingId = backStackEntry?.arguments?.getString("BookingId")?.toLongOrNull() ?: 0L
    val encodedPartnerPayId = backStackEntry?.arguments?.getString("encodedPartnerPayId")?.toLongOrNull() ?: 0L

    val navBackStackEntry = remember { navController.currentBackStackEntry }
    val privateListString = navBackStackEntry?.arguments?.getString("privateList") ?: ""
    val privateList = privateListString.split(",").mapNotNull { it.toLongOrNull() }.toMutableList()
    val isWebViewExpanded = remember { mutableStateOf(true) }

    // Observe dataResult from the ViewModel    BookingId
    val dataResult by viewmodel.dataResult.observeAsState()
    val coroutineScope = rememberCoroutineScope()
    var isButtonClicked by remember { mutableStateOf(false) }

    // Manage the loading state
    var isLoading by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()
    val bookingIdsString = backStackEntry?.arguments?.getString("BookingId") ?: ""
    val bookingIdsList = bookingIdsString.split(",").mapNotNull { it.toLongOrNull() }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp)
                .border(2.dp, Color.Gray, RoundedCornerShape(12.dp))
        ) {
    // AndroidView to embed the WebView
    AndroidView(factory = {
        WebView(context).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true


            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    Log.d("WebViewScreen", "Loading URL: $url")
                    return super.shouldOverrideUrlLoading(view, url)
                }

                override fun shouldOverrideUrlLoading(
                    view: WebView?,
                    request: WebResourceRequest
                ): Boolean {
                    val url = request.url.toString()
                    val orderId = extractOrderId2(url)

                    if (orderId.isNotEmpty()) {
                        // Extract other parameters (e.g., booking IDs, user IDs)
                        val request1 = PaymentPartBookingRequest(
                            privateExtrasIds = privateList,
                            id = BookingId,
                            bookingId = encodedPartnerPayId,
                            orderId = orderId,
                        )
                        coroutineScope.launch {
                            try {
                                val response: Boolean = viewmodel.PaymentPartBooking(request1, navController)
                                if (response) {
                                    Log.d("WebViewScreen", "Payment successful, navigating to success screen.")
                                    navController.navigate("PaymentSuccessScreen")
                                } else {
                                    Log.e("WebViewScreen", "Payment failed.")
                                    navController.navigate("payment_error_screen")
                                }
                            } catch (e: Exception) {
                                Log.e("WebViewScreen", "Exception during payment process: ${e.localizedMessage}")
                                navController.navigate("payment_error_screen")
                            }
                        }
                    } else {
                        Log.e("WebViewScreen", "Order ID not found in URL")
                        navController.navigate("payment_error_screen")
                    }

                    return true
                }

                override fun onReceivedSslError(
                    view: WebView?,
                    handler: SslErrorHandler?,
                    error: SslError?
                ) {
                    // Ignore SSL certificate errors (for testing only)
                    handler?.proceed()
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    Log.d("WebView", "Page finished loading: $url")
                    isLoading = false
                }
            }

            // Load the initial URL
            loadUrl(formUrl)
        }
    }, modifier = Modifier.fillMaxSize()
    )
            IconButton(
                onClick = {
                    val creditErrorRequest = CreditErrorRequest(
                        amount = BigDecimal.ZERO,
                        bookingIds = listOf(encodedPartnerPayId),
                        buyerId = 0L,
                        payFromAvoir = false,
                        status = true,
                        token = "",
                        transactionId = 0L
                    )

                    val selectedDate = LocalDate.now()

                    if (!isButtonClicked) {
                        isButtonClicked = true

                        navController.navigate("main_screen")
                    }

                    isWebViewExpanded.value = false
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(8.dp)
                    .background(Color.White, shape = CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close WebView",
                    tint = Color.Black
                )
            }
        }
    }
    dataResult?.let { result ->
        when (result) {
            is DataResult.Loading -> {
                Log.d("WebViewScreen", "Fetching payment details...")
            }

            is DataResult.Success -> {

                navController.navigate("PaymentSuccessScreen")
            }

            is DataResult.Failure -> {

                Log.e("WebViewScreen", "Error fetching payment details", result.exception)
            }
        }
    }
}



// Utility functions with improvements
fun extractamount1(url: String): String {
    val uri = Uri.parse(url)
    return uri.getQueryParameter("amount") ?: ""
}
fun extractpaymentRef1(url: String): String {
    val uri = Uri.parse(url)
    return uri.getQueryParameter("paymentRef") ?: ""
}
fun extractPackId1(url: String): Long {
    val uri = Uri.parse(url)
    val packIdString = uri.getQueryParameter("packId")
    return packIdString?.toLongOrNull() ?: 0L
}
fun extractOrderId2(url: String): String {
    val uri = Uri.parse(url)
    return uri.getQueryParameter("orderId") ?: ""
}



