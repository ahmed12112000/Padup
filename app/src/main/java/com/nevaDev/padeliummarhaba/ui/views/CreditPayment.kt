package com.nevaDev.padeliummarhaba.ui.views

import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nevaDev.padeliummarhaba.viewmodels.CreditPayViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetPacksViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.DataResultBooking
import com.padelium.domain.dto.CreditPayResponse
import com.padelium.domain.dto.GetPacksResponse
import com.padelium.domain.dto.GetReservationResponse
import java.math.BigDecimal

@Composable
fun CreditPayment(
    navController: NavController,
    viewModel: GetPacksViewModel = hiltViewModel(),
    viewModel2: CreditPayViewModel = hiltViewModel(),

) {
    val packsData by viewModel.packsData.observeAsState(DataResult.Loading)

    when (val result = packsData) {
        is DataResult.Loading -> {
            CircularProgressIndicator()
        }

        is DataResult.Success -> {
            // Show the data (e.g., packs)
            val packs = result.data as List<GetPacksResponse>

        }

        is DataResult.Failure -> {
            val errorMessage = result.errorMessage
            Text(text = "Error: $errorMessage")
        }
    }

    val CreditsData by viewModel2.CreditsData.observeAsState()
    val Credits = remember { mutableStateOf<List<CreditPayResponse>>(emptyList()) }


    // On data change, update reservations list
    LaunchedEffect(CreditsData) {
        if (CreditsData is DataResultBooking.Success) {
            Credits.value = (CreditsData as DataResultBooking.Success<List<CreditPayResponse>>).data
        }
    }

    // Fetch reservations when screen is launched
    LaunchedEffect(Unit) {
        viewModel2.GetCreditPay()
    }
    val reservations = Credits.value.filter { it.userAvoirTypeName == "Réservation" }
    val alimentations = Credits.value.filter { it.userAvoirTypeName == "Alimentation" }

    val totalReservations = reservations.sumOf { it.amount }
    val totalAlimentations = alimentations.sumOf { it.amount }
    val balance = totalAlimentations + totalReservations


    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .verticalScroll(rememberScrollState())
                .background(Color(0xFF0066CC)),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Détails\nCrédits",
                    color = Color(0xFFCCFF00),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 16.dp).offset(y = -25.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.a90),
                    contentDescription = "Icon",
                    tint = Color(0xFFCCFF00),
                    modifier = Modifier
                        .size(150.dp)
                        .offset(x = 60.dp, y = -40.dp)

                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()) // Add scrolling
                    .padding(16.dp)
            ) {
                Card(
                    backgroundColor = Color(0xFFFFF4C2),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 2.dp
                ) {
                    Text(
                        text = "Votre solde est $balance Crédits",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(0.8f).offset(x=110.dp),
                    verticalAlignment = Alignment.CenterVertically,

                ) {

                    Button(
                        onClick = {
                            navController.navigate("CreditCharge")
                            viewModel.GetPacks()
                        },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White,
                            contentColor = Color(0xFF0066CC),
                        ),
                        modifier = Modifier
                            .padding(start = 3.dp)
                            .height(50.dp)
                            .fillMaxWidth(0.8f) // Increased width to prevent text cut-off
                            .border(1.dp, Color(0xFF0054D8), RoundedCornerShape(13.dp)),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text(
                            text = "Charger votre compte",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.wrapContentWidth(), // Ensures text is fully displayed
                            textAlign = TextAlign.Center
                        )
                    }

                }
                Spacer(modifier = Modifier.height(16.dp))


                // Display Reservation List
                Text(
                    text = "Historiques",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                )
                Spacer(modifier = Modifier.height(8.dp))
                reservations.forEach { credit ->
                    CreditCard(credit)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display Alimentation List

                alimentations.forEach { credit ->
                    CreditCard(credit)
                }
            }
        }
    }
}

@Composable
fun CreditCard(credit: CreditPayResponse) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(modifier = Modifier.size(50.dp)) {
                        Image(
                            painter = painterResource(id = R.drawable.raquettebl),
                            contentDescription = "Reservation Icon",
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier.weight(1f)
                    ) {
                        // Dynamically adjusting text size based on available space
                        Text(
                            text = credit.userAvoirTypeName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            softWrap = false,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = credit.createdStr,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            softWrap = false,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp)) // Reduce extra space

                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    val formattedAmount = if (credit.amount.compareTo(BigDecimal.ZERO) == 0) {
                        "0"
                    } else {
                        String.format("%.2f", credit.amount)
                    }
                    Text(
                        text = "$formattedAmount",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "crédits",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}



