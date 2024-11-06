package com.nevaDev.padeliummarhaba.ui.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.nevadev.padeliummarhaba.R

@Composable
fun DrawerContentOnline(
    navController: NavController,
    onItemSelected: (String) -> Unit,
    onCloseDrawer: () -> Unit,
    username: String // Pass username here

) {
    Column(
        modifier = Modifier
            .offset(x = -40.dp)
            .padding(horizontal = 30.dp)
            .fillMaxSize()
            .background(Color(0xFF0054D8))
    ) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0054D8)) // Blue background
            .padding(horizontal = 30.dp, vertical = 16.dp)
            //.width(50.dp)



    ) {
        Column(
            modifier = Modifier
                .offset(x = -40.dp)
                .padding(horizontal = 30.dp)
                .fillMaxSize()
                .background(Color(0xFF0054D8))
                .width(100.dp)
        ) {
            IconButton(
                onClick = { onCloseDrawer() },
                modifier = Modifier.align(Alignment.End).offset(x = 90.dp, y = -8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.oip),
                    contentDescription = "Close Drawer",
                    tint = Color.Unspecified // Set color for the X icon
                )
            }}
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp) // Add padding to the bottom to avoid overlap with footer
        ) {

            // Top section with logo and greeting
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Padelium logo
                Image(
                    painter = painterResource(id = R.drawable.logopadelium), // Replace with your logo resource
                    contentDescription = "Padelium Logo",
                    modifier = Modifier.size(260.dp)
                        .offset(y = -80.dp)
                )

                // Profile section
                Row(
                    modifier = Modifier.fillMaxWidth() .offset(y = -110.dp,x=30.dp),
                    verticalAlignment = Alignment.CenterVertically // Aligns items vertically centered
                ) {
                    // Avatar
                    Image(
                        painter = painterResource(id = R.drawable.a9),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    )

                    Spacer(modifier = Modifier.width(8.dp)) // Space between the avatar and text

                    // Column for welcome text to align text elements vertically
                    Column(
                        modifier = Modifier
                            .weight(1f) // Allows the column to take remaining space in the row
                            .padding(start = 16.dp)


                    ) {
                        // Welcome Text
                        Text(
                            text = "Bonjour,$username ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Start) // Align text to the start
                        )
                        Text(
                            text = "Alors, prêts pour jouer?",
                            fontSize = 14.sp,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Start) // Align text to the start
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(modifier = Modifier .padding(horizontal = 20.dp).offset(y = -110.dp),
                    color = Color.White, thickness = 2.dp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            // User Settings Section
            Column(modifier = Modifier.padding(vertical = 8.dp).offset(y = -110.dp),) {
                // Section Header
                Text(
                    text = "Paramètres d'utilisateurs",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFCCEA44)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Menu Items
                DrawerItem(
                    icon = R.drawable.homy,
                    label = "Profil",
                    onClick = { navController.navigate("profile_screen") }
                )

                DrawerItem(
                    icon = R.drawable.contact,
                    label = "Mes réservations",
                    onClick = { navController.navigate("reservations_screen") }
                )

                DrawerItem(
                    icon = R.drawable.contact,
                    label = "Paramètres",
                    onClick = { navController.navigate("settings_screen") }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Other Section
            Column(modifier = Modifier.padding(vertical = 8.dp).offset(y = -110.dp),) {
                // Section Header
                Text(
                    text = "Autres",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFCCEA44)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Menu Items
                DrawerItem(
                    icon = R.drawable.contact,
                    label = "Plan tarifaires",
                    onClick = { navController.navigate("pricing_screen") }
                )

                DrawerItem(
                    icon = R.drawable.contact,
                    label = "Comment utiliser l’application?",
                    onClick = { navController.navigate("help_screen") }
                )

                DrawerItem(
                    icon = R.drawable.contact,
                    label = "Contactez-nous",
                    onClick = { navController.navigate("contact_screen") }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Make the image bigger

            }
        }
        Image(
            painter = painterResource(id = R.drawable.a91), // Replace with your image resource
            contentDescription = "Additional Image",
            modifier = Modifier
                .fillMaxWidth() // Fills the available width
                .height(200.dp) // Increased height for the image
                .align(Alignment.BottomCenter) // Align to the bottom center
                .offset(y = -60.dp, x = -115.dp) // Adjust the offset as needed
        )

        Row(
            modifier = Modifier.size(165.dp).offset(y = 740.dp, x = 53.dp),
            horizontalArrangement = Arrangement.End
        ) {
        Button(onClick = {
            onLogout() // Call the onLogout function passed from MainApp
        },
                modifier = Modifier
                .fillMaxWidth().padding(start = 10.dp, end = 5.dp)
            .border(1.dp, Color.Unspecified, RoundedCornerShape(13.dp))
            .clip(RoundedCornerShape(10.dp)) ,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
        )
        {
            Text("Se déconnecter", color = Color(0xFF0054D8), fontWeight = FontWeight.Bold)//0054D8
        }}
        // Footer section
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(6.dp), // Add padding for aesthetics
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(modifier = Modifier .width(900.dp).offset(y = -10.dp),
                color = Color.White, thickness = 2.dp)

            Text(
                text = "v1.0.0",
                fontSize = 10.sp,
                color = Color.White
            )
            Text(
                text = "Copyright © 2024 DEVPRO | Tous droits réservés.",
                fontSize = 10.sp,
                color = Color.White
            )
        }
    }
}


@Composable
fun DrawerItemonline(icon: Int, label: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = label,
            tint = Color.White,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = label,
            fontSize = 16.sp,
            color = Color.White
        )

    }
}}

fun onLogout() {
    TODO("Not yet implemented")
}

@Preview(showBackground = true)
@Composable
fun DrawerContentPreview() {
    val navController = rememberNavController()

    // Pass a sample username for the preview
    DrawerContentOnline(
        navController = navController,
        onItemSelected = {}, // Mock function for preview
        onCloseDrawer = {},  // Mock function for preview
        username = "John Doe" // Sample username for preview
    )
}



