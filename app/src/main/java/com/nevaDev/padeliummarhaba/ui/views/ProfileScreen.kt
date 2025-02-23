package com.nevaDev.padeliummarhaba.ui.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.nevaDev.padeliummarhaba.di.SessionManager
import com.nevaDev.padeliummarhaba.ui.activities.SharedViewModel
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.nevaDev.padeliummarhaba.viewmodels.ProfileViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dto.GetProfileResponse
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream


@Composable
fun ProfileScreen(
    viewModel2: ProfileViewModel = hiltViewModel(),
    viewModel: GetProfileViewModel = hiltViewModel(),
    navController: NavController,
    navigateToLogin: (String) -> Unit,
    ) {
    var activated by remember { mutableStateOf(false) }
    var authorities by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var image by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var langKey by remember { mutableStateOf("") }
    var login by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current
    var profileImageUri by remember { mutableStateOf<Uri?>(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            profileImageUri = uri
        } else {
          //  Toast.makeText(context, "Image selection cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.fetchProfileData()
    }


    val profileData by viewModel.profileData.observeAsState(DataResult.Loading)
    val sessionManager = remember { SessionManager(context) }
    val isLoggedInState by rememberUpdatedState(sessionManager.isLoggedIn())




    when (val result = profileData) {
        is DataResult.Success -> {
            val profile = result.data as? GetProfileResponse
            if (profile != null && !firstName.isNotEmpty()) {
                firstName = profile.firstName
                lastName = profile.lastName
                phoneNumber = profile.phone
                activated = profile.activated
                authorities  = profile.authorities
                email  = profile.email
                langKey  = profile.langKey
                login  = profile.login
                image  = profile.image
                imageUrl  = profile.imageUrl

            } else {
                Text(text = "")
            }
        }
        is DataResult.Loading -> {
          //  Text(text = "Loading profile data...")
        }
        is DataResult.Failure -> {
          //  Text(text = "Error: ${result.errorMessage}")
        }
    }


    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Mon Profil",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.offset(x = 5.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(
            modifier = Modifier.width(900.dp).padding(horizontal = 10.dp).offset(y = -10.dp),
            color = Color.Gray, thickness = 1.dp
        )
        Spacer(modifier = Modifier.height(28.dp))

        OutlinedTextField(
            value = firstName,
            onValueChange = { firstName = it },
            label = { Text("Prénom") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(13.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = lastName,
            onValueChange = { lastName = it },
            label = { Text("Nom") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(13.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                if ((it.all { char -> char.isDigit() || char == '+' }) && it.length <= 8) {
                    if (it.count { char -> char == '+' } <= 1 && (it.indexOf('+') == 0 || !it.contains('+'))) {
                        phoneNumber = it
                    }
                }
            },
            label = { Text("Téléphone") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(13.dp),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = "Phone Icon"
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
        )

        Spacer(modifier = Modifier.height(36.dp))

        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Photo de profile",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .size(90.dp)
                    .align(Alignment.CenterHorizontally)
                    .drawBehind {
                        drawCircle(
                            color = Color.Gray.copy(alpha = 0.5f),
                            radius = size.minDimension / 3 + 2.dp.toPx(),
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
                        painter = painterResource(id = R.drawable.a123),
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {



            Button(
                onClick = {
                    val base64Image = profileImageUri?.let { getBase64FromUri(context, it) } ?: image

                    val accountData = mapOf(
                        "activated" to activated,
                        "authorities" to listOf(authorities),
                        "email" to email,
                        "firstName" to firstName,
                        "image" to base64Image,
                        "imageUrl" to imageUrl,
                        "langKey" to langKey,
                        "lastName" to lastName,
                        "login" to login,
                        "phone" to phoneNumber,
                    )
                    val accountJson = JSONObject(accountData).toString()

                    viewModel2.Profile(accountJson, profileImageUri)
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color(0xFF0066CC)
                ),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
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
            LaunchedEffect(Unit) {
                viewModel.fetchProfileData()
            }



            Spacer(modifier = Modifier.width(8.dp))
        }
    }
}
fun getBase64FromUri(context: Context, uri: Uri): String? {
    return try {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray: ByteArray = byteArrayOutputStream.toByteArray()
        Base64.encodeToString(byteArray, Base64.NO_WRAP)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
