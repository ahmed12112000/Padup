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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevadev.padeliummarhaba.R

@Composable
fun CreditCharge() {
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
                    PricingCard(
                        title = "Premium",
                        price = "300",
                        credits = "Payer seulement 150 DT"
                    )
                    PricingCard(
                        title = "Gold",
                        price = "600",
                        credits = "Payer seulement 360 DT"
                    )
                    PricingCard(
                        title = "Standard",
                        price = "30",
                        credits = "Payer seulement 50 DT"
                    )
                }
            }
        }
    }
}

@Composable
fun PricingCard(title: String, price: String, credits: String) {
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
            Text(
                text = price,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(4.dp)) // Adds space between the texts

            Text(
                text = "crédits",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black
            )
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
                Text(text =credits, fontSize = 20.sp,fontWeight = FontWeight.Bold,)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreditChargePreview() {
    CreditCharge()
}
