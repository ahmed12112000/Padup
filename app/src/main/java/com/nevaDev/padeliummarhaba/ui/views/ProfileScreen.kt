package com.nevaDev.padeliummarhaba.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.rememberAsyncImagePainter
import com.google.gson.Gson
import com.nevaDev.padeliummarhaba.viewmodels.GetProfileViewModel
import com.nevaDev.padeliummarhaba.viewmodels.ProfileViewModel
import com.nevadev.padeliummarhaba.R
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.dataresult.Resulta
import com.padelium.domain.dto.GetPacksResponse
import com.padelium.domain.dto.GetProfileResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull

import com.padelium.domain.dto.ProfileRequest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import org.json.JSONObject
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import java.io.ByteArrayOutputStream
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream


@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel2: ProfileViewModel = hiltViewModel(),
    viewModel: GetProfileViewModel = hiltViewModel()
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
            Toast.makeText(context, "Image selection cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    // Fetch profile data on screen load
    LaunchedEffect(Unit) {
        viewModel.fetchProfileData()
    }


    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val profileData by viewModel.profileData.observeAsState(DataResult.Loading)
    var isLoading by remember { mutableStateOf(false) }




    when (val result = profileData) {
        is DataResult.Success -> {
            // Extract profile data into variables
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
            Text(text = "Loading profile data...")
        }
        is DataResult.Failure -> {
            Text(text = "Error: ${result.errorMessage}")
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
                // Allow digits, the plus sign (+), and restrict the length to 8 characters
                if ((it.all { char -> char.isDigit() || char == '+' }) && it.length <= 8) {
                    // Ensure that the plus sign only appears at the beginning
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

        // Profile Image Section
        Column(
            modifier = Modifier
                .fillMaxSize(), // Fill the entire screen
            verticalArrangement = Arrangement.Center, // Center vertically
            horizontalAlignment = Alignment.CenterHorizontally // Center horizontally
        ) {
            Text(
                text = "Photo de profile",
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally) // Ensure the text is centered
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
                .padding(horizontal = 16.dp), // Add horizontal padding for smoother spacing
            horizontalArrangement = Arrangement.Center // Center the button horizontally
        ) {



            Button(
                onClick = {
                    // Create account JSON
                    val base64Image = profileImageUri?.let { getBase64FromUri(context, it) } ?: image

                    val accountData = mapOf(
                        "activated" to activated,
                        "authorities" to listOf(authorities), // Replace with actual authorities if needed
                        "email" to email, // Set this to the user's email if available
                        "firstName" to firstName,
                        "image" to base64Image, // Use the new image if available
                        "imageUrl" to imageUrl, // Set this to the actual image URL if available
                        "langKey" to langKey, // Set this to the actual language key if available
                        "lastName" to lastName,
                        "login" to login, // Set this to the user's login if available
                        "phone" to phoneNumber,
                    )
                    val accountJson = JSONObject(accountData).toString()
                    Log.d("ProfileUpdate", "Account JSON: $accountJson")

                    // Call the ViewModel to update the profile
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
        Base64.encodeToString(byteArray, Base64.NO_WRAP) // Encode to base64
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
