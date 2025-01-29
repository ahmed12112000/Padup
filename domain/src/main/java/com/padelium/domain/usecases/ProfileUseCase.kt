package com.padelium.domain.usecases

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IProfileRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class ProfileUseCase @Inject constructor(private val profileRepository: IProfileRepository) {

    suspend fun Profile(accountJson: String, imageUri: Uri?, context: Context): DataResult {
        return try {
            Log.d("ProfileUseCase", "Received accountJson: $accountJson")
            Log.d("ProfileUseCase", "Received imageUri: $imageUri")

            // Convert accountJson string to RequestBody
            val accountRequestBody: RequestBody = RequestBody.create(
                MediaType.parse("application/json"),
                accountJson
            )
            Log.d("ProfileUseCase", "Account JSON as RequestBody created")

            // Only create the image part if imageUri is not null
            val imagePart = if (imageUri != null) {
                val imageFile = uriToFile(imageUri, context)
                if (imageFile?.exists() == true) {
                    val fileRequestBody = RequestBody.create(
                        MediaType.parse("image/*"),
                        imageFile
                    )
                    Log.d("ProfileUseCase", "File exists at: ${imageFile.absolutePath}")
                    MultipartBody.Part.createFormData("file", imageFile.name, fileRequestBody)
                } else {
                    Log.e("ProfileUseCase", "Image file does not exist or could not be created.")
                    null
                }
            } else {
                Log.d("ProfileUseCase", "No image selected, proceeding without an image part.")
                null
            }

            // Call repository for profile update
            val response = profileRepository.Profile(accountRequestBody, imagePart)

            if (response.isSuccessful) {
                Log.d("ProfileUseCase", "Profile updated successfully")
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                Log.e("ProfileUseCase", "Error during profile update: $errorMessage")
                DataResult.Failure(null, response.code(), errorMessage)
            }

        } catch (ex: Exception) {
            Log.e("ProfileUseCase", "Error during profile update: ${ex.localizedMessage}", ex)
            DataResult.Failure(ex, null, "Error during profile update: ${ex.localizedMessage}")
        }
    }
}


fun uriToFile(uri: Uri, context: Context): File? {
    return try {
        val inputStream = context.contentResolver.openInputStream(uri)
        if (inputStream == null) {
            Log.e("ProfileUseCase", "Input stream is null for URI: $uri")
            return null
        }

        val tempFile = File.createTempFile("temp_image", null, context.cacheDir)
        inputStream.use { input ->
            FileOutputStream(tempFile).use { output ->
                input.copyTo(output)
            }
        }
        tempFile
    } catch (e: Exception) {
        Log.e("ProfileUseCase", "Failed to convert URI to file: ${e.localizedMessage}", e)
        null
    }
}




