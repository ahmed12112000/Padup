package com.nevaDev.padeliummarhaba.ui.views

import android.util.Log
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.nevadev.padeliummarhaba.R
import android.util.Base64
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.foundation.text.ClickableText
import androidx.compose.ui.text.TextStyle
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.SpanStyle
import androidx.hilt.navigation.compose.hiltViewModel
import com.nevaDev.padeliummarhaba.viewmodels.LogoutViewModel
import com.nevaDev.padeliummarhaba.viewmodels.UserViewModel
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.logoutRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collectLatest


@Composable
fun CopyrightText() {
    val context = LocalContext.current

    // Create an annotated string with clickable text
    val annotatedString = buildAnnotatedString {
        append("Copyright © 2025 ")
        // Add an annotation for "SPOFUN" to make it clickable
        pushStringAnnotation(tag = "SPOFUN", annotation = "https://spofun.tn/")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.White)) {
            append("SPOFUN")
        }
        pop()
        append(" | Tous droits réservés.")
    }

    // Use ClickableText to make "SPOFUN" clickable
    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            // Get the clicked annotation
            val clickedTag = annotatedString.getStringAnnotations(offset, offset).firstOrNull()
            // If the clicked part is "SPOFUN", open the URL
            clickedTag?.let {
                if (it.tag == "SPOFUN") {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it.item))
                    context.startActivity(intent)
                }
            }
        },
        style = TextStyle(color = Color.White, fontSize = 10.sp) // Use TextStyle here
    )
}

@Composable
fun  DrawerContentOnline(
    navController: NavController,
    onItemSelected: (String) -> Unit,
    onCloseDrawer: () -> Unit,
    firstName: String,
    lastName: String,
    image: String,
   // onLogout: () -> Unit,
) {
    val userViewModel: LogoutViewModel = hiltViewModel()
    Log.d("ImageURL", "Image URL: $image")
    val bitmap = remember(image) {
        if (image.isNotEmpty()) {
            val decodedString = Base64.decode(image, Base64.DEFAULT)
            android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
        } else {
            null
        }
    }
    val context = LocalContext.current
    val logoutState by userViewModel.logoutState.collectAsState()

/*
    when (logoutState) {
        is DataResult.Success -> {
            // Navigate to login screen and clear the back stack
            LaunchedEffect(Unit) {
                navController.navigate("login_screen") {
                    popUpTo(navController.graph.startDestinationId) {
                        inclusive = true
                    }
                }
                onCloseDrawer()
            }
        }
        is DataResult.Failure -> {
            val errorMessage = (logoutState as DataResult.Failure).errorMessage
            Toast.makeText(context, "Logout failed: $errorMessage", Toast.LENGTH_SHORT).show()
        }
        DataResult.Loading -> {
            // Optionally: Show a loading indicator if needed
        }
        else -> {}
    }
*/

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
                        modifier = Modifier.fillMaxWidth().offset(y = -110.dp, x = 30.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (bitmap != null) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Avatar",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                            )
                        } else {
                            // Fallback image if bitmap is null
                            Image(
                                painter = painterResource(id = R.drawable.a9),
                                contentDescription = "Fallback Avatar",
                                modifier = Modifier
                                    .size(60.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray)
                            )
                        }





                        Spacer(modifier = Modifier.width(4.dp))

                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp)


                        ) {
                            // Welcome Text
                            Text(
                                text = "Bonjour, $firstName $lastName ",
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
                        onClick = { navController.navigate("summary_screen") }
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
                  //  val logoutRequest = logoutRequest()
                    //  userViewModel.logoutUser(logoutRequest)

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
            /*  Column(
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
                      text = "Copyright © 2025 SPOFUN  | Tous droits réservés.",
                      fontSize = 10.sp,
                      color = Color.White
                  )
              }  */
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(6.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalDivider(
                    modifier = Modifier.width(900.dp).offset(y = -10.dp),
                    color = Color.White, thickness = 2.dp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "v1.0.0",
                    fontSize = 10.sp,
                    color = Color.White
                )


                CopyrightText()
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
/*
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

 */



