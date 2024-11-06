package com.nevaDev.padeliummarhaba.ui.views

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.nevadev.padeliummarhaba.R

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    var prenom by remember { mutableStateOf("") }
    var nom by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var isValid by remember { mutableStateOf(false) }
    // Profile image picker
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            profileImageUri = uri
        } else {
            Toast.makeText(context, "Image selection cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        // Progress Bar and title
        Text(
            text = "Mon Profil",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
                    color = Color.Black,
            modifier = Modifier.offset(x = 5.dp)
        )
        Spacer(modifier = Modifier.height(13.dp))

        HorizontalDivider(modifier = Modifier .width(900.dp).padding(horizontal = 10.dp).offset(y = -10.dp),
            color = Color.Gray, thickness = 1.dp)
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = "Progression de votre profil :",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.offset(x = 14.dp)

        )
        Spacer(modifier = Modifier.height(13.dp))
        HorizontalDivider(modifier = Modifier .width(900.dp).padding(horizontal = 17.dp).offset(y = -10.dp),
            color = Color.Gray, thickness = 1.dp)
        Spacer(modifier = Modifier.height(8.dp))

        // Simulate a progress bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp)
                .padding(horizontal = 17.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(24.dp)) // Gray border
                .clip(RoundedCornerShape(24.dp)) // Rounded corners
        ) {
            // LinearProgressIndicator
            LinearProgressIndicator(
                progress = 0.75f,
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFFD7F057), // Progress color
                backgroundColor = Color.White, // Background color
                strokeCap = StrokeCap.Round // Rounded ends
            )

            // Text inside the LinearProgressIndicator
            Text(
                text = "75%",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center) // Center the text in the Box
                    .fillMaxSize(), // Make sure the Text takes the same size as the Box
                color = Color.Black
            )
        }



        Spacer(modifier = Modifier.height(16.dp))

        // First Name Input
        BasicTextField(
            value = prenom,
            onValueChange = { prenom = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            decorationBox = { innerTextField ->
                Column {
                    Text(text = "| Nom", color = Color.Black,  fontSize = 19.sp,
                        modifier = Modifier.offset(x = 25.dp))

                    // Use a Box to apply rounded corners
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            //.shadow(1.dp, RoundedCornerShape(15.dp))
                           // .clip(RoundedCornerShape(24.dp)) // Clip the inner content to the rounded shape
                    ) {
                        OutlinedTextField(
                            value = prenom,
                            onValueChange = { prenom = it },
                            label = { Text("User Lastname") },
                           // leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(15.dp)
                        )
                        }
                    }

            }
        )


        Spacer(modifier = Modifier.height(8.dp))

        // Last Name Input
        BasicTextField(
            value = nom,
            onValueChange = { nom = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            decorationBox = { innerTextField ->
                Column {
                    Text(text = "| Prénom", color = Color.Black,  fontSize = 19.sp,
                        modifier = Modifier.offset(x = 25.dp))

                    // Use a Box to apply rounded corners
                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = prenom,
                            onValueChange = { prenom = it },
                            label = { Text("User Firstname") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(15.dp)
                        )
                        }
                    }

            }
        )


        Spacer(modifier = Modifier.height(16.dp))

        // Phone Number Input
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = phoneNumber,
                    onValueChange = {
                        // Allow only numbers and limit to 8 digits
                        if (it.all { char -> char.isDigit() } && it.length <= 8) {
                            phoneNumber = it
                            isValid = it.length == 8 // Check if valid
                        }
                    },
                    modifier = Modifier
                        .offset(x = 3.dp)
                        .width(150.dp)
                        .padding(vertical = 4.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Phone Icon
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Phone Icon",
                                modifier = Modifier.padding(8.dp)
                            )
                            Text(
                                text = "|",
                                fontSize = 29.sp,
                                color = Color.Black,
                                modifier = Modifier.offset(x = -8.dp, y = -2.dp))
                            // Inner TextField
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                innerTextField()
                            }

                            // Success Icon
                            if (isValid) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Valid Phone Number",
                                    tint = Color.Green,
                                    modifier = Modifier.padding(8.dp) // Padding for the icon
                                )
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.width(10.dp)) // Add some space between the fields

                // Button to validate the phone number

                    Button(
                        onClick = {
                            // Validate the phone number or perform any action
                            if (isValid) {
                                println("Phone number is valid: $phoneNumber")
                            } else {
                                println("Phone number must be 8 digits.")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth().padding(10.dp)
                            .border(1.dp, Color.Unspecified, RoundedCornerShape(13.dp))
                            .clip(RoundedCornerShape(10.dp)) ,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White) // White background for the button
                    ) {
                        Text(text = "Modifier", color = Color.Blue, fontWeight = FontWeight.Bold,) // Set text color to black for contrast
                    }
                }



            // "Modifier" text below the phone input
            if (phoneNumber.isNotEmpty()) {
                Text(
                    text = "Modifier",
                    color = Color.Blue,
                    modifier = Modifier.padding(top = 4.dp)
                )

            }
            Text(text = "Votre numéro ne nous sert que pour communiquer les informations utiles à vos réservations.",
                color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Profile Picture Upload
        // Profile Picture Upload
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "| Modifier la photo de profil",
                color = Color.Black,
                fontSize = 19.sp,
                modifier = Modifier.offset(x = 25.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Box to hold the upload icon or selected image
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .padding(4.dp)

            ) {
                if (profileImageUri != null) {
                    // Display selected image
                    Image(
                        painter = rememberAsyncImagePainter(profileImageUri),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize().clickable {
                            launcher.launch("image/*") // Launches file picker to select an image
                        },
                        contentScale = ContentScale.Crop
                    )
                } else {
                    // Display upload icon when no image is selected
                    Icon(
                        painter = painterResource(id = R.drawable.a123), // Use drawable icon
                        contentDescription = "Upload Photo",
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(x = 50.dp)
                            .align(Alignment.Center)
                            .clickable { // Make the icon clickable
                                launcher.launch("image/*") // Launch file picker when the icon is clicked
                            },
                        tint = Color.Unspecified // No color tint for the icon
                    )
                }
            }

        }



        Spacer(modifier = Modifier.height(16.dp))

        // Save and Cancel Buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = { /* Handle Save */ }) {
                Text("Sauvegarder")
            }
            Spacer(modifier = Modifier.width(8.dp))

        }

        Spacer(modifier = Modifier.height(16.dp))

        // Add a Logout button

    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(onLogout = { /* Handle Logout Action */ })
}
