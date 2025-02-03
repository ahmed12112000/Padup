package com.padelium.domain.repositories

import android.content.Context
import android.net.Uri
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface IProfileRepository {
    /**
     * Updates the user profile with account details and an image file.
     *
     * @param context The context needed to access the Uri and convert it into a file.
     * @param accountJson A JSON string containing the account details (e.g., "nom", "prenom").
     * @param imageUri A Uri pointing to the image file to be uploaded (or null if no image).
     * @return A Response<Void> indicating the success or failure of the operation.
     */
    suspend fun Profile(context: Context, accountJson: String, imageUri: Uri?): Response<Void> }

