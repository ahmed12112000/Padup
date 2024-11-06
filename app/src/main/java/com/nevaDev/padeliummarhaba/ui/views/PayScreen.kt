package com.nevaDev.padeliummarhaba.ui.views

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.nevadev.padeliummarhaba.R

@Composable
fun PayScreen(totalAmount: Int, navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    var cardNumber by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var cardholderName by remember { mutableStateOf("") }
    var emailAddress by remember { mutableStateOf("") }
    var isEmailChecked by remember { mutableStateOf(true) }
    var optionalMessage by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // Tabs for payment methods
        TabRow(
            selectedTabIndex = selectedTab,
            backgroundColor = Color.Transparent,
            indicator = { tabPositions ->
                TabRowDefaults.Indicator(
                    Modifier
                        .tabIndicatorOffset(tabPositions[selectedTab])
                        .height(4.dp), // Adjust the height of the indicator if needed
                    color = Color(0xFF0054D8) // Set the color of the indicator
                )
            }
        ) {
            // First Tab (Card Crédit)
            Tab(
                selected = selectedTab == 0,
                onClick = { selectedTab = 0 },
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp)) // Apply rounded corners
                    .border(1.dp, if (selectedTab == 0) Color(0xFF0054D8) else Color.Transparent, RoundedCornerShape(8.dp))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Add the icon next to the text
                    Icon(
                        painter = painterResource(id = R.drawable.a123), // Replace with your drawable resource name
                        contentDescription = "Card Crédit Icon",
                        modifier = Modifier.size(20.dp),
                        tint = if (selectedTab == 0) Color.Unspecified else Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp)) // Space between icon and text
                    Text(
                        text = "Card Crédit",
                        modifier = Modifier.padding(8.dp),
                        fontWeight = FontWeight.Bold,
                        color = if (selectedTab == 0) Color(0xFF0054D8) else Color.Gray
                    )
                }
            }

            // Second Tab (Crédit Padelium)
            Tab(
                selected = selectedTab == 1,
                onClick = { selectedTab = 1 },
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp)) // Apply rounded corners
                    .border(1.dp, if (selectedTab == 1) Color(0xFF0054D8) else Color.Transparent, RoundedCornerShape(8.dp))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Add the icon next to the text
                    Icon(
                        painter = painterResource(id = R.drawable.a123), // Replace with your drawable resource name
                        contentDescription = "Crédit Padelium Icon",
                        modifier = Modifier.size(20.dp),
                        tint = if (selectedTab == 1) Color.Unspecified else Color.Gray
                    )
                    Spacer(modifier = Modifier.width(4.dp)) // Space between icon and text
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

        // Card number field
        OutlinedTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            label = { Text("Numéro de la carte") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Month and Year fields
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

        // Security Code field
        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            label = { Text("Code de sûreté") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(24.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Cardholder Name field
        OutlinedTextField(
            value = cardholderName,
            onValueChange = { cardholderName = it },
            label = { Text("Le nom du détenteur") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Email Checkbox and Address Field
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

        // Payment button
        Button(
            onClick = {
                navController.navigate("PayScreen/$totalAmount") // Ensure the correct argument is passed
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0054D8)),
            shape = RoundedCornerShape(24.dp)
        ) {
            Text(
                text = "Paiement $totalAmount DT", // Use totalAmount here
                color = Color.White,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Optional Message field
        OutlinedTextField(
            value = optionalMessage,
            onValueChange = { optionalMessage = it },
            label = { Text("Taper votre message (optionnel)") },
            modifier = Modifier
                .fillMaxWidth()
                .size(250.dp),
            shape = RoundedCornerShape(24.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PaymentScreenPreview() {
    PayScreen(totalAmount = 100, navController = rememberNavController())
}

//i ant after clicking on "Payer avec Carte" button to navigate to