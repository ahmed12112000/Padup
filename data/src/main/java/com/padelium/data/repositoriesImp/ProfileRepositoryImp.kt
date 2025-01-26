package com.padelium.data.repositories

import com.padelium.domain.repositories.IProfileRepository
import com.padelium.data.datasource.remote.PadeliumApi
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import retrofit2.Response

class ProfileRepositoryImp @Inject constructor(
    private val api: PadeliumApi
) : IProfileRepository {

    override suspend fun Profile(account: RequestBody, file: MultipartBody.Part?): Response<Void> {
        // Account data is passed as a RequestBody, which is the expected format for the account information
        // Prepare the account JSON
        val accountJson = account.toString() // This may be unnecessary, as you are already passing RequestBody

        // Prepare filePart for the file (MultipartBody.Part)
        val filePart = file.toString()

        // Call the API to update the profile with account data and file (if it exists)
        return api.Profile(accountJson, filePart)
    }
}


