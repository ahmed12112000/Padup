package com.nevaDev.padeliummarhaba.di

import android.content.Context
import android.content.SharedPreferences
import com.nevaDev.padeliummarhaba.repository.Constant
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@InstallIn(SingletonComponent::class)
@Module
object NetworkModule {

    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences("your_prefs_name", Context.MODE_PRIVATE)
    }

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constant.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        jSessionInterceptor: JSessionInterceptor
    ): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // Increase connection timeout
            .readTimeout(60, TimeUnit.SECONDS)    // Increase read timeout
            .writeTimeout(60, TimeUnit.SECONDS)   // Increase write timeout
            .addInterceptor(logging)
            .addInterceptor(jSessionInterceptor)
            .addInterceptor { chain ->
                val response = chain.proceed(chain.request())

                println("Response Code: ${response.code}")
                println("Response Body: ${response.peekBody(2048).string()}")

                response
            }
            .build()
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideJSessionInterceptor(sharedPreferences: SharedPreferences): JSessionInterceptor {
        return JSessionInterceptor(sharedPreferences)
    }
}


