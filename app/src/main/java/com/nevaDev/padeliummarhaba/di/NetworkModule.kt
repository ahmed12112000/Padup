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
import com.nevaDev.padeliummarhaba.di.JSessionInterceptor

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
        csrfInterceptor: CsrfInterceptor,
        authInterceptor: AuthInterceptor,
        jSessionInterceptor: JSessionInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Logging interceptor for request/response logging
            .addInterceptor(csrfInterceptor)    // CSRF Interceptor for token handling
            .addInterceptor(authInterceptor)    // Add AuthInterceptor for Authorization token
            .addInterceptor(jSessionInterceptor) // Add JSessionInterceptor to manage cookies
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

    @Provides
    fun provideAuthInterceptor(sharedPreferences: SharedPreferences): AuthInterceptor {
        return AuthInterceptor(sharedPreferences) // Provide AuthInterceptor to manage Authorization token
    }

    @Provides
    fun provideJSessionInterceptor(sharedPreferences: SharedPreferences): JSessionInterceptor {
        return JSessionInterceptor(sharedPreferences) // Provide JSessionInterceptor to manage JSESSIONID cookies
    }
}

