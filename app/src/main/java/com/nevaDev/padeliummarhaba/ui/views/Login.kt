package com.nevaDev.padeliummarhaba.ui.views

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import com.nevaDev.padeliummarhaba.ui.activities.MainActivity
import com.nevaDev.padeliummarhaba.ui.activities.SharedViewModel
import com.nevaDev.padeliummarhaba.viewmodels.BalanceViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.padelium.domain.dto.LoginRequest
import com.nevaDev.padeliummarhaba.viewmodels.UserViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.Resulta
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: UserViewModel = hiltViewModel(),
    getProfileViewModel: GetProfileViewModel = hiltViewModel(),
    navController: NavController,
    loginRequest: LoginRequest,
    destinationRoute: String,
    sharedViewModel: SharedViewModel  // Access SharedViewModel

) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val balanceViewModel: BalanceViewModel = hiltViewModel()
    val emailPattern = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$".toRegex()
    var email by remember { mutableStateOf(loginRequest.username) }
    var password by remember { mutableStateOf(loginRequest.password) }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val focusManager = LocalFocusManager.current // Focus manager to clear focus
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val screenHeight = configuration.screenHeightDp

    viewModel.dataResult1.observe(lifecycleOwner) { result ->
        isLoading = false
        when (result) {
            is Resulta.Loading -> {
                isLoading = true
            }

            is Resulta.Success -> {
                if (result.data != null) {
                    Log.d("LoginResponse", "Success: User logged in successfully")

                    // Fetch profile data after successful login
                    getProfileViewModel.fetchProfileData()
                    navController.navigate(destinationRoute) {
                        popUpTo("login_screen") { inclusive = true }
                    }
                } else {
                    Toast.makeText(
                        context,
                        "Unexpected empty response from server",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            is Resulta.Failure -> {
                Log.e("LoginResponse", "Error: ${result.errorMessage} | Code: ${result.statusCode}")
                Toast.makeText(context, "Error: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Observe the profile data to check for user role
    getProfileViewModel.hasUserRole.observe(lifecycleOwner) { hasRole ->
        if (hasRole) {
            // User has ROLE_USER, proceed to main screen
            balanceViewModel.fetchAndBalance()
            sharedViewModel.setLoggedIn(true) // Persist login state

            val intent = Intent(context, MainActivity::class.java).apply {
                putExtra("navigate_to", destinationRoute)
            }
            context.startActivity(intent)
            (context as Activity).finish() // Close LoginActivity

        } else {
            // User does not have the correct role, redirect to login page
            val intent =
                Intent(Intent.ACTION_VIEW, Uri.parse("http://141.94.246.248/account/login"))
            context.startActivity(intent)
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { focusManager.clearFocus() } // Clear focus on outside tap
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.padeliuum),
                    contentDescription = "Base Image",
                    modifier = Modifier
                        .fillMaxSize(),
                    //  .padding(end = 20.dp),
                    contentScale = ContentScale.Fit
                )
                Image(
                    painter = painterResource(id = R.drawable.a90),
                    contentDescription = "Overlay Image",
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(x = 140.dp, y = 60.dp),
                    //.height(200.dp)
                    //   .border(2.dp, Color.Unspecified),
                    contentScale = ContentScale.Fit // Adjust as needed

                )
            }

            Spacer(modifier = Modifier.height(66.dp))

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
                        painter = painterResource(id = R.drawable.mail),
                        contentDescription = "E-mail Icon",
                        tint = Color.Unspecified,
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(13.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                ),
                isError = isEmailError
            )
/*
            if (isEmailError) {
                Text(
                    text = stringResource(R.string.email_error),
                    color = Color.Red,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

 */

            Spacer(modifier = Modifier.height(8.dp))


            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it.trim()
                    isPasswordError = password.length < 8
                },
                label = { Text(stringResource(R.string.password)) },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.password),
                        contentDescription = "Password Icon",
                        modifier = Modifier.size(24.dp)
                    )
                },
                trailingIcon = {
                    val iconRes =
                        if (passwordVisible) R.drawable.showpassword else R.drawable.hidepassword
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            painter = painterResource(id = iconRes),
                            contentDescription = if (passwordVisible) "Hide Password" else "Show Password",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(13.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color.Gray,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color.Black,
                ),
                isError = isPasswordError
            )
            /*
            if (isPasswordError) {
                Text(
                    text = stringResource(R.string.password_error),
                    color = Color.Red,
                    style = MaterialTheme.typography.body2,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }

             */

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                /*
            var checked by remember { mutableStateOf(false) }
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = it },
                modifier = Modifier.size(11.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.primary,
                    uncheckedColor = Color.Gray,
                    checkmarkColor = Color.White
                )
            )

            Text(
                text = "Rester-Connecté !",
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                fontStyle = FontStyle.Normal,
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
            )

             */
                Text(
                    text = stringResource(R.string.forgot_password),
                    color = Color(0xFF0054D8),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.clickable { }
                )
            }

            Spacer(modifier = Modifier.height(36.dp))
            val isEmailValid = email.matches(emailPattern)
            val isPasswordValid = password.length >= 8
            val isButtonEnabled = isEmailValid && isPasswordValid && email.isNotBlank() && password.isNotBlank()

            Button(
                onClick = {
                    coroutineScope.launch {
                        isLoading = true
                        val updatedRequest = loginRequest.copy(
                            username = email,
                            password = password,
                        )
                        viewModel.loginUser(updatedRequest)


                    }
                },
                enabled = !isLoading && isButtonEnabled,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0054D8)),
                shape = RoundedCornerShape(13.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
                } else {
                    Text(
                        text = stringResource(R.string.login_button),
                        color = Color.White,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }

            errorMessage?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
            }

            Spacer(modifier = Modifier.height(36.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Divider(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp),
                        color = Color(android.graphics.Color.parseColor("#999999")),
                        thickness = 1.dp
                    )
                    /*
                    Text(
                        text = "OU",
                        modifier = Modifier.padding(horizontal = 8.dp),
                        color = Color(android.graphics.Color.parseColor("#999999")),

                        fontSize = 15.sp
                    )

                    Divider(
                        modifier = Modifier
                            .weight(1f)
                            .padding(vertical = 8.dp),
                        color = Color(android.graphics.Color.parseColor("#999999")),
                        thickness = 1.dp
                    )
                    text = stringResource(R.string.signinredirection),

                     */
                }
                Spacer(modifier = Modifier.height(1.dp))
                Spacer(modifier = Modifier.height(60.dp))
                Row {
                    Text(
                        text = stringResource(R.string.signinbutoon),
                        color = Color.Gray)
                    Text(
                        text = stringResource(R.string.signinredirection),
                        color = Color.Black,
                        modifier = Modifier.clickable { navController.navigate("signup_screen") },
                        textDecoration = Underline
                    )
                }
            }
        }
    }
}


