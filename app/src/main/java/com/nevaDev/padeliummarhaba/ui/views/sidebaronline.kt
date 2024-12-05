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
    username: String
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
            .background(Color(0xFF0054D8))
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
                    tint = Color.Unspecified
                )
            }}
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 10.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logopadelium),
                    contentDescription = "Padelium Logo",
                    modifier = Modifier.size(260.dp)
                        .offset(y = -80.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth() .offset(y = -110.dp,x=30.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.a9),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Color.LightGray)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)


                    ) {
                        // Welcome Text
                        Text(
                            text = "Bonjour,$username ",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Start)
                        )

                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(modifier = Modifier .padding(horizontal = 20.dp).offset(y = -110.dp),
                    color = Color.White, thickness = 2.dp)
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(modifier = Modifier.padding(vertical = 8.dp).offset(y = -110.dp),) {


                Spacer(modifier = Modifier.height(8.dp))

                DrawerItem(
                    icon = R.drawable.sidebarmenue,
                    label = "Profil",
                    labelColor = Color.White,
                    onClick = { navController.navigate("profile_screen") }

                )

                DrawerItem(
                    icon = R.drawable.sidebarmenue,
                    label = "Mes Réservations",
                    labelColor = Color.White,
                    onClick = { navController.navigate("reservations_screen") }
                )
                DrawerItem(
                    icon = R.drawable.sidebarmenue,
                    label = "Plans Tarifaires",
                    labelColor = Color.White,
                    onClick = { navController.navigate("CreditPayment") }
                )


                Spacer(modifier = Modifier.height(16.dp))
            }

            Column(modifier = Modifier.padding(vertical = 8.dp).offset(y = -110.dp),) {


                Spacer(modifier = Modifier.height(8.dp))



                DrawerItem(
                    icon = R.drawable.sidebarmenue,
                    label = "Qui sommes nous?",
                    labelColor = Color.White,
                    onClick = { navController.navigate("help_screen") }
                )

                DrawerItem(
                    icon = R.drawable.sidebarmenue,
                    label = "CGV",
                    labelColor = Color.White,
                    onClick = { navController.navigate("contact_screen") }
                )

                Spacer(modifier = Modifier.height(16.dp))


            }
        }
        Image(
            painter = painterResource(id = R.drawable.a91),
            contentDescription = "Additional Image",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .align(Alignment.BottomCenter)
                .offset(y = -60.dp, x = -135.dp)
        )

        Row(
            modifier = Modifier.size(165.dp).offset(y = 760.dp, x = 90.dp),
            horizontalArrangement = Arrangement.End
        ) {
        Button(onClick = {
            onLogout()
        },
                modifier = Modifier
                .fillMaxWidth().padding(start = 10.dp, end = 5.dp)
            .border(1.dp, Color.Unspecified, RoundedCornerShape(13.dp))
            .clip(RoundedCornerShape(10.dp)) ,
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
        )
        {
            Text("Se déconnecter", color = Color(0xFF0054D8), fontWeight = FontWeight.Bold)
        }}
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(6.dp),
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

    DrawerContentOnline(
        navController = navController,
        onItemSelected = {},
        onCloseDrawer = {},
        username = "John Doe"
    )
}



