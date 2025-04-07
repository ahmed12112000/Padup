package com.nevaDev.padeliummarhaba.ui.activities


import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.*
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.pager.ExperimentalPagerApi
import java.time.LocalDate
import com.nevaDev.padeliummarhaba.ui.views.SplashScreen
import com.nevaDev.padeliummarhaba.ui.theme.PadeliumMarhabaTheme
import com.nevadev.padeliummarhaba.R
import dagger.hilt.android.AndroidEntryPoint
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.nevaDev.padeliummarhaba.di.SessionManager
import com.nevaDev.padeliummarhaba.ui.views.CopyrightText
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetReservationViewModel
import com.nevaDev.padeliummarhaba.viewmodels.KeyViewModel
import com.padelium.domain.dataresult.DataResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.icons.filled.SignalWifiOff
import androidx.compose.ui.draw.scale
import com.nevaDev.padeliummarhaba.di.NetworkUtil
import androidx.compose.ui.text.TextStyle
import androidx.compose.material.Button

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private lateinit var networkUtil: NetworkUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        networkUtil = NetworkUtil(this)

        setContent {
            PadeliumMarhabaTheme {
                var showSplashScreen by remember { mutableStateOf(true) }
                var isNetworkAvailable by remember { mutableStateOf(true) }
                val navController = rememberNavController()
                val viewModel: GetProfileViewModel = hiltViewModel()
                val context = LocalContext.current
                val sessionManager = remember { SessionManager(context) }
                val sharedPreferences =
                    remember { context.getSharedPreferences("csrf_prefs", MODE_PRIVATE) }

                val isLoggedIn by sessionManager.isLoggedInFlow.collectAsState()

                LaunchedEffect(Unit) {
                    networkUtil.registerNetworkCallback { isConnected ->
                        isNetworkAvailable = isConnected // Update network status
                        if (!isNetworkAvailable) {
                            Toast.makeText(context, "Internet disconnected", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                LaunchedEffect(Unit) {
                    delay(3000)
                    showSplashScreen = false
                }

                if (showSplashScreen) {
                    SplashScreen()
                } else {
                    val onLogout: () -> Unit = {
                        sharedPreferences.edit().clear().apply()
                        sessionManager.logout()
                        navController.navigate("login") {
                            popUpTo("home") { inclusive = true }
                        }
                    }

                    MainApp(
                        context = context,
                        sharedPreferences = sharedPreferences,
                        viewModel = viewModel,
                        navController = navController,
                        isNetworkAvailable = isNetworkAvailable
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        networkUtil.unregister()
    }
}

@Composable
fun MainApp(
    context: Context,
    sharedPreferences: SharedPreferences,
    viewModel: GetProfileViewModel,
    navController: NavHostController,
    isNetworkAvailable: Boolean
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val getReservationViewModel: GetReservationViewModel = hiltViewModel()
    val sessionManager = remember { SessionManager(context) }

    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedItem = currentBackStackEntry?.destination?.route
    val shouldShowBottomBar = selectedItem !in listOf("splash_screen", "login_screen")

    val interactionSource = remember { MutableInteractionSource() }
    val disabledModifier = if (!isNetworkAvailable) Modifier.clickable(
        interactionSource = interactionSource,
        indication = null // Disable visual feedback
    ) {} else Modifier

    // Track network connection status actively
    val showDialog = remember { mutableStateOf(!isNetworkAvailable) }
    val insets = WindowInsets

    LaunchedEffect(isNetworkAvailable) {
        if (!isNetworkAvailable) {
            showDialog.value = true // Show dialog when internet is disconnected
        } else {
            showDialog.value = false // Hide dialog when internet is available
        }
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                AnimatedBottomBar(
                    navController = navController,
                    getReservationViewModel = getReservationViewModel

                )
            }
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)
            ) {
                if (isNetworkAvailable) {
                    AppNavHost(
                        navController = navController,
                        isUserLoggedIn = sessionManager.isLoggedIn(),
                        onLoginSuccess = { },
                        onLogout = { sessionManager.logout() },
                        context = context,
                        sharedPreferences = sharedPreferences,
                        drawerState = drawerState,
                        scope = scope,
                        onSignupSuccess = { }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .clickable(interactionSource = interactionSource, indication = null) {}
                    )
                }
            }
        }
    )

    // Dialog for no internet connection with Try Again functionality
    if (showDialog.value) {
        AlertDialog(
            onDismissRequest = { showDialog.value = true }, // Keep dialog open on dismiss
            title = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SignalWifiOff,
                        contentDescription = "No Internet",
                        tint = Color.Red,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Aucune connexion internet",
                        style = TextStyle(fontSize = 18.sp, color = Color.Black)
                    )
                }
            },
            text = {
                Text("Veuillez vérifier votre connexion Internet et réessayer.")
            },
            buttons = {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = {
                            // Check if internet is restored after clicking the retry button
                            if (isNetworkAvailable) {
                                showDialog.value = false // Hide the dialog if connected
                            } else {
                                showDialog.value = true // Keep the dialog open if still disconnected
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor =  Color(0xFF0054D8)
                        ),
                        modifier = Modifier.fillMaxWidth(0.8f)
                    ) {
                        Text("Réessayez", color = Color.White)
                    }
                }
            }
        )
    }
}











@Composable
fun TopBar(
    navController: NavController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {



        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
              //  .padding(top = 3.dp)
                .background(Color(0xFF0054D8))

        ) {


                Icon(
                    painter = painterResource(id = R.drawable.logopadelium),
                    contentDescription = "Right Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .fillMaxSize(0.6f)
                        .clip(CircleShape)
                        .clickable {
                            navController.navigate("main_screen")
                        }
                        .align(Alignment.CenterEnd)

                )



    }
}
@Preview(showBackground = true, name = "TopBar Preview")
@Composable
fun TopBarPreview() {
    // Mocking parameters for the preview
    val navController = rememberNavController() // Mock NavController
    val drawerState = rememberDrawerState(DrawerValue.Closed) // Mock DrawerState
    val scope = rememberCoroutineScope() // Mock CoroutineScope

    // Call the TopBar composable with mock data
    TopBar(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    )
}
fun logout(context: Context) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
}

@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onReservationClicked: (LocalDate) -> Unit,
    viewModel: KeyViewModel = hiltViewModel(),
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var isButtonClicked by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val isConnected = remember { mutableStateOf(checkInternetConnection(context)) }

    // Re-check internet connection periodically
    LaunchedEffect(Unit) {
        while (true) {
            isConnected.value = checkInternetConnection(context)
            delay(5000) // Check every 5 seconds
        }
    }

    if (isLoading) {
        CircularProgressIndicator()
    }

    val images = listOf(R.drawable.a, R.drawable.b, R.drawable.c)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = -4.dp)
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .background(Color(0xFF0054D8))
                .height(150.dp)
        ) {}

        Button(
            onClick = {
                if (isConnected.value) {
                    if (!isButtonClicked) {
                        isButtonClicked = true
                        selectedDate = LocalDate.now()
                        selectedDate?.let { date ->
                            isLoading = true
                            onReservationClicked(date)
                        }
                    }
                } else {
                    showToastForFiveSeconds(context, "Please verify your internet connection")
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor =  Color(0xFFD7F057)
            ),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .offset(x = 100.dp, y = -35.dp)
                .border(0.5.dp, Color(0xFFD7F057), RoundedCornerShape(12.dp))
                .height(50.dp),
            shape = RoundedCornerShape(12.dp),
       //     enabled = isConnected.value // Disable button if no internet
        ) {
            Text(
                text = "Réserver",
                color = Color(0xFF0054D8) ,
                fontSize = 23.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, end = 16.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.pictures_gallerie),
                contentDescription = "Logo",
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "|",
                color = Color(0xFF0054D8),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Photos",
                color = Color(0xFF0054D8),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )
        }

        ImageCarousel(
            images = images,
            modifier = Modifier
                .padding(top = 30.dp)
                .fillMaxWidth()
                .height(335.dp)
        )
    }
}
// Function to check internet connection
fun checkInternetConnection(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivityManager.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

// Function to display a Toast for exactly 5 seconds
fun showToastForFiveSeconds(context: Context, message: String) {
    val handler = Handler(Looper.getMainLooper())
    val toast = Toast.makeText(context, message, Toast.LENGTH_SHORT)

    // Show the toast multiple times for a total of 5 seconds
    val repeatCount = 3 // Approximate (each short toast ~1 second)
    var count = 0

    val runnable = object : Runnable {
        override fun run() {
            if (count < repeatCount) {
                toast.show()
                count++
                handler.postDelayed(this, 1000) // Re-show the toast every second
            }
        }
    }
    handler.post(runnable)
}


@OptIn(ExperimentalPagerApi::class)
@Composable
fun ImageCarousel(images: List<Int>, modifier: Modifier = Modifier) {
    val pagerState = rememberPagerState()

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(350.dp)
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            count = images.size,
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
        ) { page ->
            Image(
                painter = painterResource(id = images[page]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.White)
                    .padding(5.dp)
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp).offset(y = -71.dp),
            horizontalArrangement = Arrangement.spacedBy(13.dp)
        ) {
            images.forEachIndexed { index, _ ->
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(if (index == pagerState.currentPage) Color(0xFF0054D8) else Color.White)
                )
            }
        }
    }
}






@Composable
fun AnimatedBottomBar(
    navController: NavController,
    getReservationViewModel: GetReservationViewModel,
    modifier: Modifier = Modifier,
) {
    val selectedItem by rememberUpdatedState(navController.currentBackStackEntry?.destination?.route)
    val reservationsData by getReservationViewModel.ReservationsData.observeAsState(DataResult.Loading)
    val context = LocalContext.current
    val sessionManager = remember { SessionManager(context) }
    val isUserLoggedIn by sessionManager.isLoggedInFlow.collectAsState()

    // Fixed offsets for different devices
    val selectedItemOffset = remember { mutableStateOf(0.dp) }

    // List of items
    val navItems = listOf(
        NavItem("main_screen", Icons.Filled.Home, "Accueil"),
        NavItem("summary_screen", Icons.Filled.CalendarMonth, "Mes Réservations"),
        NavItem("CreditPayment", Icons.Filled.CreditCard, "Mes crédits"),
        NavItem("Profile_screen", Icons.Filled.Person, "Profil")
    )

    // Bottom Navigation Bar
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .background(
                color = Color(0xFF0054D8), // Blue background color
                shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp) // Rounded top corners
            )
            .padding(horizontal = 10.dp) // Prevent content from stretching too much
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            navItems.forEach { item ->
                CustomBottomNavItem(
                    navController = navController,
                    route = item.route,
                    icon = item.icon,
                    label = item.label,
                    isSelected = selectedItem == item.route
                )
            }
        }
    }
}

@Composable
fun CustomBottomNavItem(
    navController: NavController,
    route: String,
    icon: ImageVector,
    label: String,
    isSelected: Boolean
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionManager.getInstance(context) }
    val isUserLoggedIn by sessionManager.isLoggedInFlow.collectAsState()
    val isConnected = remember { mutableStateOf(checkInternetConnection(context)) }

    // Re-check internet connection periodically
    LaunchedEffect(Unit) {
        while (true) {
            isConnected.value = checkInternetConnection(context)
            delay(5000) // Check every 5 seconds
        }
    }
    val restrictedRoutes = listOf("Profile_screen", "CreditPayment", "summary_screen")

    // Animation for scaling effect
    val animatedScale by animateFloatAsState(
        targetValue = if (isSelected) 1.15f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
    )

    // Animation for vertical movement
    val animatedOffsetY by animateDpAsState(
        targetValue = if (isSelected) (-15).dp else 0.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFFD7F057) else Color.Transparent,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    )

    val iconColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF0054D8) else Color.White,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    )

    val textColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFFD7F057) else Color.White,
        animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clickable {
                if (!isConnected.value) {
                    showToastForFiveSeconds(context, "No internet connection")
                } else if (route in restrictedRoutes && !sessionManager.isLoggedIn()) {
                    Log.d("Routeeeeeee","restricted route is : $route")
                    if (!sessionManager.isSessionValid()) {
                        sessionManager.clearAuthToken() // Clear expired session
                    }
                    navController.navigate("login_screen?redirectUrl=$route") {
                        popUpTo("main_screen") { inclusive = false }
                    }
                } else {
                    navController.navigate(route)
                }

            }
            .offset(y = animatedOffsetY)
            .scale(animatedScale)
    ) {
        // Icon container
        Box(
            modifier = Modifier
                .size(48.dp) // Ensure a standard size across all devices
                .clip(CircleShape)
                .background(backgroundColor)
                .align(Alignment.CenterHorizontally)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier
                    .size(26.dp)
                    .align(Alignment.Center)
            )
        }

        // Label
        Text(
            text = label,
            color = textColor,
            fontSize = 12.sp, // Increase font size slightly for visibility
            fontWeight = FontWeight.Medium,
            modifier = Modifier
                .padding(top = 6.dp) // Ensure spacing from the icon
                .align(Alignment.CenterHorizontally)
        )
    }
}





data class NavItem(val route: String, val icon: ImageVector, val label: String)











@Composable
fun DrawerContent(navController: NavController, onItemSelected: (String) -> Unit, onCloseDrawer: () -> Unit) {

    Column(
        modifier = Modifier
            .offset(x = -40.dp)
            .padding(horizontal = 30.dp)
            .fillMaxSize()
            .background(Color(0xFF0054D8))
    ) {
        IconButton(
            onClick = { onCloseDrawer() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.oip),
                contentDescription = "Close Drawer",
                tint = Color.Unspecified
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logopadelium),
                contentDescription = "Padelium Logo",
                modifier = Modifier.size(260.dp)
                    .offset(y = -90.dp)
            )

            Spacer(modifier = Modifier.height(3.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = -90.dp, x= 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {


                Spacer(modifier = Modifier.width(8.dp))

                Column {


                    Row {
                        Text(
                            text = "Se connecter",
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate("login_screen")
                                }
                        )
                        Text(
                            text = " ou ",
                            fontSize = 18.sp,
                            color = Color.White
                        )
                        Text(
                            text = "S'inscrire",
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier
                                .clickable {
                                    try {
                                        navController.navigate("signup_screen")
                                    } catch (e: Exception) {
                                        Log.e("NavigationError", "Failed to navigate: ${e.message}")
                                    }
                                }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(thickness = 2.dp, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .offset(y = -80.dp, x= 20.dp)
                .weight(1f)
        ) {
            DrawerItem(
                icon = R.drawable.sidebarmenue,
                label = "Accueil",
                onClick = {
                    navController.navigate("main_screen")
                }
            )


            DrawerItem(
                icon = R.drawable.sidebarmenue,
                label = "Plans Tarifaires",
                onClick = {
                    navController.navigate("pricing_screen")
                }
            )

            DrawerItem(
                icon = R.drawable.sidebarmenue,
                label = "Qui sommes nous?",
                onClick = {
                    navController.navigate("how_to_use_screen")
                }
            )

            DrawerItem(
                icon = R.drawable.sidebarmenue,
                label = "CGV",
                onClick = {
                    navController.navigate("about_us_screen")
                }
            )

            DrawerItem(
                icon = R.drawable.sidebarmenue,
                label = "Contactez-nous",
                onClick = {
                    navController.navigate("contact_screen")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = R.drawable.a91),
            contentDescription = "Additional Image",
            modifier = Modifier
                .size(160.dp)
                .align(Alignment.CenterHorizontally)
                .offset(y = -55.dp, x = -141.dp)
        )

        HorizontalDivider(modifier = Modifier .fillMaxWidth().offset(y = -10.dp),
            color = Color.White, thickness = 1.5.dp)

        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = "v1.0.0",
                    fontSize = 10.sp,
                    color = Color.White
                )


                CopyrightText()
            }
        }
    }
}





@Composable
fun DrawerItem(
    icon: Int,
    label: String,
    onClick: () -> Unit,
    abelColor: Color = Color.White
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = label,
            fontSize = 15.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    }

    Divider(color = Color.White, thickness = 1.dp)
}


