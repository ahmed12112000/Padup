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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration.Companion.Underline
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nevaDev.padeliummarhaba.di.SessionManager
import com.nevaDev.padeliummarhaba.ui.activities.MainActivity
import com.nevaDev.padeliummarhaba.viewmodels.BalanceViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.padelium.domain.dto.LoginRequest
import com.nevaDev.padeliummarhaba.viewmodels.UserViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.Resulta
import com.padelium.domain.dto.GetProfileResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

sealed interface LoginState {
    object Idle : LoginState
    object Success : LoginState
    object ProfileFetching : LoginState
    object Completed : LoginState
}
@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: UserViewModel = hiltViewModel(),
    getProfileViewModel: GetProfileViewModel = hiltViewModel(),
    navController: NavController,
    loginRequest: LoginRequest,
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
    val focusManager = LocalFocusManager.current
    val sessionManager = remember { SessionManager.getInstance(context) }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val backStackEntry = navController.currentBackStackEntry
    val destinationRoute = backStackEntry?.arguments?.getString("destination") ?: "main_screen"
    val redirectUrl = backStackEntry?.arguments?.getString("redirectUrl")

    val profileData by getProfileViewModel.profileData.observeAsState()
    val profileResult = getProfileViewModel.profileData.observeAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    var isProfileFetched by remember { mutableStateOf(false) }
    var isLoginSuccessful by remember { mutableStateOf(false) } // Flag to track login success
    var loginState by remember { mutableStateOf<LoginState>(LoginState.Idle) }
    var pendingToken: String? by remember { mutableStateOf(null) }

    viewModel.dataResult1.observe(lifecycleOwner) { result ->
        isLoading = false
        when (result) {
            is Resulta.Loading -> isLoading = true
            is Resulta.Success -> {
                result.data?.let { tokenResponse ->
                    pendingToken = tokenResponse.toString()
                    loginState = LoginState.Success
                    Log.d("LoginFlow", "Token received, waiting for profile check...")
                }
            }
            is Resulta.Failure -> {
                errorMessage = "Nom d'utilisateur ou Mot de passe invalide"
            }
            else -> Unit
        }
    }

    LaunchedEffect(loginState) {
        if (loginState == LoginState.Success) {
            getProfileViewModel.fetchProfileData()
            loginState = LoginState.ProfileFetching
        }
    }

    LaunchedEffect(profileResult.value) {
        when (val result = profileResult.value) {
            is DataResult.Success -> {
                val profile = result.data as? GetProfileResponse
                if (profile != null) {
                    val isAdmin = listOf("ROLE_ADMIN", "ROLE_MANAGER").any { role -> role in profile.authorities }

                    if (isAdmin) {
                        navController.popBackStack("login_screen", inclusive = true)

                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("http://141.94.246.248/account/login"))
                            .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        context.startActivity(intent)

                        (context as? Activity)?.finish()

                        (context as? Activity)?.finish()
                    } else {
                        pendingToken?.let {
                            val expiresIn = 1800000L
                            sessionManager.saveAuthToken(it, expiresIn)
                            Log.d("SessionToken", "Saved token: $it")
                        }

                        navController.navigate(redirectUrl ?: destinationRoute) {
                            popUpTo("login_screen") { inclusive = true }
                        }


                        loginState = LoginState.Completed
                        onLoginSuccess()
                    }
                }
            }

            is DataResult.Failure -> {
                errorMessage = "Nom d'utilisateur ou Mot de passe invalide"
                loginState = LoginState.Idle
            }

            else -> Unit
        }
    }




//      ahmedmghaieth9@gmail.com       HibA98821607

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) { focusManager.clearFocus() }
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
                        contentScale = ContentScale.Fit
                    )
                    Image(
                        painter = painterResource(id = R.drawable.a90),
                        contentDescription = "Overlay Image",
                        modifier = Modifier
                            .fillMaxSize()
                            .offset(x = 145.dp, y = 60.dp),
                        contentScale = ContentScale.Fit

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
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide() // Hide keyboard on Done action
                        }
                    ),
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


                Spacer(modifier = Modifier.height(8.dp))


                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it.trim()
                        isPasswordError = password.length < 8
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide() // ✅ Now resolved
                        }
                    ),
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



                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {

                    Text(
                        text = stringResource(R.string.forgot_password),
                        color = Color(0xFF0054D8),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { }
                    )
                }

                Spacer(modifier = Modifier.height(26.dp))
                val isEmailValid = email.matches(emailPattern)
                val isPasswordValid = password.length >= 8
                val isButtonEnabled =
                    isEmailValid && isPasswordValid && email.isNotBlank() && password.isNotBlank()

                Button(
                    onClick = {
                        coroutineScope.launch {
                            isLoading = true
                            val updatedRequest = loginRequest.copy(username = email, password = password)

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

                Spacer(modifier = Modifier.height(16.dp))

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

                    }
                    Spacer(modifier = Modifier.height(1.dp))
                    Spacer(modifier = Modifier.height(20.dp))
                    Row {
                        Text(
                            text = stringResource(R.string.signinbutoon),
                            color = Color.Gray
                        )
                        Spacer(modifier = Modifier.width(8.dp))
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



