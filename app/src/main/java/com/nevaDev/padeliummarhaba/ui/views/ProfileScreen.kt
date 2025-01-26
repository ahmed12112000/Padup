package com.nevaDev.padeliummarhaba.ui.views

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
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
import java.io.FileInputStream
import java.io.FileOutputStream


@Composable
fun ProfileScreen(
    onLogout: () -> Unit,
    viewModel2: ProfileViewModel = hiltViewModel(),
    viewModel: GetProfileViewModel = hiltViewModel()
) {
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
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

    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val profileData by viewModel.profileData.observeAsState(DataResult.Loading)
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.GetProfile()
    }

    when (val result = profileData) {
        is DataResult.Success -> {
            // Extract profile data into variables
            val profile = result.data as? GetProfileResponse
            if (profile != null && !firstName.isNotEmpty()) {
                firstName = profile.firstName
                lastName = profile.lastName
                phoneNumber = profile.phone
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

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            BasicTextField(
                value = phoneNumber,
                onValueChange = {
                    // Allow digits, the plus sign (+), and restrict the length to 8 characters
                    if ((it.all { char -> char.isDigit() || char == '+' }) && it.length <= 8) {
                        // Ensure that the plus sign only appears at the beginning
                        if (it.count { char -> char == '+' } <= 1 && (it.indexOf('+') == 0 || !it.contains(
                                '+'
                            ))) {
                            phoneNumber = it
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(x = 3.dp)
                    .width(200.dp)
                    .padding(vertical = 4.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Phone Icon
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Phone Icon",
                            modifier = Modifier.padding(8.dp)
                        )
                        androidx.compose.material.Text(
                            text = "|",
                            fontSize = 29.sp,
                            color = Color.Black,
                            modifier = Modifier.offset(x = -8.dp, y = -2.dp)
                        )
                        Box(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            innerTextField()
                        }


                    }
                }
            )
        }
            Spacer(modifier = Modifier.width(10.dp))

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
                  //  .offset(x = 100.dp)
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
                    // Create the account JSON
                    val accountData = mapOf(
                        "firstName" to firstName,
                        "lastName" to lastName,
                        "phone" to phoneNumber
                    )
                    val accountJson = JSONObject(accountData).toString()

                    // Get the image URI from somewhere (e.g., image picker or file selection)
                    val uri: Uri? = profileImageUri // Assuming profileImageUri is already defined and contains the URI

                    // Handle URI if available
                    if (uri != null) {
                        val contentResolver = context.contentResolver
                        val fileDescriptor = contentResolver.openFileDescriptor(uri, "r") ?: return@Button
                        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
                        val file = File(context.cacheDir, "uploaded_image.jpg")
                        val outputStream = FileOutputStream(file)
                        inputStream.copyTo(outputStream)
                        inputStream.close()
                        outputStream.close()

                        // Get the absolute path of the image
                        val imagePath = file.absolutePath

                        // Log the path for debugging
                        Log.d("ProfileUpdate", "Image path: $imagePath")

                        // Prepare the file part for upload
                        val fileRequestBody = file.asRequestBody("image/*".toMediaType())
                        val filePart = MultipartBody.Part.createFormData("file", file.name, fileRequestBody)

                        // Call the Profile update method
                        viewModel2.Profile(accountJson, filePart.toString())

                        // Observe the result
                        viewModel2.dataResult.observe(lifecycleOwner) { dataResult ->
                            when (dataResult) {
                                is DataResult.Loading -> isLoading = true
                                is DataResult.Success -> {
                                    isLoading = false
                                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                                }
                                is DataResult.Failure -> {
                                    isLoading = false
                                    Toast.makeText(context, "Failed to update profile: ${dataResult.errorMessage}", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        // Handle the case where no image was selected
                        Log.d("ProfileUpdate", "No image selected")
                        // Proceed without file upload if necessary


                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.White,
                    contentColor = Color(0xFF0066CC)
                ),
                modifier = Modifier
                    .weight(1f) // Ensure the button is evenly distributed within the row
                    .padding(horizontal = 8.dp) // Add smooth padding around the button
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
    }
}

@SuppressLint("Range")
fun getRealPathFromURI(context: Context, uri: Uri): String? {
    val cursor = context.contentResolver.query(uri, null, null, null, null)
    cursor?.moveToFirst()
    val path = cursor?.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
    cursor?.close()
    return path
}
