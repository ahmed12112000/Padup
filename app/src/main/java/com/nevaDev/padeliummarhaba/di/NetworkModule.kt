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
            .client(okHttpClient) // Add the OkHttpClient with the CSRF interceptor
            .build()
    }

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor, csrfInterceptor: CsrfInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Logging interceptor for request/response logging
            .addInterceptor(csrfInterceptor)    // CSRF Interceptor for token handling
            .build()
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    fun provideCsrfInterceptor(sharedPreferences: SharedPreferences): CsrfInterceptor {
        return CsrfInterceptor(sharedPreferences) // Pass SharedPreferences to the CsrfInterceptor
    }
}
