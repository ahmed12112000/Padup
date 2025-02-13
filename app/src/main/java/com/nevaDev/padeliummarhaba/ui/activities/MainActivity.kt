package com.nevaDev.padeliummarhaba.ui.activities


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.material.BottomNavigation
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.pager.ExperimentalPagerApi
import java.time.LocalDate
import com.nevaDev.padeliummarhaba.ui.views.SplashScreen
import com.nevaDev.padeliummarhaba.ui.theme.PadeliumMarhabaTheme
import com.nevadev.padeliummarhaba.R
import dagger.hilt.android.AndroidEntryPoint
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.nevaDev.padeliummarhaba.ui.views.CopyrightText
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetReservationViewModel
import com.padelium.domain.dataresult.DataResult

//   single task single activity..........instanse single one
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("csrf_prefs", MODE_PRIVATE)
        setContent {
            PadeliumMarhabaTheme {
                var showSplashScreen by remember { mutableStateOf(true) }

                LaunchedEffect(Unit) {
                    delay(3100)
                    showSplashScreen = false
                }

                if (showSplashScreen) {
                    SplashScreen()
                } else {
                    val viewModel: GetProfileViewModel = hiltViewModel()
                    val sharedViewModel: SharedViewModel = hiltViewModel()
                    val navController = rememberNavController()

                    val context = LocalContext.current
                    val onLogout: () -> Unit = {
                        // Handle logout logic
                        sharedPreferences.edit().clear().apply()
                    }
                    val isLoggedIn = intent.getBooleanExtra("isLoggedIn", false)
                    sharedViewModel.setLoggedIn(isLoggedIn)

                    // Retrieve the target route from intent
                    val navigateTo = intent.getStringExtra("navigate_to")

                    LaunchedEffect(navigateTo) {
                        if (!navigateTo.isNullOrEmpty()) {
                            navController.navigate(navigateTo)
                        }
                    }

                    MainApp(context, sharedPreferences, viewModel, sharedViewModel, navController)
                }
            }
        }
    }
}

//   https://developer.android.com/guide/topics/manifest/activity-element#lmode


@Composable
fun MainApp(
    context: Context,
    sharedPreferences: SharedPreferences,
    viewModel: GetProfileViewModel,
    sharedViewModel: SharedViewModel,
    navController: NavController,
) {

    val navController = rememberNavController()
    val onLogoutAction: () -> Unit = {
        // Clear shared preferences or any other data
        sharedPreferences.edit().clear().apply()

        // Navigate to the login screen
        navController.navigate("login_screen") {
            // Clear the back stack so that the user can't go back to the previous screen
            popUpTo("login_screen") { inclusive = true }
        }

    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val isUserLoggedIn by sharedViewModel.isLoggedIn.observeAsState(false)
    var username by remember { mutableStateOf("") }
    val getReservationViewModel: GetReservationViewModel = hiltViewModel()
    //  val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val sharedViewModel: SharedViewModel = hiltViewModel()

    val screensWithTopBar = listOf("main_screen")  // Only main_screen will show the top bar
    val profileData by viewModel.profileData.observeAsState()
    val firstName by viewModel.firstName.observeAsState("")
    val lastName by viewModel.lastName.observeAsState("")
    //val image by viewModel.image.observeAsState("")
    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val selectedItem = currentBackStackEntry.value?.destination?.route ?: "main_screen"

    // Determine if the bottom bar should be shown
    val shouldShowBottomBar = when (selectedItem) {
        "main_screen", "summary_screen", "CreditPayment", "Profile_screen" , "login_screen" -> true
        else -> false
    }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                AnimatedBottomBar(
                    navController = navController,
                    getReservationViewModel = getReservationViewModel,
                    sharedViewModel = sharedViewModel,
                )
            }
        },
        content = { innerPadding ->
            Box(modifier = Modifier.padding(innerPadding)) {
                AppNavHost(
                    navController = navController,
                    isUserLoggedIn = isUserLoggedIn,
                    onLoginSuccess = { sharedViewModel.setLoggedIn(true) },
                    onLogout = { sharedViewModel.setLoggedIn(false) },
                    context = context,
                    sharedPreferences = sharedPreferences,
                    drawerState = drawerState,
                    scope = scope,
                    onSignupSuccess = { sharedViewModel.setLoggedIn(false) },
                )
            }
        }
    )
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
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(top = 1.dp)
                //  .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 30.dp))
                .background(Color(0xFF0054D8))
                .shadow(
                    elevation = 200.dp,
                    shape = RoundedCornerShape(topStart = 20.dp, topEnd = 30.dp),
                    clip = true
                )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                /*
                Icon(
                    painter = painterResource(id = R.drawable.sidebarblue),
                    contentDescription = "Menu",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .offset(x = -15.dp)
                        .clickable {
                            scope.launch {
                                if (drawerState.isClosed) {
                                    drawerState.open()
                                } else {
                                    drawerState.close()
                                }
                            }
                        }
                        .padding(8.dp)
                )
                 */

                Icon(
                    painter = painterResource(id = R.drawable.logopadelium),
                    contentDescription = "Right Icon",
                    tint = Color.Unspecified,
                    modifier = Modifier
                        .size(2000.dp)
                        .clip(CircleShape)
                        .clickable {
                            navController.navigate("main_screen")
                        }
                        .padding(8.dp)
                        .offset(x = 80.dp)
                )
            }
        }
    }
}

fun logout(context: Context) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    //sharedPreferences.edit().remove("JSESSIONID").apply()
    Toast.makeText(context, "Logged out successfully", Toast.LENGTH_SHORT).show()
}

/*
@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    // Your existing profile screen UI components

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Profile Screen")

        // Add a Logout button
        Button(onClick = {
            onLogout() // Call the onLogout function passed from MainApp
        }) {
            Text("Logout")
        }
    }
}
*/@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onReservationClicked: (LocalDate) -> Unit
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var isButtonClicked by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

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
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF0054D8))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) { }
            }
        }

        Button(
            onClick = {
                if (!isButtonClicked) {
                    isButtonClicked = true
                    selectedDate = LocalDate.now()

                    selectedDate?.let { date ->
                        isLoading = true
                        onReservationClicked(date)
                    } ?: Log.e("MainScreen", "Selected date is null")
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD7F057)),
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .offset(x = 100.dp, y = -35.dp)
                .border(0.5.dp, Color(0xFFD7F057), RoundedCornerShape(12.dp))
                .height(50.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Réserver",
                color = Color(0xFF0054D8),
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
                .height(350.dp)
        )
    }
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
    sharedViewModel: SharedViewModel
) {
    val selectedItem = navController.currentBackStackEntry?.destination?.route ?: "main_screen"
    val reservationsData by getReservationViewModel.ReservationsData.observeAsState(DataResult.Loading)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .background(Color(0xFF0054D8))
    ) {
        BottomNavigation(
            backgroundColor = Color(0xFF0054D8),
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
        ) {
            listOf(
                NavItem("main_screen", Icons.Filled.Home, "Accueil"),
                NavItem("summary_screen", Icons.Filled.CalendarMonth, "Mes Réservations"),
                NavItem("CreditPayment", Icons.Filled.CreditCard, "Mes crédits"),
                NavItem("Profile_screen", Icons.Filled.Person, "Profil") // Profile icon
            ).forEach { item ->
                CustomBottomNavItem(
                    navController = navController,
                    route = item.route,
                    icon = item.icon,
                    label = item.label,
                    isSelected = selectedItem == item.route,
                    context = LocalContext.current,
                    sharedViewModel = sharedViewModel
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
    isSelected: Boolean,
    context: Context,
    sharedViewModel: SharedViewModel
) {
    val isLoggedIn by sharedViewModel.isLoggedIn.observeAsState(false)

    val animatedOffsetY by animateDpAsState(
        targetValue = if (isSelected) (-12).dp else 0.dp,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    val backgroundColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFFD7F057) else Color.Transparent,
        animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing)
    )

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFF0054D8) else Color.White,
        animationSpec = tween(durationMillis = 400, easing = LinearOutSlowInEasing)
    )
    val textColor = if (isSelected) Color(0xFFD7F057) else Color.White

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 4.dp)
            .clickable {
                when (route) {
                    "Profile_screen", "CreditPayment", "summary_screen" -> {
                        if (isLoggedIn) {
                            navController.navigate(route) {
                                popUpTo("main_screen") { inclusive = false }
                            }
                        } else {
                            // Save the intended route to navigate after successful login
                            val intent = Intent(context, LoginActivity::class.java).apply {
                                putExtra("destination_route", route) // Ensure correct destination is passed
                                flags = Intent.FLAG_ACTIVITY_NEW_TASK // Keep the task but don't clear everything
                            }
                            context.startActivity(intent)

                        }
                    }
                    else -> {
                        // Handle any other cases if needed
                    }
                }
            }
            .padding(10.dp)

    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .height(60.dp)
                .offset(y = animatedOffsetY)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier
                    .size(27.dp)
                    .clip(CircleShape)
                    .background(backgroundColor)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = label,
                color = textColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Normal
            )
        }
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
            /*
                        DrawerItem(
                            icon = R.drawable.sidebarmenue,
                            label = "Réserver un terrain",
                            onClick = {
                                navController.navigate("reservation_options/${LocalDate.now()}/${null}") // Pass the selected date and time slot
                            }
                        )

             */

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


