package com.padelium.data.repositories

import android.content.Context
import android.net.Uri
import android.util.Log
import com.padelium.domain.repositories.IProfileRepository
import com.padelium.data.datasource.remote.PadeliumApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject
import retrofit2.Response
import java.io.FileOutputStream




class ProfileRepositoryImp @Inject constructor(
    private val api: PadeliumApi
) : IProfileRepository {

    override suspend fun Profile(context: Context, accountJson: String, imageUri: Uri?): Response<Void> {
        // Call the API with the account JSON and the image URI
        return api.Profile(accountJson, imageUri)
    }
}




