package com.nevaDev.padeliummarhaba.ui.views

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.material.TextFieldDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.nevaDev.padeliummarhaba.repository.signup.SignupViewModel
import com.nevaDev.padeliummarhaba.viewmodels.UserViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.LoginRequest
import com.padelium.domain.dto.SignupRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.format.TextStyle

@Composable
fun SignUpScreen(
    navController: NavController,
    onSignupSuccess: () -> Unit,
    viewModel : UserViewModel = hiltViewModel()
) {
    // val viewModel: SignupViewModel = viewModel() // No context needed
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var message by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current // Focus manager to clear focus
    val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    var showMessage by remember { mutableStateOf(false) }

    viewModel.dataResult.observe(lifecycleOwner) { result ->
        isLoading = false
        when (result) {
            is DataResult.Loading -> {
                Log.e("TAG", "Loading")
            }

            is DataResult.Success -> {
                Log.e("TAG", "Success")
                message = "Sign up successful"
                isSuccess = true

                navController.navigate("main_screen")

                // Show toast for 5 seconds
                coroutineScope.launch {
                    Toast.makeText(context, "Compte enrigistré ! Mercie de Vérifier votre email de confirmation.", Toast.LENGTH_LONG).show()
                    delay(5000)
                    message = ""
                }
            }

            is DataResult.Failure -> {
                /*
                isLoading = false
                message = "Error: ${result.errorMessage}"
                isSuccess = false
                Log.e("TAG", "Failure - Error Code:${result.errorCode}, Message: ${result.errorMessage}")

                 */
            }
        }
    }

    if (showMessage) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f)), // Semi-transparent background
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = message,
                color = Color.White,
                fontSize = 18.sp,
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.DarkGray, shape = RoundedCornerShape(8.dp))
                    .padding(16.dp)
            )
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { focusManager.clearFocus() },
            contentAlignment = Alignment.Center

        ) {
            // Overlay Image
            Image(
                painter = painterResource(id = R.drawable.a90),
                contentDescription = "Overlay Image",
                modifier = Modifier
                    .fillMaxSize()
                    .offset(
                        x = 158.dp,
                        y = 40.dp
                    )
                    .border(2.dp, Color.Unspecified)
            )
            Image(
                painter = painterResource(id = R.drawable.padeliuum),
                contentDescription = "Base Image",
                modifier = Modifier
                    .fillMaxSize()
                    // .offset( y = -50.dp)
                    .padding(end = 20.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(1.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = -65.dp, x = 10.dp)
        ) {
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text(stringResource(R.string.firstName)) },
                leadingIcon = {
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp, top = 8.dp)
                    .offset(x = -22.dp, y = -8.dp)
                    .shadow(8.dp, RoundedCornerShape(15.dp), ambientColor = Color.Gray, spotColor = Color.Gray),
                    keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                )

            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text(stringResource(R.string.lastName)) },
                leadingIcon = {
                },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
                    .offset(x = -20.dp)
                    .shadow(4.dp, RoundedCornerShape(15.dp), ambientColor = Color.Gray, spotColor = Color.Gray)
                ,
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                shape = RoundedCornerShape(15.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    backgroundColor = Color.White,
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                )
            )

        }
        Spacer(modifier = Modifier.height(10.dp))
        var isEmailError by remember { mutableStateOf(false) }
        var isPasswordError by remember { mutableStateOf(false) }
        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it.trim()
                isEmailError = !email.matches(emailPattern)
            },
            label = { Text(stringResource(R.string.email)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.email),
                    contentDescription = "Email Icon",
                    modifier = Modifier.size(24.dp)
                )
            },
            modifier = Modifier.fillMaxWidth().offset(y = -75.dp, x = -10.dp)
                .shadow(4.dp, RoundedCornerShape(15.dp), ambientColor = Color.Gray, spotColor = Color.Gray),
            keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White,
                focusedBorderColor = Color.Black,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
            ),
            isError = isEmailError

        )

    }

    /*
    var countryCode by remember { mutableStateOf("+216") } // Country code
    var phoneNumber by remember { mutableStateOf("") } // Phone number

    Row(
        modifier = Modifier
            .fillMaxWidth() // Ensure the Row takes up the full width
            .offset(y = 330.dp, x = 10.dp) // Adjust the vertical position as needed
    ) {
        // Country Code TextField
        OutlinedTextField(
            value = countryCode,
            onValueChange = { countryCode = it },
            label = { Text("") }, // You can leave the label empty or add something like "Country Code"
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.a9), // Replace with actual flag icon
                    contentDescription = "Country Flag",
                    modifier = Modifier.size(25.dp)
                )
            },
            modifier = Modifier
                .weight(1.3f)

                .padding(end = 8.dp, top = 8.dp) // Add spacing between fields
                .offset(x = -6.dp, y = -8.dp)
                .shadow(8.dp, RoundedCornerShape(15.dp), ambientColor = Color.Gray, spotColor = Color.Gray), // Gray shadow
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Next
            ),
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White
            )
        )

        // Phone Number TextField
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = { Text("Numéro de Téléphone") }, // Label for phone number
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone, // You can use an appropriate icon
                    contentDescription = "Phone Icon"
                )
            },
            modifier = Modifier
                .weight(3f)
                .padding(start = 8.dp, end = 20.dp)
                .offset(x = -12.dp)
                .shadow(4.dp, RoundedCornerShape(15.dp), ambientColor = Color.Gray, spotColor = Color.Gray), // Gray shadow
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            shape = RoundedCornerShape(15.dp),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = Color.White
            )
        )

    }*/


    Spacer(modifier = Modifier.height(8.dp))

    var passwordVisible by remember { mutableStateOf(false) }
    var isPasswordError by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = password,
        onValueChange = {
            password = it.trim()
            isPasswordError = password.length < 8
        },
        label = { Text(stringResource(R.string.password)) },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.password1),
                contentDescription = "Password Icon",
                modifier = Modifier.size(24.dp)
            )
        },
        trailingIcon = {
            val iconRes = if (passwordVisible) R.drawable.showpassword else R.drawable.hidepassword
            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                Icon(
                    painter = painterResource(id = iconRes),
                    contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 1.dp, end = 24.dp)
            .offset(x = 5.dp, y = 355.dp)
            .shadow(4.dp, RoundedCornerShape(15.dp), ambientColor = Color.Gray, spotColor = Color.Gray),

        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        shape = RoundedCornerShape(15.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.White,
            focusedBorderColor = Color.Black,
            unfocusedBorderColor = Color.Gray,
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Black,
        ),
        isError = isPasswordError
    )


    Spacer(modifier = Modifier.height(2.dp))

    /*
    // Repeat Password Field
    var repeatPassword by remember { mutableStateOf("") }
    var repeatPasswordVisible by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = repeatPassword,
        onValueChange = { repeatPassword = it },
        label = { Text("Confirm Password") },
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.password1), // Replace 'your_icon' with the actual drawable resource name
                contentDescription = "Repeat Password Icon",
                modifier = Modifier.size(24.dp) // Adjust the size as needed
            )
        },
        trailingIcon = {
            val image = if (repeatPasswordVisible)
                Icons.Filled.Visibility
            else Icons.Filled.VisibilityOff

            IconButton(onClick = { repeatPasswordVisible = !repeatPasswordVisible }) {
                Icon(imageVector = image, contentDescription = "Toggle Repeat Password Visibility")
            }
        },
        visualTransformation = if (repeatPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 1.dp, end = 24.dp)
            .offset(x = 5.dp, y = 420.dp) // Adjust the offset as needed
            .shadow(4.dp, RoundedCornerShape(15.dp), ambientColor = Color.Gray, spotColor = Color.Gray), // Gray shadow
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        shape = RoundedCornerShape(15.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = Color.White // Set the background color to white
        )
    )
    Spacer(modifier = Modifier.height(8.dp))*/
    // State for the checkbox
    var checked by remember { mutableStateOf(false) }
    var showErrorMessage by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .padding(start = 1.dp, end = 24.dp)
            .offset(x = 22.dp, y = 450.dp)
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it
                showErrorMessage = false},
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colors.primary,
                uncheckedColor = Color.Gray,
                checkmarkColor = Color.White
            ),
            modifier = Modifier
                .size(24.dp)
                .clip(RoundedCornerShape(35.dp))
                .shadow(50.dp, RoundedCornerShape(35.dp))
        )

        Spacer(modifier = Modifier.width(1.dp))

        Text(
            text = buildAnnotatedString {
                append("J'accepte ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = Color.Black)) {

                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        textDecoration = Underline,
                        color = Color.Black
                    )
                ) {
                    append("les Conditions d'utilisation")
                }
                withStyle(style = SpanStyle(fontWeight = FontWeight.Normal, color = Color.Black)) {
                    append(" & la ")
                }
                withStyle(
                    style = SpanStyle(
                        fontWeight = FontWeight.Bold,
                        textDecoration = Underline,
                        color = Color.Black
                    )
                ) {
                    append("Politique de confidentalité")
                }
                append(".")
            },
            color = Color.Black,
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 8.dp, vertical = 2.dp)
        )


    }
    val isEmailValid = email.matches(emailPattern)
    val isPasswordValid = password.length >= 8
    val isFirstNameValid = firstName.isNotBlank()
    val isLastNameValid = lastName.isNotBlank()

// Update the button enabled condition to include firstName and lastName checks
    val isButtonEnabled = isEmailValid && isPasswordValid && isFirstNameValid && isLastNameValid
    Button(
        onClick = {
            // Check if any of the fields are empty
            if (firstName.isBlank() || lastName.isBlank() || email.isBlank() || password.isBlank()) {
                Toast.makeText(context, "Vérifier les champs", Toast.LENGTH_SHORT).show()
            } else if (!checked) {
                // If fields are filled but checkbox is not checked
                Toast.makeText(context, "Veuillez accepter les conditions générales et la politique de confidentialité", Toast.LENGTH_SHORT).show()
            } else {
                // Proceed with signup
                isLoading = true
                val signupRequest = SignupRequest(email, password, firstName, lastName)
                viewModel.signupUser (signupRequest)
            }
        },
        enabled = isButtonEnabled && !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .offset(x = 10.dp, y = 520.dp)
            .padding(start = 27.dp, end = 60.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
        shape = RoundedCornerShape(24.dp)
    ) {
        Text(
            text = "Enrigistrement",
            color = Color.White,
            fontSize = 25.sp
        )
    }

    if (message.isNotEmpty() && !isSuccess) {
        Text(
            text = message,
            color = Color.Red,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
    /*
        // OR Divider
        Column(
            modifier = Modifier.fillMaxWidth(), // Ensure the Column takes up the full width
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
                    .offset(x = 10.dp, y = 650.dp) // Adjust the position as needed
                ,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 3.dp, end = 20.dp)
                        .offset(x = -3.dp) // Adjust the position as needed
                    ,
                    color = Color(android.graphics.Color.parseColor("#999999")),
                    thickness = 1.dp
                )
                Text(
                    text = "OU",
                    modifier = Modifier.padding(start =1.dp)
                        .offset(x = -6.dp),
                    color = Color(android.graphics.Color.parseColor("#999999")),

                    fontSize = 15.sp
                )
                Divider(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 12.dp, end = 17.dp),
                    color = Color(android.graphics.Color.parseColor("#999999")),

                    thickness = 1.dp
                )
            }

            Text(
                text = "Utiliser votre profil social pour se connecter",
                modifier = Modifier.padding(horizontal = 2.dp, vertical = 17.dp)
                    .offset(x = 10.dp, y = 650.dp) // Adjust the position as needed
                ,
                color = Color(android.graphics.Color.parseColor("#999999")),

                fontSize = 13.sp
            )
        }


        Spacer(modifier = Modifier.height(1.dp))

        // Social Login Buttons
        Row(
            modifier = Modifier.fillMaxWidth()
                .offset(x = 10.dp, y = 700.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = { /* Handle Facebook login */ },
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 1.dp, end = 3.dp), // Padding between buttons
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF1877F2)),
                contentPadding = PaddingValues(1.dp) // Add padding inside the button if needed
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth() // Ensure Row takes up full width
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.fb), // Use your drawable resource ID here
                        contentDescription = "Facebook Icon",
                        modifier = Modifier
                            .padding(start = 4.dp, end = 2.dp) // Padding between icon and text
                            .size(24.dp) // Adjust icon size if needed
                    )
                    Text(
                        text = "Login with Facebook",
                        color = Color.White,
                        fontSize = 13.sp,
                        modifier = Modifier.weight(1f) // Center text horizontally within Row
                    )
                }
            }


            Button(
                onClick = { /* Handle Google login */ },
                modifier = Modifier
                    .width(200.dp)
                    .weight(1f)
                    .padding(start = 1.dp, end = 2.dp), // Padding between buttons
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                contentPadding = PaddingValues(1.dp) // Remove default padding if needed
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                        .offset(x = -2.dp)// Ensure Row takes up full width
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.google), // Use your drawable resource ID here
                        contentDescription = "Google Icon",
                        modifier = Modifier
                            .padding(end = 8.dp) // Padding between icon and text
                            .size(24.dp) // Adjust icon size if needed
                    )
                    Text(
                        text = "Sign in with Google",
                        color = Color.Black,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center, // Center text horizontally
                        modifier = Modifier.fillMaxWidth()
                            .offset(x = -13.dp)// Ensure text takes up full width of the button
                    )
                }


            }
        }
    */
    Spacer(modifier = Modifier.height(60.dp))
    Row (modifier = Modifier.fillMaxSize()
        .offset(x = 120.dp, y = 600.dp))
    {
        Text(text = stringResource(R.string.logininbutoon), color = Color.Gray)
        Spacer(modifier = Modifier.width(6.dp))

        Text(
            text = stringResource(R.string.loginredirection),
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            textDecoration = Underline,
            modifier = Modifier.clickable {  navController.navigate("login_screen") },
        )
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.bottompadelium),
            contentDescription = "Bottom Icon",
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .size(110.dp)
                .height(200.dp)
                .padding(bottom = 36.dp)
                .offset( y = 615 .dp)
        )
    }
}




