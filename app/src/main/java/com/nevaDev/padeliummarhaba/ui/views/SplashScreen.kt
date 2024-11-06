package com.nevaDev.padeliummarhaba.ui.views
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.* // Imports for padding, alignment
import androidx.compose.material3.* // For modern Material3
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.layout.ContentScale
import com.nevadev.padeliummarhaba.R

@Composable
fun SplashScreen() {
    var isLoading by remember { mutableStateOf(true) }

    // Simulate loading (e.g., you can replace this with real data loading)
    LaunchedEffect(Unit) {
        delay(3000) // Loading time delay (3 seconds)
        isLoading = false
        // Navigate to your next screen after loading is done
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0054D8)), // Full blue background
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title "PADELIUM MARHABA"
            Image(
                painter = painterResource(id = R.drawable.logopadelium), // Replace with your actual image name
                contentDescription = "Padelium Marhaba Logo",
                modifier = Modifier
                    .size(310.dp) // Adjust the size as needed
                    .offset(y = 180.dp) // Adjust vertical offset
                    .padding(bottom = 8.dp),
                contentScale = ContentScale.Fit // Adjust scale as necessary (Fit, Crop, etc.)
            )

            // Subtitle "Your Ultimate Padel Destination"
            Text(
                text = "Your Ultimate Padel Destination",
                fontSize = 29.sp,
                fontWeight = FontWeight.Bold, // Make the text bold
                color = Color.White,
                textAlign = TextAlign.Center, // Center the text horizontally
                modifier = Modifier
                    .fillMaxWidth() // Ensure the text takes up the full width of the screen
                    .padding(bottom = 48.dp)
                    .offset(y = 190.dp) // Adjust the position of the text as needed
            )


            Spacer(modifier = Modifier.weight(1f))

            // Loading Circle
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.Cyan, // Blue circle as in your image
                    strokeWidth = 4.dp,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(bottom = 32.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SplashScreenPreview() {
    SplashScreen()
}