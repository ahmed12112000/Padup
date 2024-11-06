package com.nevaDev.padeliummarhaba.ui.views

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nevaDev.padeliummarhaba.models.ReservationOption
import java.time.LocalDate
import androidx.compose.material.*
import androidx.compose.material.icons.filled.Money
import androidx.compose.material.icons.filled.Payment
import androidx.compose.ui.tooling.preview.Preview
@OptIn(ExperimentalMaterialApi::class)

@Composable
fun PaymentSection1(
    selectedDate: LocalDate,
    selectedTimeSlot: String,
    selectedReservation: ReservationOption,
    onExtrasUpdate: (Int, Int, Boolean) -> Unit, // Callback parameter
    onPayWithCardClick: () -> Unit, // Callback for "Payer avec Carte"

) {
    var showPayScreen by remember { mutableStateOf(false) } // State to toggle PayScreen visibility

    var additionalExtrasEnabled by remember { mutableStateOf(false) } // For the second switch

    var partnerName by remember { mutableStateOf("") }
    // State for dropdown visibility
    var expanded by remember { mutableStateOf(false) }
    // State for selected parts
    var selectedParts by remember { mutableStateOf("1") }
    // List of options for parts
    val options = listOf("1", "2", "3", "4")
    // State variables to store user input
    var phoneNumber by remember { mutableStateOf("") }
    var payParts by remember { mutableStateOf("1") }
    var payTotal by remember { mutableStateOf(false) } // Toggle between parts or total
    var extrasEnabled by remember { mutableStateOf(false) }
    var selectedRaquette by remember { mutableStateOf(1) }
    var includeBalls by remember { mutableStateOf(false) }

    // Calculate extras cost
    val extrasCost = (if (includeBalls) 5 else 0) + (selectedRaquette * 2)
    onExtrasUpdate(extrasCost, selectedRaquette, includeBalls)


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Phone Number Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Requis pour votre réservation", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(18.dp))

                HorizontalDivider(modifier = Modifier .width(900.dp).padding(horizontal = 10.dp).offset(y = -10.dp),
                    color = Color.Gray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "    Numéro de téléphone", fontSize = 15.sp,fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(18.dp))

                HorizontalDivider(modifier = Modifier .width(900.dp).padding(horizontal = 10.dp).offset(y = -10.dp),
                    color = Color.Gray, thickness = 1.dp)
                Spacer(modifier = Modifier.height(8.dp))
                // Phone number input with pre-filled number and "Modifier" button
                Row(verticalAlignment = Alignment.CenterVertically) {
                    BasicTextField(
                        value = phoneNumber,
                        onValueChange = {
                            // Allow only numbers and limit to 8 digits
                            if (it.all { char -> char.isDigit() } && it.length <= 8) {
                                phoneNumber = it

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
                                androidx.compose.material.Text(
                                    text = "|",
                                    fontSize = 29.sp,
                                    color = Color.Black,
                                    modifier = Modifier.offset(x = -8.dp, y = -2.dp)
                                )
                                // Inner TextField
                                Box(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    innerTextField()
                                }

                                // Success Icon

                            }
                        }
                    )

                    Spacer(modifier = Modifier.width(10.dp)) // Add some space between the fields

                    // Button to validate the phone number

                    Button(
                        onClick = {
                            // Handle button click
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .border(1.dp, Color(0xFF0054D8), RoundedCornerShape(13.dp)),
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        shape = RoundedCornerShape(15.dp) // Set button background to transparent
                    ) {
                        Box(
                            modifier = Modifier
                                .background(Color.White, RoundedCornerShape(10.dp)) // White background for the box
                                .fillMaxSize() // Fill the button size
                        ) {
                            Text(
                                text = "Modifier",
                                color = Color(0xFF0054D8), // Text color
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                modifier = Modifier.align(Alignment.Center) // Center the text in the box
                            )
                        }
                    }

                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Payment options for parts or total
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "    Je veux payer pour", fontSize = 15.sp,fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(18.dp))

                HorizontalDivider(modifier = Modifier .width(900.dp).padding(horizontal = 10.dp).offset(y = -10.dp),
                    color = Color.Gray, thickness = 1.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Dropdown for number of parts
                    ExposedDropdownMenuBox(
                        expanded = expanded,
                        onExpandedChange = { expanded = !expanded } // Toggle dropdown visibility
                    ) {
                        TextField(
                            value = selectedParts,
                            onValueChange = { selectedParts = it }, // Not used as it's read-only
                            readOnly = true, // Make it read-only to ensure dropdown selection
                            modifier = Modifier
                                .width(50.dp)
                                .border(1.dp, Color.Unspecified, RoundedCornerShape(13.dp)), // Custom border
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White, // Set background color
                                cursorColor = Color.Transparent, // Hide cursor color if needed
                                focusedIndicatorColor = Color.Transparent, // Hide underline when focused
                                unfocusedIndicatorColor = Color.Transparent // Hide underline when not focused
                            ),
                            placeholder = { Text("Select parts") } // Placeholder text
                        )

                        ExposedDropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false } // Close dropdown when dismissed
                        ) {
                            options.forEach { option ->
                                DropdownMenuItem(onClick = {
                                    selectedParts = option // Update selected parts
                                    expanded = false // Close the dropdown
                                }) {
                                    Text(text = option) // Display option text
                                }
                            }
                        }
                    }

                    Text(text = "parts")

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(text = "OU")
                    Row(
                        verticalAlignment = Alignment.CenterVertically, // Center vertically
                        modifier = Modifier.padding(start = 8.dp) // Add some padding if needed
                    ) {
                        Checkbox(
                            checked = payTotal,
                            onCheckedChange = { payTotal = it }, // Handle checkbox state change
                            colors = CheckboxDefaults.colors(checkedColor = Color.Blue) // Set checked color to blue
                        )
                        Spacer(modifier = Modifier.width(4.dp)) // Adjust space between checkbox and label
                        Text(text = "total") // Label for the checkbox
                    }

                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Partner Selection Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "    Sélectionnez votre partenaire", fontSize = 16.sp,fontWeight = FontWeight.Bold)
                    Switch(
                        checked = extrasEnabled,
                        onCheckedChange = { extrasEnabled = it }, // Update state on toggle
                        colors = SwitchDefaults.colors(
                            // Customize the colors directly here
                            checkedThumbColor = Color(0xFF0054D8), // Set thumb color to blue when checked
                            uncheckedThumbColor = Color.Gray, // Set thumb color to gray when unchecked
                            checkedTrackColor = Color(0xFF0054D8).copy(alpha = 0.5f), // Set track color to blue when checked
                            uncheckedTrackColor = Color.LightGray // Set track color to light gray when unchecked
                        )
                    )

                }
                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(modifier = Modifier .width(900.dp).padding(horizontal = 10.dp).offset(y = -10.dp),
                    color = Color.Gray, thickness = 1.dp)

                Text(
                    text = "Votre partenaire doit avoir un compte sur PADELIUM",
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = partnerName,  // Replace with your state variable for text input
                    onValueChange = { partnerName = it }, // Handle partner name input
                    label = { Text("Taper le nom de votre partenaire") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp) // Set height
                        .border(1.dp, Color(0xFF0054D8), RoundedCornerShape(8.dp)), // Optional custom border
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White, // Set background color to white
                       // focusedIndicatorColor = Color.Transparent, // Remove underline when focused
                        //unfocusedIndicatorColor = Color.Transparent // Remove underline when not focused
                    ),
                    shape = RoundedCornerShape(15.dp) // Rounded corners
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Extras Section (Raquette and Balls)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "    Des extras?", fontSize = 16.sp,fontWeight = FontWeight.Bold)
                    Switch(
                        checked = additionalExtrasEnabled ,
                        onCheckedChange = { additionalExtrasEnabled = it }, // Update state on toggle
                        colors = SwitchDefaults.colors(
                            // Customize the colors directly here
                            checkedThumbColor = Color(0xFF0054D8), // Set thumb color to blue when checked
                            uncheckedThumbColor = Color.Gray, // Set thumb color to gray when unchecked
                            checkedTrackColor = Color(0xFF0054D8).copy(alpha = 0.5f), // Set track color to blue when checked
                            uncheckedTrackColor = Color.LightGray // Set track color to light gray when unchecked
                        )
                    )

                }
                Spacer(modifier = Modifier.height(12.dp))

                HorizontalDivider(modifier = Modifier .width(900.dp).padding(horizontal = 10.dp).offset(y = -10.dp),
                    color = Color.Gray, thickness = 1.dp)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        var expanded by remember { mutableStateOf(false) }

                        Box {
                            Button(onClick = { expanded = true },

                                modifier = Modifier
                                    //.fillMaxWidth()
                                    //.height(48.dp)
                                    .border(1.dp, Color.Unspecified, RoundedCornerShape(13.dp)),
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                                shape = RoundedCornerShape(1.dp))
                            {
                                Text(text = selectedRaquette.toString())
                            }
                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                (1..4).forEach { number ->
                                    DropdownMenuItem(
                                        text = { Text(text = number.toString()) },
                                        onClick = {
                                            selectedRaquette = number
                                            expanded = false
                                            onExtrasUpdate(extrasCost, selectedRaquette, includeBalls)
                                        }
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Raquette")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = includeBalls,
                            onCheckedChange = { includeBalls = it }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "3 New Balls")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Reservation Summary Section
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            ReservationSummary(
                selectedDate = selectedDate,
                selectedTimeSlot = selectedTimeSlot,
                selectedReservation = selectedReservation,
                extrasCost = extrasCost,
                selectedRaquette = selectedRaquette.toString(),
                includeBalls = includeBalls,
                onTotalAmountCalculated = { total ->
                    // Handle the total amount calculation here if needed
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Payment buttons
        Row(
            modifier = Modifier.fillMaxWidth().offset(x=-10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { onPayWithCardClick() },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White), // White background
                border = BorderStroke(2.dp, Color(0xFF0054D8)) // Blue border
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Payment, // Replace with your desired icon
                        contentDescription = "Card Payment",
                        tint = Color(0xFF0054D8) // Set icon color to blue
                    )
                    Spacer(modifier = Modifier.width(4.dp)) // Space between icon and text
                    Text(
                        text = "Card Crédit",
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray // Set text color to blue
                    )
                }
            }

            Button(
                onClick = { /* Handle credits payment */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White), // White background
                border = BorderStroke(2.dp, Color(0xFF0054D8)) // Blue border
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Money, // Replace with your desired icon
                        contentDescription = "Credits Payment",
                        tint = Color(0xFF0054D8) // Set icon color to blue
                    )
                    Spacer(modifier = Modifier.width(4.dp)) // Space between icon and text
                    Text(
                        text = "Crédit Padelium",
                        fontWeight = FontWeight.Bold,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}
fun calculateTotalAmount(): Int {
    // Your logic to calculate the total amount based on selected options
    return 100 // Example static value, replace this with actual calculation
}


@Preview(showBackground = true)
@Composable
fun PaymentSection1Preview() {
    // Mock data to use in the preview
    val mockSelectedDate = LocalDate.now()
    val mockSelectedTimeSlot = "10:00 AM - 11:00 AM"
    val mockSelectedReservation = ReservationOption(
        name = "Tennis Court",
        price = 50.toString(),
        time = "10:00 AM", // Provide a mock value for time
        duration = "1 hour" // Provide a mock value for duration
    )

    PaymentSection1(
        selectedDate = mockSelectedDate,
        selectedTimeSlot = mockSelectedTimeSlot,
        selectedReservation = mockSelectedReservation,
        onExtrasUpdate = { extrasCost, selectedRaquette, includeBalls ->
            // Handle extras update in the preview
        },
        onPayWithCardClick = {
            // Handle pay with card click in the preview
        },

    )
}