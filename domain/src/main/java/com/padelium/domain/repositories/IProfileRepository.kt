package com.padelium.domain.repositories

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

interface IProfileRepository {
    /**
     * Updates the user profile with account details and an image file.
     *
     * @param account A RequestBody containing the account details (e.g., "nom", "prenom").
     * @param file A MultipartBody.Part for the image file.
     * @return A Response<Void> indicating the success or failure of the operation.
     */
    suspend fun Profile(account: RequestBody, file: MultipartBody.Part?): Response<Void>
}

