package com.nevaDev.padeliummarhaba.ui.activities
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.*
import androidx.compose.material.BottomNavigation
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel

import java.time.LocalDate
import androidx.navigation.NavType
import androidx.navigation.navArgument
import java.time.format.DateTimeFormatter
import com.nevaDev.padeliummarhaba.ui.views.DrawerContentOnline
import com.nevaDev.padeliummarhaba.ui.views.LoginScreen
import com.nevaDev.padeliummarhaba.ui.views.PayScreen
import com.nevaDev.padeliummarhaba.ui.views.ProfileScreen
import com.nevaDev.padeliummarhaba.ui.views.ReservationScreen
import com.nevaDev.padeliummarhaba.ui.views.SignUpScreen
import com.nevaDev.padeliummarhaba.ui.views.SplashScreen
import com.nevaDev.padeliummarhaba.viewmodels.ReservationViewModel
import com.nevaDev.padeliummarhaba.ui.theme.PadeliumMarhabaTheme
import com.nevadev.padeliummarhaba.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize SharedPreferences
        val sharedPreferences = getSharedPreferences("csrf_prefs", MODE_PRIVATE)

        // Initialize RetrofitClient with SharedPreferences
        setContent {
            PadeliumMarhabaTheme {
                var showSplashScreen by remember { mutableStateOf(true) }

                // Delay for 3 seconds and then show the main app
                LaunchedEffect(Unit) {
                    delay(3100) // 3 second delay for the splash screen
                    showSplashScreen = false
                }

                if (showSplashScreen) {
                    // Show Splash Screen
                    SplashScreen()
                } else {
                    // Show Main App Content
                    val context = LocalContext.current // Get context
                    MainApp(context, sharedPreferences) // Pass the context here
                }
            }
        }

    }
}



@Composable
fun MainApp(context: Context, sharedPreferences: SharedPreferences) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var isUserLoggedIn by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }

    // Create an instance of ReservationViewModel directly
    val viewModel: ReservationViewModel = remember { ReservationViewModel() } // Pass any required parameters

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            // Display online drawer if user is logged in, otherwise the regular drawer
            if (isUserLoggedIn) {
                DrawerContentOnline(
                    navController = navController,
                    onItemSelected = { itemSelected -> /* Handle item selection */ },
                    onCloseDrawer = {
                        scope.launch { drawerState.close() }
                    },
                    username = username
                )
            } else {
                DrawerContent(
                    navController = navController,
                    onItemSelected = { itemSelected -> /* Handle item selection */ },
                    onCloseDrawer = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .padding(top = 16.dp)
                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 30.dp))
                        .background(Color(0xFF0054D8))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFF0054D8))
                            .height(80.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White,
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF4CAF50))
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
                            LogoHeader(navController = navController)
                        }
                    }
                }
            },
            bottomBar = {
                AnimatedBottomBar(navController = navController)
            },
            content = { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = if (isUserLoggedIn) "main_screen" else "login_screen",
                    Modifier.padding(innerPadding)
                ) {

                    // Your main screen
                    composable("PayScreen/{totalAmount}") { backStackEntry ->
                        val totalAmount = backStackEntry.arguments?.getString("totalAmount")?.toIntOrNull() ?: 0
                        PayScreen(totalAmount = totalAmount, navController = navController)

                    }
                    composable("main_screen") {
                        MainScreen(navController, onReservationClicked = { selectedDate ->
                            // Example data with fallback values for testing
                            val key = "someKey"
                            val activityName = "SomeActivity" // Ensure this is not null
                            val cityName = "SomeCity" // Ensure this is a valid String
                            val activityId = "1" // This should be a String
                            val cityId = "1" // This should be a String
                            val establishmentId = "1" // This should be a String
                            val time = "10:00" // Ensure this is a valid time in String format
                            val isCity = false // Boolean

                            // Ensure the date is formatted correctly as a String
                            val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))

                            // Construct the destination string with proper types
                            val destination = "reservation_screen/$key/$formattedDate/$activityName/$cityName/$activityId/$cityId/$establishmentId/$time/$isCity"

                            // Navigate to the reservation screen
                            navController.navigate(destination)
                        })

                    }
//error: wrong argument type for "ActivityName" in argument bundle. string expected
                    composable(
                        route = "reservation_screen/{key}/{date}/{activityName}/{cityName}/{activityId}/{cityId}/{establishmentId}/{time}/{isCity}",
                        arguments = listOf(
                            navArgument("key") { type = NavType.StringType },
                            navArgument("date") { type = NavType.StringType },
                            navArgument("activityName") { type = NavType.StringType },
                            navArgument("cityName") { type = NavType.StringType },
                            navArgument("activityId") { type = NavType.StringType },
                            navArgument("cityId") { type = NavType.StringType },
                            navArgument("establishmentId") { type = NavType.StringType },
                            navArgument("time") { type = NavType.StringType },
                            navArgument("isCity") { type = NavType.BoolType }
                        )
                    )  {


                        ReservationScreen(
                            navController = navController,
                            isUserLoggedIn = isUserLoggedIn,
                            context = context,
                            sharedPreferences = sharedPreferences
                        )
                    }
                    composable("login_screen") {

                        LoginScreen(onLoginSuccess = {
                            isUserLoggedIn = true
                            // Store session ID in SharedPreferences
                            val sessionId = "your_session_id" // Replace with actual session ID obtained from login response
                            // sharedPreferences.edit().putString("JSESSIONID", sessionId).apply()
                            navController.popBackStack()
                        })
                    }
                    composable("Profile_screen") {
                        ProfileScreen(onLogout = {
                            // Handle logout action
                            // sharedPreferences.edit().remove("JSESSIONID").apply() // Clear session ID on logout
                            isUserLoggedIn = false
                            navController.navigate("login_screen") {
                                popUpTo("main_screen") { inclusive = true } // Clear back stack
                            }
                        })
                    }
                    composable("signup_screen") {
                        SignUpScreen(
                            navController = navController,
                            onSignupSuccess = { /* Define the action to take on successful signup, e.g., navigate to a different screen */ },
                            viewModel = hiltViewModel()
                        ) }
                }
            }
        )
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
*/

@Composable
fun MainScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    onReservationClicked: (LocalDate) -> Unit // Callback when reservation button is clicked
) {
    var selectedDate by remember { mutableStateOf<LocalDate?>(null) }
    var isButtonClicked by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) } // Local loading state

    // Show loading indicator if necessary
    if (isLoading) {
        CircularProgressIndicator() // Add a loading spinner or indicator
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = -4.dp)
                .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                .background(Color(0xFF0054D8))
                .height(300.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .offset(y = -4.dp)
            ) {
                // Top Content, Date picker, and Ad space can be added here

                // Date picker and search bar
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .background(Color(0xFF0054D8), RoundedCornerShape(200.dp))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .padding(horizontal = 16.dp)
                            .offset(y = 50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Quand voulez-vous réserver?",
                            color = Color.Yellow,
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Rechercher Button
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier.fillMaxSize()
            ) {
                Button(
                    onClick = {
                        if (!isButtonClicked) {
                            isButtonClicked = true
                            selectedDate = LocalDate.now()

                            // Call the reservation key fetch function with the selected date
                            selectedDate?.let { date ->
                                isLoading = true // Start loading state
                                onReservationClicked(date)
                            } ?: run {
                                Log.e("MainScreen", "Selected date is null")
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB2FF59)),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp)
                        .fillMaxWidth(0.5f)
                ) {
                    Text(
                        text = "Réserver",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }





    // Overlay Section with Images and Bottom Navigation
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            // Top Logo and text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = 332.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.email),
                    contentDescription = "Logo",
                    modifier = Modifier.size(32.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "| Quelques photos",
                    color = Color.Blue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            /*
            ok i want the bottom bar to be animated like in the photo...i mean when i click on one of the three items included in the bottom bar
           the  icon go out and be bigger with changing in it color from original tou yellow also the name correspond to it will be i yellow color
            just like the photo i provided to you

             */



            // Description text
            Text(
                text = "Un avant goût de l'expérience Padel qui vous attend!",
                color = Color.Gray,
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(vertical = 8.dp)
                    .offset(y = 330.dp)
            )

            // Images section
            Column(
                modifier = Modifier
                    .offset(y = 128.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                ImageWithOverlayText()

                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun AnimatedBottomBar(navController: NavController) {
    val selectedItem = navController.currentBackStackEntry?.destination?.route ?: "main_screen"

    BottomNavigation(
        backgroundColor = Color(0xFF0054D8),
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
    ) {
        CustomBottomNavItem(
            navController = navController,
            route = "main_screen",
            icon = Icons.Filled.Home,
            label = "Home",
            isSelected = selectedItem == "main_screen"
        )
        CustomBottomNavItem(
            navController = navController,
            route = "summary_screen",
            icon = Icons.Filled.Book,
            label = "Réservation",
            isSelected = selectedItem == "summary_screen"
        )
        CustomBottomNavItem(
            navController = navController,
            route = "Profile_screen",
            icon = Icons.Filled.Person,
            label = "Profile",
            isSelected = selectedItem == "Profile_screen"
        )
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
    // Animating scale based on selection
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.4f else 1f,
        animationSpec = tween(durationMillis = 300) // Customize animation duration here
    )

    // Animating color change smoothly
    val iconColor by animateColorAsState(
        targetValue = if (isSelected) Color(0xFFE5F727) else Color.White,
        animationSpec = tween(durationMillis = 300)
    )

    Box(
        modifier = Modifier
            .clickable {
                navController.navigate(route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
            .padding(16.dp)
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, color = iconColor)
        }
    }
}






/*
@Composable
fun ReservationElements() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Your reservation UI components
        Text(text = "Reservation Elements", fontWeight = FontWeight.Bold, fontSize = 24.sp)

        // Additional UI elements like TextFields, Buttons, etc.
        // Example:
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Your Input") })
        Button(onClick = { /* Handle reservation logic */ }) {
            Text("Confirm Reservation")
        }
    }
}*/

@Composable
fun LogoHeader(navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
            .clickable {
                // Navigate to the home page when the icon is clicked
                navController.navigate("main_screen")
            },
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        // Increase the size of the Box for a larger logo display
        Box(
            modifier = Modifier
                .size(140.dp) // Adjust size as needed for your design
                .padding(end = 8.dp)
                .offset(x = 200.dp)// Add padding if needed
        ) {
            Image(
                painter = painterResource(id = R.drawable.logopadelium), // Ensure this is a high-quality image
                contentDescription = "Logo Icon",
                contentScale = ContentScale.Crop, // Use Crop to fill the Box
                modifier = Modifier.fillMaxSize() // Make the image fill the Box
            )
        }
    }
}



@Composable
fun ImageWithOverlayText() {
    // List of images to display
    val images = listOf(R.drawable.photo1, R.drawable.photo2, R.drawable.photo3)
    var currentIndex by remember { mutableStateOf(0) }  // Manage the current image index

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(16.dp))
    ) {
        // Display the current image
        Image(
            painter = painterResource(id = images[currentIndex]),
            contentDescription = null,
            modifier = Modifier.size(600.dp)
                .padding(10.dp)
        )

        // Overlay Text
        androidx.compose.material.Text(
            text = "VIVEZ L'EXCITATION DU\nPADEL À SOUSSE",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFADFF2F),  // Bright yellow-green color
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)
        )

        // Left Arrow for previous image
        androidx.compose.material.IconButton(
            onClick = {
                currentIndex = if (currentIndex == 0) images.size - 1 else currentIndex - 1
            },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp)
        ) {
            androidx.compose.material.Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),  // Replace with your left arrow drawable
                contentDescription = "Previous Image",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }

        // Right Arrow for next image
        androidx.compose.material.IconButton(
            onClick = {
                currentIndex = (currentIndex + 1) % images.size
            },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
        ) {
            androidx.compose.material.Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = "Next Image",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}

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
                tint = Color.Unspecified // Set color for the X icon
            )
        }

        // Logo and Sign In section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 16.dp, end = 16.dp, bottom = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logopadelium), // Replace with your logo resource
                contentDescription = "Padelium Logo",
                modifier = Modifier.size(160.dp)
                    .offset(y = -90.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Sign in section with avatar and texts in a Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = -90.dp),
                verticalAlignment = Alignment.CenterVertically // Align items in the Row vertically centered
            ) {
                Image(
                    painter = painterResource(id = R.drawable.a9),
                    contentDescription = "User Avatar",
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFF0054D8))

                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        text = "Se connecter",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    // Use Row for inline text
                    Row {
                        Text(
                            text = "Se connecter",
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier
                                .clickable {
                                    navController.navigate("login_screen") // Navigate to the login page
                                }
                        )
                        Text(
                            text = " ou ",
                            fontSize = 14.sp,
                            color = Color.White
                        )
                        Text(
                            text = "S'inscrire",
                            fontSize = 14.sp,
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

            Spacer(modifier = Modifier.height(32.dp))
        }

        // Menu items section
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .offset(y = -80.dp)
                .weight(1f) // Pushes the content above it upward
        ) {
            DrawerItem(
                icon = R.drawable.homy,
                label = "Accueil",
                onClick = {
                    navController.navigate("main_screen")
                }
            )

            DrawerItem(
                icon = R.drawable.contact, // Replace with your icon resource for reservation
                label = "Réserver un terrain",
                onClick = {
                    navController.navigate("reservation_screen")
                }
            )

            DrawerItem(
                icon = R.drawable.contact, // Replace with your icon resource for pricing plan
                label = "Plan tarifaires",
                onClick = {
                    navController.navigate("pricing_screen")
                }
            )

            DrawerItem(
                icon = R.drawable.contact, // Replace with your icon resource for help
                label = "Comment utiliser l'application?",
                onClick = {
                    navController.navigate("how_to_use_screen")
                }
            )

            DrawerItem(
                icon = R.drawable.contact, // Replace with your icon resource for about us
                label = "Qui sommes nous?",
                onClick = {
                    navController.navigate("about_us_screen")
                }
            )

            DrawerItem(
                icon = R.drawable.contact, // Replace with your icon resource for contact
                label = "Contactez-nous",
                onClick = {
                    navController.navigate("contact_screen")
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp)) // Spacer to add some space above the image

        Image(
            painter = painterResource(id = R.drawable.a91), // Replace with your image resource
            contentDescription = "Additional Image",
            modifier = Modifier
                .size(160.dp) // Adjust size as needed
                .align(Alignment.CenterHorizontally)
                .offset(y = -55.dp, x = -128.dp) // Center the image horizontally
        )

        HorizontalDivider(modifier = Modifier .fillMaxWidth().offset(y = -10.dp),
            color = Color.White, thickness = 2.dp)

        // Footer section at the bottom
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Column {
                Text(
                    text = "v1.0.0",
                    fontSize = 10.sp,
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.height(1.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Copyright © 2024 DEVPRO | Tous droits réservés.",
                        fontSize = 9.sp,
                        color = Color.White
                    )
                }
            }
        }
    }
}





@Composable
fun DrawerItem(icon: Int, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp)
            .background(Color.Transparent),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon for the drawer item
        Image(
            painter = painterResource(id = icon),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(Color.White)
        )
        Spacer(modifier = Modifier.width(16.dp))

        // Text for the drawer item
        Text(
            text = label,
            fontSize = 15.sp,
            color = Color.White,
            fontWeight = FontWeight.SemiBold
        )
    }

    // Divider between menu items
    Divider(color = Color.White, thickness = 1.dp)
}



@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PadeliumMarhabaTheme {
        // Provide a NavController instance if necessary
        val navController = rememberNavController()

        // Use LocalContext for preview and create a mock SharedPreferences
        val context = LocalContext.current

        // Create a dummy SharedPreferences instance for the preview
        val sharedPreferences = context.getSharedPreferences("preview_prefs", Context.MODE_PRIVATE)

        // Call MainApp and pass the dummy sharedPreferences
        MainApp(context = context, sharedPreferences = sharedPreferences) // Pass the sharedPreferences here
    }
}

