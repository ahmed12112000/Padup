package com.nevaDev.padeliummarhaba.ui.views

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.nevadev.padeliummarhaba.R

@Composable
fun ProfileScreen(onLogout: () -> Unit) {
    var prenom by remember { mutableStateOf("") }
    var nom by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    var isValid by remember { mutableStateOf(false) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            profileImageUri = uri
        } else {
            Toast.makeText(context, "Image selection cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "Mon Profil",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
                    color = Color.Black,
            modifier = Modifier.offset(x = 5.dp)
        )
        Spacer(modifier = Modifier.height(13.dp))

        HorizontalDivider(modifier = Modifier .width(900.dp).padding(horizontal = 10.dp).offset(y = -10.dp),
            color = Color.Gray, thickness = 1.dp)
        Spacer(modifier = Modifier.height(28.dp))

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(18.dp)
                .padding(horizontal = 17.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
        ) {
            LinearProgressIndicator(
                progress = 0.75f,
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFFD7F057),
                backgroundColor = Color.White,
                strokeCap = StrokeCap.Round
            )

            Text(
                text = "75%",
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(),
                color = Color.Black
            )
        }



        Spacer(modifier = Modifier.height(16.dp))

        BasicTextField(
            value = prenom,
            onValueChange = { prenom = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            decorationBox = { innerTextField ->
                Column {
                    Text(text = "| Nom", color = Color.Black,  fontSize = 19.sp,
                        modifier = Modifier.offset(x = 25.dp))

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                            //.shadow(1.dp, RoundedCornerShape(15.dp))
                           // .clip(RoundedCornerShape(24.dp)) // Clip the inner content to the rounded shape
                    ) {
                        OutlinedTextField(
                            value = prenom,
                            onValueChange = { prenom = it },
                            label = { Text("") },
                           // leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(15.dp)
                        )
                        }
                    }

            }
        )


        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = nom,
            onValueChange = { nom = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            decorationBox = { innerTextField ->
                Column {
                    Text(text = "| Prénom", color = Color.Black,  fontSize = 19.sp,
                        modifier = Modifier.offset(x = 25.dp))

                    Box(
                        modifier = Modifier
                            .padding(4.dp)
                            .fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = prenom,
                            onValueChange = { prenom = it },
                            label = { Text("") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(15.dp)
                        )
                        }
                    }

            }
        )


        Spacer(modifier = Modifier.height(16.dp))

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = phoneNumber,
                    onValueChange = {
                        if (it.all { char -> char.isDigit() } && it.length <= 8) {
                            phoneNumber = it
                            isValid = it.length == 8
                        }
                    },
                    modifier = Modifier
                        .offset(x = 3.dp)
                        .width(150.dp)
                        .padding(vertical = 4.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Phone,
                                contentDescription = "Phone Icon",
                                modifier = Modifier.padding(8.dp)
                            )
                            Text(
                                text = "|",
                                fontSize = 29.sp,
                                color = Color.Black,
                                modifier = Modifier.offset(x = -8.dp, y = -2.dp))
                            Box(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                innerTextField()
                            }

                            if (isValid) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Valid Phone Number",
                                    tint = Color.Green,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.width(10.dp))


                    Button(
                        onClick = {
                            if (isValid) {
                                println("Phone number is valid: $phoneNumber")
                            } else {
                                println("Phone number must be 8 digits.")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth().padding(10.dp)
                            .border(1.dp, Color.Unspecified, RoundedCornerShape(13.dp))
                            .clip(RoundedCornerShape(10.dp)) ,
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.White)
                    ) {
                        Text(text = "Modifier", color = Color(0xFF0066CC), fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                        )
                    }
                }



            if (phoneNumber.isNotEmpty()) {
                Text(
                    text = "Modifier",
                    color = Color.Blue,
                    modifier = Modifier.padding(top = 4.dp)
                )

            }
            Text(text = "Votre numéro ne nous sert que pour communiquer les informations utiles à vos réservations.",
                color = Color.Gray)
        }

        Spacer(modifier = Modifier.height(36.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Photo de profile",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                modifier = Modifier.offset(x = 100.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .offset(x=100.dp)
                    .size(90.dp)
                   /// .background(Color.Unspecified) // Ensure the box has a white background
                    .drawBehind {
                        drawCircle(
                            color = Color.Gray.copy(alpha = 0.5f), // Grey shadow with transparency
                            radius = size.minDimension / 3 + 2.dp.toPx(), // Reduce shadow spread
                            center = center
                        )
                    }
            ) {
                if (profileImageUri != null) {
                    Image(
                        painter = rememberAsyncImagePainter(profileImageUri),
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize()
                            .clickable {
                            launcher.launch("image/*")
                        },
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        painter = painterResource(id = R.drawable.a321),
                        contentDescription = "Upload Photo",
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxSize()
                            .clickable {
                                launcher.launch("image/*")
                            },
                        tint = Color.Unspecified
                    )
                }
            }

        }



        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {  },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor =Color(0xFF0066CC),
                    contentColor = Color.White,
                ),
                modifier = Modifier
                    .padding(start = 3.dp)
                    .height(50.dp)
                    .fillMaxWidth(0.6f)
                    .offset(x = 10.dp),
                shape = RoundedCornerShape(12.dp)


            ) {
                Text(
                    text = "Sauvegarder",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.width(8.dp))

        }
/*
  Button(
                        onClick = {  },
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White,
                            contentColor = Color(0xFF0066CC),
                        )

                    ) {
                        Text(
                            text = "Sauvegarder",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    }
 */
        Spacer(modifier = Modifier.height(16.dp))


    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(onLogout = {  })
}
