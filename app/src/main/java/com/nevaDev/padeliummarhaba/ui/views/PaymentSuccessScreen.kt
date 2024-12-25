package com.nevaDev.padeliummarhaba.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun PaymentSuccessScreen(navController: NavController) {
    val context = LocalContext.current

    // A simple box with a success message
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE5F9E6)) // Light green background for success
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Payment Success",
                tint = Color(0xFF28A745), // Green color for success
                modifier = Modifier.size(100.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Payment Successful!",
                style = MaterialTheme.typography.h4.copy(color = Color.Black),
                modifier = Modifier.padding(8.dp)
            )
            Text(
                text = "Your payment has been processed successfully. Thank you for your purchase.",
                style = MaterialTheme.typography.body1.copy(color = Color.Black),
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { navController.navigate("Home") }, // You can navigate to any screen
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Go to Home")
            }
        }
    }
}
