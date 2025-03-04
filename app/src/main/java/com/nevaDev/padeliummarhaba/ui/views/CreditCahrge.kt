package com.nevaDev.padeliummarhaba.ui.views

import android.net.Uri
import android.net.http.SslError
import android.util.Log
import android.view.ViewGroup
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.compose.compiler.plugins.kotlin.ComposeCallableIds.remember
import androidx.compose.compiler.plugins.kotlin.ComposeFqNames.remember
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nevaDev.padeliummarhaba.viewmodels.CreditPayViewModel
import com.nevaDev.padeliummarhaba.viewmodels.ErrorCreditViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetPacksViewModel
import com.nevaDev.padeliummarhaba.viewmodels.PaymentGetAvoirViewModel
import com.nevaDev.padeliummarhaba.viewmodels.UserAvoirViewModel
import com.padelium.data.dto.CreditPayResponseDTO
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.CreditErrorRequest
import com.padelium.domain.dto.CreditPayResponse
import com.padelium.domain.dto.GetPacksResponse
import com.padelium.domain.dto.GetPaymentRequest
import com.padelium.domain.dto.PaymentGetAvoirRequest
import com.padelium.domain.dto.UserAvoirRequest
import com.padelium.domain.dto.UserAvoirResponse
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDate

@Composable
fun CreditCharge(
    viewModel: GetPacksViewModel = hiltViewModel(),
    userAvoirViewModel: UserAvoirViewModel = hiltViewModel(),
    creditPayViewModel: CreditPayViewModel = hiltViewModel(),
    navController: NavController
) {
    // Fetch packs and credits when the screen is loaded
    LaunchedEffect(Unit) {
        viewModel.GetPacks()
        creditPayViewModel.GetCreditPay()
    }

    // Observe data results
    val packsData by viewModel.packsData.observeAsState(DataResult.Loading)
    val paymentResponse by userAvoirViewModel.dataResult.observeAsState(DataResult.Loading)
    val creditsData by creditPayViewModel.CreditsData.observeAsState(DataResultBooking.Loading)

    // Get context from LocalContext
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFE5E5E5)).verticalScroll(rememberScrollState())
    ) {
        // Header Section
        Box(
            modifier = Modifier.fillMaxWidth().height(100.dp).background(Color(0xFF0066CC)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Nos Plans",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFCCFF00)
            )
        }

        // Content Section
        Box(
            modifier = Modifier.fillMaxWidth().background(Color.White).padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Column {
                Text(
                    text = "Découvrez nos offres tarifaires adaptées à vos besoins !",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Display Packs
                when (val result = packsData) {
                    is DataResult.Success -> {
                        val packs = result.data as? List<GetPacksResponse>
                        if (packs != null) {
                            packs.forEach { pack ->
                                val amount = pack.amount
                                val encodedAmount = Uri.encode(amount.toString()) // Correctly encode amount

                                PricingCard(
                                    title = pack.title,
                                    price = pack.description,
                                    credits = amount.toString(),
                                    currencySymbol = pack.currency.currencySymbol,
                                    amount = amount,
                                    currency = "DT",
                                    orderId = "520",
                                    onPaymentClick = { userAvoirRequest ->
                                        userAvoirViewModel.PaymentAvoir(userAvoirRequest)
                                    }
                                )
                            }
                        } else {
                            Text(text = "No packs available")
                        }
                    }
                    is DataResult.Loading -> {
                        Text(text = "Loading...")
                    }
                    is DataResult.Failure -> {
                        // Handle the failure case and navigate to the error screen if errorCode != 200
                        result.errorCode?.let { errorCode ->
                            if (errorCode != 200) {
                                navController.navigate("server_error_screen")
                            } else {
                                Text(text = "Error: ${result.errorMessage}")
                            }
                        } ?: run {
                            Text(text = "Error: ${result.errorMessage}")
                        }
                    }
                }
            }
        }

        // Handle Payment Response
        when (paymentResponse) {
            is DataResult.Success -> {
                val formUrl: String? = ((paymentResponse as DataResult.Success).data as? UserAvoirResponse)?.formUrl

                // Handling the packsData correctly
                val amountValue: BigDecimal? = when (val result = packsData) {
                    is DataResult.Success -> {
                        val packs = result.data as? List<GetPacksResponse>
                        // Get the amount from the first pack, or handle differently if needed
                        packs?.firstOrNull()?.amount
                    }
                    else -> null
                }
                val IdValue: Long? = when (val result = packsData) {
                    is DataResult.Success -> {
                        val packs = result.data as? List<GetPacksResponse>
                        // Get the id from the first pack as Long
                        packs?.firstOrNull()?.id
                    }
                    else -> null
                }
                Log.d("amount", "Selected Parts: $amountValue")

                if (!formUrl.isNullOrEmpty() && amountValue != null) {
                    val encodedUrl = Uri.encode(formUrl)
                    val encodedAmount = Uri.encode(amountValue.toString()) // Correctly encode amount
                    val encodedId = Uri.encode(IdValue.toString())
                    navController.navigate("WebViewScreen1?formUrl=${encodedUrl}&encodedAmount=${encodedAmount}&encodedId=${encodedId}")
                } else {
                    Toast.makeText(context, "No form URL or amount received.", Toast.LENGTH_LONG).show()
                }
            }
            is DataResult.Failure -> {
                Toast.makeText(context, "Payment failed: ${(paymentResponse as DataResult.Failure).errorMessage}", Toast.LENGTH_LONG).show()
            }
            is DataResult.Loading -> {
                // Optionally show loading state or handle it differently
            }
        }
    }
}








@Composable
fun PricingCard(
    title: String,
    price: String,
    credits: String,
    currencySymbol: String,
    amount: BigDecimal,
    currency: String,
    orderId: String,
    onPaymentClick: (UserAvoirRequest) -> Unit // Keep it as a single parameter for now
) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.White,
        elevation = 4.dp,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color.Gray,
                fontSize = 23.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = price,
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.width(4.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    val userAvoirRequest = UserAvoirRequest(
                        amount = amount.toString(),
                        currency = currency,
                        orderId = orderId
                    )
                    onPaymentClick(userAvoirRequest)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF0066CC),
                    contentColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth().height(40.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "$credits $currencySymbol",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}



@Composable
fun WebViewScreen1(
    navController: NavController,
    formUrl: String,
    paymentGetAvoirViewModel: PaymentGetAvoirViewModel,
    amount: BigDecimal,
    Id: Long,

) {
    // Access the local context for WebView
    val context = LocalContext.current
    val amountList = amount.toPlainString().split(",").mapNotNull { it.toLongOrNull() }
    val IdValue = Id
    val errorCreditViewModel: ErrorCreditViewModel = hiltViewModel()
    val coroutineScope = rememberCoroutineScope()
    val isWebViewExpanded = remember { mutableStateOf(true) }

    // Observe dataResult from the ViewModel
    val dataResult by paymentGetAvoirViewModel.dataResult.observeAsState()

    // Manage the loading state
    var isLoading by remember { mutableStateOf(true) }

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
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW // Allow mixed content

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    Log.d("WebViewScreen", "Loading URL: $url")
                    return super.shouldOverrideUrlLoading(view, url)
                }
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest): Boolean {
                    val url = request.url.toString()
                    val packId = extractPackId3(url)
                    val orderId = extractOrderId1(url)
                    val amountBigDecimal = amountList.fold(BigDecimal.ZERO) { acc, amount ->
                        acc + BigDecimal(amount) // Summing up the values in the list to form a BigDecimal
                    }
                    if (orderId.isNotEmpty()) {
                        // Extract other parameters (e.g., booking IDs, user IDs)
                        val request1 = PaymentGetAvoirRequest(
                            amount = amountBigDecimal,
                            packId = IdValue,
                            paymentRef = extractpaymentRef(url),
                            orderId = orderId,
                        )
                        paymentGetAvoirViewModel.PaymentGetAvoir(request1)
                    } else {
                        Log.e("WebViewScreen", "Order ID not found in URL")
                    }
                    navController.navigate("PaymentSuccessScreen")

                    return true
                }
                override fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
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
                bookingIds = listOf(IdValue),
                buyerId = 0L,
                payFromAvoir = false,
                status = true,
                token = "",
                transactionId = 0L
            )

            isWebViewExpanded.value = false
               navController.navigate("CreditCharge")
        },
        modifier = Modifier
            .align(Alignment.TopEnd)
            .padding(8.dp)
            .background(Color.White, shape = CircleShape)
    ) {
        Icon(imageVector = Icons.Default.Close, contentDescription = "Close WebView", tint = Color.Black)
    }
}

// Observe the dataResult from the errorCreditViewModel
val errorDataResult by errorCreditViewModel.dataResult.observeAsState()
errorDataResult?.let { result ->
    when (result) {
        is DataResult.Loading -> {
            Log.d("WebViewScreen", "Processing error credit request...")
        }
        is DataResult.Success -> {
            Log.d("WebViewScreen", "Error credit processed successfully.")
        }
        is DataResult.Failure -> {
            Log.e("WebViewScreen", "Error processing credit: ${result.exception}")
        }
    }
}

    // Handle ViewModel dataResult states
    dataResult?.let { result ->
        when (result) {
            is DataResult.Loading -> {
                Log.d("WebViewScreen", "Fetching payment details...")
                isLoading = true
            }
            is DataResult.Success -> {
                Toast.makeText(context, "Payment details fetched successfully!", Toast.LENGTH_LONG).show()
                Log.d("WebViewScreen", "Payment details fetched successfully: ${result.data}")
                navController.navigate("PaymentSuccessScreen")
            }
            is DataResult.Failure -> {
                Toast.makeText(
                    context,
                    "Failed to fetch payment details: ${result.errorMessage} (Code: ${result.errorCode})",
                    Toast.LENGTH_LONG
                ).show()
                Log.e("WebViewScreen", "Error fetching payment details", result.exception)
            }
        }
    }

    // Optionally show a loading indicator
    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier)
    }
}



// Utility functions with improvements
fun extractamount(url: String): String {
    val uri = Uri.parse(url)
    return uri.getQueryParameter("amount") ?: ""
}
fun extractpaymentRef(url: String): String {
    val uri = Uri.parse(url)
    return uri.getQueryParameter("paymentRef") ?: ""
}
fun extractPackId3(url: String): Long {
    val uri = Uri.parse(url)
    val packIdString = uri.getQueryParameter("packId")
    return packIdString?.toLongOrNull() ?: 0L
}
fun extractOrderId1(url: String): String {
    val uri = Uri.parse(url)
    return uri.getQueryParameter("orderId") ?: ""
}



