package com.nevaDev.padeliummarhaba.ui.views
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
//import com.example.padeliummarhaba.ReservationScreen
import kotlinx.coroutines.launch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDrawerState
import androidx.compose.ui.text.style.TextAlign
import com.nevadev.padeliummarhaba.R

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
            modifier = Modifier.fillMaxWidth()
                .padding(10.dp)
        )

        // Overlay Text
        Text(
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
        IconButton(
            onClick = {
                currentIndex = if (currentIndex == 0) images.size - 1 else currentIndex - 1
            },
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_right),  // Replace with your left arrow drawable
                contentDescription = "Previous Image",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }

        // Right Arrow for next image
        IconButton(
            onClick = {
                currentIndex = (currentIndex + 1) % images.size
            },
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_left),
                contentDescription = "Next Image",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePage(navController: NavController, modifier: Modifier = Modifier) {
    var selectedTab by remember { mutableStateOf("Home") }
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true, // Allows swipe gesture to open the drawer
        drawerContent = {
            DrawerContent(navController = navController, onItemSelected = { item ->
                scope.launch { drawerState.close() } // Close the drawer after item selection
            })
        }
    ) {

                Box(modifier = Modifier.fillMaxSize()) {
                    // Top Section (base)
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xFFB2FF59))
                            .offset(y = 14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .offset(y = 14.dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 20.dp,
                                        topEnd = 20.dp,
                                        bottomEnd = 49.dp,
                                        bottomStart = 49.dp
                                    )
                                )
                                .background(Color(0xFF0054D8))
                                .height(450.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                                    .offset(y = -28.dp)
                            ) {
                                // Top section with logo and menu
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 16.dp),
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

                                    Image(
                                        painter = painterResource(id = R.drawable.logopadelium),
                                        contentDescription = "Logo Icon",
                                        modifier = Modifier.size(112.dp)
                                    )
                                }
                                // Ad space section
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Color.White),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "ESPACE PUBLICITAIRE",
                                        color = Color(0xFF0054D8),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                // Date picker and search bar
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp)
                                        .background(
                                            Color(0xFF0054D8),
                                            RoundedCornerShape(200.dp)
                                        ),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "Quand voulez-vous réserver?",
                                        color = Color.Yellow,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(
                                            top = 16.dp,
                                            bottom = 8.dp
                                        )
                                    )
                                }
                            }
                        }
                    }

                    // Rechercher Button (base element)
                    Box(
                        modifier = Modifier
                            .size(350.dp)
                            .offset(y = 440.dp, x = 25.dp)
                            .padding(horizontal = 40.dp)
                    ) {
                        Button(
                            onClick = {
                            },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFB2FF59)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .padding(horizontal = 16.dp)
                                .border(
                                    width = 1.dp, // Thickness of the border
                                    color = Color.Blue, // Border color
                                    shape = RoundedCornerShape(3.dp) // Optional: You can round the corners of the border
                                )
                        ) {
                            Box(
                                modifier = Modifier.fillMaxWidth(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.a321),
                                    contentDescription = "Search",
                                    tint = Color.Unspecified,
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                        .padding(end = 8.dp)
                                )
                                Text(
                                    text = "Réserver",
                                    color = Color.White,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                    }



                    // Second Section (overlay)
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp)
                        ) {
                            // Logo and text
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .offset(y = 502.dp),
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

                            // Description text
                            Text(
                                text = "Un avant goût de l'expérience Padel qui vous attend!",
                                color = Color.Gray,
                                fontSize = 15.sp,
                                modifier = Modifier
                                    .padding(vertical = 8.dp)
                                    .offset(y = 508.dp)
                            )

                            // Images section
                            Column(
                                modifier = Modifier
                                    .offset(y = 518.dp)
                                    .verticalScroll(scrollState)
                            ) {
                               // ImageWithOverlayText()

                                Spacer(modifier = Modifier.weight(1f)) // Pushes the bottom navigation to the bottom
                            }

                            // Bottom Navigation Bar
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                            ) {
                                // Main content of the page
                                Column(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(bottom = 70.dp) // Leave space for the BottomNavigation
                                ) {
                                    // Add your main content here
                                }

                                // Bottom Navigation Bar
                                BottomNavigation(
                                    backgroundColor = Color(0xFF0054D8),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(70.dp)
                                        .align(Alignment.BottomCenter) // Align the BottomNavigation at the bottom
                                        .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
                                ) {
                                    BottomNavigationItem(
                                        icon = { Icon(Icons.Filled.Home, contentDescription = "Home") },
                                        label = { Text("Home") },
                                        selected = selectedTab == "Home",
                                        onClick = { selectedTab = "Home" }
                                    )
                                    BottomNavigationItem(
                                        icon = { Icon(Icons.Filled.Book, contentDescription = "Réservation") },
                                        label = { Text("Réservation") },
                                        selected = selectedTab == "Réservation",
                                        onClick = { selectedTab = "Réservation" }
                                    )
                                    BottomNavigationItem(
                                        icon = { Icon(Icons.Filled.Person, contentDescription = "Profile") },
                                        label = { Text("Profile") },
                                        selected = selectedTab == "Profile",
                                        onClick = { selectedTab = "Profile" }
                                    )
                                }
                            }
                        }
                    }
                }
            }

    }


@Preview(showBackground = true)
@Composable
fun HomePagePreview() {
    // You need to provide a NavController here for the preview.
    HomePage(navController = rememberNavController())
}
 @Composable
                fun DrawerContent(navController: NavController, onItemSelected: (String) -> Unit) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Unspecified)  // Background color for the drawer
                    ) {
                        // User profile section
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .width(340.dp)
                                .height(200.dp)
                                .background(Color(0xFF4A4A4A))  // Dark gray background color
                                .clip(RoundedCornerShape(8.dp))  // Rounded corners
                                .padding(bottom = 16.dp)  // Space between profile and menu items
                        ) {
                            // User avatar
                            Image(
                                painter = painterResource(id = R.drawable.a9),  // user avatar resource
                                contentDescription = "User Avatar",
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(RoundedCornerShape(50))
                                    .background(Color.LightGray)
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            // Login and Register Text
                            Column {
                                androidx.compose.material3.Text(
                                    text = "Sign in",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                                androidx.compose.material3.Text(
                                    text = "Login or register",
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        // Menu items section
                        Column(
                            modifier = Modifier
                                .width(340.dp)
                                .background(Color(0xFFF5F5F5))  // Light gray background color
                                .clip(RoundedCornerShape(8.dp))
                                .padding(vertical = 8.dp)
                        ) {
                            DrawerItem(
                                icon = R.drawable.homy,
                                label = "Accueil",
                                onClick = {
                                    navController.navigate("main_screen") // Navigate to the MainScreen
                                }
                            )

                            DrawerItem(
                                icon = R.drawable.contact,
                                label = "Contactez-nous",
                                onClick = {
                                    navController.navigate("contact_screen") // Navigate to the Contact Screen
                                }
                            )

                            DrawerItem(
                                icon = R.drawable.prix, // Replace with your icon resource
                                label = "Prix",
                                onClick = {
                                    navController.navigate("prix_screen") // Navigate to the Prix Screen
                                }
                            )

                            DrawerItem(
                                icon = R.drawable.login,
                                label = "Se connecter",
                                onClick = {
                                    navController.navigate("SignUpScreen") // Navigate to the Login Screen
                                }
                            )


                            DrawerItem(
                                icon = R.drawable.signup, // Replace with your icon resource
                                label = "S'inscrire",
                                onClick = {
                                    navController.navigate("signup_screen") // Navigate to the Signup Screen
                                }
                            )

                            Spacer(modifier = Modifier.weight(1f))

                            // Version text at the bottom
                            androidx.compose.material3.Text(
                                text = "v1.19.2",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.CenterHorizontally)
                                    .background(Color.Transparent)
                            )
                        }
                    }
                }


                @Composable
                fun DrawerItem(
                    icon: Int, // Resource ID of the icon
                    label: String,
                    onClick: () -> Unit,
                    labelColor: Color = Color.White
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { onClick() }
                    ) {
                        androidx.compose.material3.Icon(
                            painter = painterResource(id = icon),
                            contentDescription = label,
                            modifier = Modifier.size(24.dp),
                            tint = Color.Unspecified
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        androidx.compose.material3.Text(
                            text = label,
                            color = labelColor,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }

@Composable
fun CustomTopBar(onMenuClick: () -> Unit) {
    TopAppBar(
        backgroundColor = Color(0xFF0054D8), // Blue background color
        contentPadding = PaddingValues(16.dp), // Optional padding for the content
        elevation = 4.dp // Optional shadow for the app bar
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
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
                    .background(Color(0xFF4CAF50)) // Green background for the icon
                    .clickable { onMenuClick() }
                    .padding(8.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.a91),
                contentDescription = "Logo Icon",
                modifier = Modifier.size(82.dp)
            )
        }
    }
}


