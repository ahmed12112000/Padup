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
            .addInterceptor(logging) // Logging interceptor for request/response logging
            .addInterceptor(jSessionInterceptor) // Ensure JSessionInterceptor is added here
            .addInterceptor { chain ->
                val response = chain.proceed(chain.request())

                // Log response details for debugging
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
        return JSessionInterceptor(sharedPreferences) // Provide JSessionInterceptor to manage JSESSIONID cookies
    }
}


