package com.nevaDev.padeliummarhaba.retrofit

import android.content.SharedPreferences
import com.nevaDev.padeliummarhaba.repository.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private lateinit var sharedPreferences: SharedPreferences

    private val mHttpLoggingInterceptor = HttpLoggingInterceptor()
        .setLevel(HttpLoggingInterceptor.Level.BODY)

    private val mOkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(mHttpLoggingInterceptor) // Uncomment if you want logging
            .build()
    }

    private var mRetrofit: Retrofit? = null

    fun init(sharedPrefs: SharedPreferences) {
        sharedPreferences = sharedPrefs
    }

    val client: Retrofit?
        get() {
            if (mRetrofit == null) {
                mRetrofit = Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(mOkHttpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return mRetrofit
        }
}
