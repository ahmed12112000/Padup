package com.nevaDev.padeliummarhaba.ui.views

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.runtime.Composable
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
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nevadev.padeliummarhaba.R

@Composable
fun CreditPayment(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
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
                modifier = Modifier.padding(start = 16.dp)
            )
                Icon(
                    painter = painterResource(id = R.drawable.a90),
                    contentDescription = "Icon",
                    tint = Color(0xFFCCFF00),
                    modifier = Modifier
                        .size(150.dp)
                        .offset(x = 62.dp)

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
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Card(
                    backgroundColor = Color(0xFFFFF4C2),
                    modifier = Modifier.fillMaxWidth(),
                    elevation = 2.dp
                ) {
                    Text(
                        text = "Votre solde est 0 Crédits",
                        modifier = Modifier.padding(16.dp),
                        fontSize = 16.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Button(
                        onClick = { navController.navigate("CreditCharge") },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White,
                            contentColor = Color(0xFF0066CC),
                        ),
                        modifier = Modifier
                            .padding(start = 3.dp)
                            .height(50.dp)
                            .fillMaxWidth(0.6f)
                            .offset(x = 150.dp),
                        border = BorderStroke(1.dp, Color(0xFF0054D8)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Charger votre compte",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Box (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Historiques",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black,
                    )
                }

            }
        }
    }

    Column() {

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .offset(y = 300.dp)
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
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.raquettebl),
                            contentDescription = "Reservation Icon",
                            modifier = Modifier
                                .size(30.dp)
                                .align(Alignment.Center)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
// 2 chifres apres virgule money......Pach sans verule
                    //control.....pack 300----<300 credit.....not "seulement
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Réservation",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(7.dp))
                            Text(
                                text = "05/12/2024 13:28",
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp
                            )

                            Spacer(modifier = Modifier.height(7.dp))


                        }
                    Spacer(modifier = Modifier.width(130.dp))

                        Column(
                           // modifier = Modifier.weight(1f),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "-30.50",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
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
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = 4.dp,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 300.dp)
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
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.creditcard),
                                contentDescription = "Reservation Icon",
                                modifier = Modifier
                                    .size(30.dp)
                                    .align(Alignment.Center)  // Control the position (centered in this case)
                            )
                        }
                        Spacer(modifier = Modifier.width(13.dp)) // Space between image and text

                        // Column for text content
                        Column(
                            verticalArrangement = Arrangement.Center,  // Center the text vertically in the column
                            horizontalAlignment = Alignment.Start  // Align the text to the start (left)
                        ) {
                            Text(
                                    text = "Alimentation",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(7.dp))
                            Text(
                                text = "05/12/2024 13:55",
                                fontWeight = FontWeight.Normal,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.height(7.dp))
                        }

                        Spacer(modifier = Modifier.width(130.dp)) // Space between image and text

                        // Column for right-aligned text content
                        Column(
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "400.50",
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
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
    } }


@Preview(showBackground = true)
@Composable
fun CreditPaymentPreview() {
    CreditPayment( navController = rememberNavController())
}
