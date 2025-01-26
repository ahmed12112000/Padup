package com.padelium.domain.usecases

import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IProfileRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class ProfileUseCase @Inject constructor(private val profileRepository: IProfileRepository) {

    /**
     * Updates the user profile with account details and an image file.
     *
     * @param accountJson A JSON string containing the account details (e.g., "nom", "prenom").
     * @param imagePath The file path to the image being uploaded.
     * @return A DataResult indicating the success or failure of the operation.
     */
    suspend fun Profile(accountJson: String, imagePath: String?): DataResult {
        return try {
            // Manually create the RequestBody for JSON
            val accountRequestBody: RequestBody = RequestBody.create(
                MediaType.parse("application/json"),
                accountJson
            )

            // Prepare image file
            val imageFile = imagePath?.let { File(it) }
            val imagePart = if (imageFile?.exists() == true) {
                val fileRequestBody = RequestBody.create(
                    MediaType.parse("image/*"),
                    imageFile
                )
                MultipartBody.Part.createFormData("file", imageFile.name, fileRequestBody)
            } else {
                null
            }

            // Call repository
            val response = profileRepository.Profile(accountRequestBody, imagePart)
            Log.e("ProfileUseCase", "Response code: ${response.code()}")

            if (response.isSuccessful) {
                Log.e("ProfileUseCase", "Profile update successful: ${response.code()}")
                DataResult.Success(response)
            } else {
                val errorMessage = response.errorBody()?.string() ?: "Unknown error occurred"
                Log.e("ProfileUseCase", "Error response: $errorMessage")

                DataResult.Failure(null, response.code(), errorMessage)
            }
        } catch (ex: Exception) {
            Log.e("ProfileUseCase", "Error during profile update: ${ex.localizedMessage}")

            DataResult.Failure(ex, null, ex.localizedMessage ?: "An error occurred during profile update")
        }
    }
}
