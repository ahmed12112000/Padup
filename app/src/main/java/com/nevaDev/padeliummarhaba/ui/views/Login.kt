package com.nevaDev.padeliummarhaba.ui.views
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.padelium.domain.dto.LoginRequest
import com.nevaDev.padeliummarhaba.viewmodels.UserViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.DataResult

@Composable
fun LoginScreen(
        onLoginSuccess: () -> Unit,
        viewModel : UserViewModel = hiltViewModel()
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    // Initialize RetrofitClient with context

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current

    viewModel.dataResult.observe(lifecycleOwner) { result ->
        isLoading = false

        when (result) {
            is DataResult.Loading -> {
                Log.e("TAG", "Loading")
            }
            is DataResult.Success -> {
                Log.e("TAG", "Success")
                onLoginSuccess() // Navigate on success
            }
            is DataResult.Failure -> {
                isLoading = false
                Log.e("TAG", "Failure - Error Code: ${result.exception},${result.errorCode}, Message: ${result.errorMessage}")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo and Background Images (kept same as original)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.a2),
                contentDescription = "Overlay Image",
                modifier = Modifier
                    .size(2000.dp)
                    .offset(x = 15.dp, y = 52.dp)
                    .border(2.dp, Color.Unspecified)
            )
            Image(
                painter = painterResource(id = R.drawable.padelium),
                contentDescription = "Base Image",
                modifier = Modifier
                    .fillMaxSize()
                    .padding(end = 20.dp),
                contentScale = ContentScale.Fit
            )
        }

        Image(
            painter = painterResource(id = R.drawable.a3),
            contentDescription = "Logo",
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = -18.dp)
                .height(60.dp)
                .padding(end = 20.dp, top = 10.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Username/Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text(context.getString(R.string.login)) },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon") },
            trailingIcon = {
                val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, contentDescription = "Toggle Password Visibility")
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Remember Me & Forgot Password Row (same as original)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var checked by remember { mutableStateOf(false) }
            Checkbox(
                checked = checked,
                onCheckedChange = { checked = it },
                modifier = Modifier.size(24.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colors.primary,
                    uncheckedColor = Color.Gray,
                    checkmarkColor = Color.White
                )
            )
            Text(
                text = "Remember Me !",
                color = Color.Gray,
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
            )
            Text(
                text = "Mot de passe oublié ?",
                color = Color.Red,
                modifier = Modifier.clickable { /* Handle forgot password click */ }
            )
        }

        Spacer(modifier = Modifier.height(36.dp))

        // Login Button with Loading State
        Button(
            onClick = {

                Log.e("TAG","onclick")
                isLoading = true
                val loginRequest = LoginRequest(email, password)
                viewModel.loginUser(loginRequest)
            },
            modifier = Modifier.fillMaxWidth().height(48.dp),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
            shape = RoundedCornerShape(24.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
            } else {
                Text(text = "Se connecter", color = Color.White, fontSize = 25.sp)
            }
        }

        // Show error message if login fails
        errorMessage?.let {
            Text(text = it, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }

        Spacer(modifier = Modifier.height(36.dp))

        // OR Divider
        Column(
            modifier = Modifier.fillMaxWidth(), // Ensure the Column takes up the full width
            horizontalAlignment = Alignment.CenterHorizontally // Center content horizontally
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
            }
            /*
                        Text(
                            text = "Utiliser votre profil social pour se connecter",
                            modifier = Modifier.padding(horizontal = 2.dp, vertical = 17.dp),
                            color = Color(android.graphics.Color.parseColor("#999999")),

                            fontSize = 13.sp
                        )*/
        }


        Spacer(modifier = Modifier.height(1.dp))
        /*
                // Social Login Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                            .weight(1f)
                            .padding(start = 3.dp), // Padding between buttons
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
                        contentPadding = PaddingValues(1.dp) // Remove default padding if needed
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxWidth() // Ensure Row takes up full width
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
                                modifier = Modifier.fillMaxWidth() // Ensure text takes up full width of the button
                            )
                        }

                    }
                }*/

        Spacer(modifier = Modifier.height(60.dp))

        // Sign Up Link
        Row {
            Text(text = "vous n'avez pas de compte ?", color = Color.Gray)
            Text(
                text = "S'inscrire",
                color = Color.Black,
                modifier = Modifier.clickable { /* Handle sign up click */ },
                textDecoration = Underline
            )
        }

    }
    Box(
        modifier = Modifier.fillMaxSize() // Ensure the Box takes up the full available space
    ) {
        Image(
            painter = painterResource(id = R.drawable.bottom), // Replace with your drawable resource ID
            contentDescription = "Bottom Icon",
            contentScale = ContentScale.FillWidth, // Adjust content scale as needed
            modifier = Modifier
                .size(5000.dp) // Make the image as wide as the parent
                .height(200.dp) // Set a fixed height (adjust as needed)
                .padding(bottom = 36.dp) // Padding to push the image up from the bottom edge
                .offset( y = 435 .dp) // Adjust the horizontal and vertical position as needed
        )
    }
}

