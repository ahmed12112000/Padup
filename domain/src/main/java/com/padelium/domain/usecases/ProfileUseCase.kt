package com.padelium.domain.usecases

import android.content.Context
import android.net.Uri
import android.util.Log
import com.padelium.domain.dataresult.DataResult
import com.padelium.domain.repositories.IProfileRepository
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


class ProfileUseCase @Inject constructor(
    private val profileRepository: IProfileRepository
) {

    suspend fun Profile(accountJson: String, imageUri: Uri?, context: Context): DataResult {
        return try {
            Log.d("ProfileUseCase", "Received accountJson: $accountJson")
            Log.d("ProfileUseCase", "Received imageUri: $imageUri")

            // Call repository for profile update
            val response = profileRepository.Profile(context, accountJson, imageUri)

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










