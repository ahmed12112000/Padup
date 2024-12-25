package com.nevaDev.padeliummarhaba.ui.views

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetPacksViewModel
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.GetPacksResponse



@Composable
fun CreditCharge(viewModel: GetPacksViewModel = hiltViewModel()) {
    LaunchedEffect(Unit) {
        viewModel.GetPacks()
    }
    val packsData by viewModel.packsData.observeAsState(DataResult.Loading)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5E5E5))
            .verticalScroll(rememberScrollState())

    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .background(Color(0xFF0066CC)),
            contentAlignment = Alignment.Center
        ) {

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Nos Plans",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFCCFF00)
                )


            }

        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Column {
                Text(
                    text = "Découvrez nos offres tarifaires adaptées à vos besoins !",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    when (val result = packsData) {
                        is DataResult.Success -> {
                            val packs = result.data as? List<GetPacksResponse>
                            if (packs != null) {
                                packs.forEach { pack ->
                                    PricingCard(
                                        title = pack.title,
                                        price = pack.description.toString(),
                                        credits = pack.amount.toString(),
                                        currencySymbol = pack.currency.currencySymbol

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
                            Text(text = "Error: ${result.errorMessage}")
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PricingCard(title: String, price: String, credits: String, currencySymbol: String) {
    Card(
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.White,
        elevation = 4.dp,
        modifier = Modifier
            // .weight(1f)
            .padding(horizontal = 4.dp)
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
            Row(
                verticalAlignment = Alignment.CenterVertically // Aligns text vertically
            ) {
            Text( //"description":"200 crédits"
                text = price,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp)) // Adds space between the texts


            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* Handle onClick */ },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF0066CC),
                    contentColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(40.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "$credits $currencySymbol",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )//"amount":150 ---->credits
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreditChargePreview() {
    CreditCharge()
}
