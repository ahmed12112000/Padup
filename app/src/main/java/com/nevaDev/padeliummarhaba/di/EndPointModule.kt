package com.nevaDev.padeliummarhaba.di


import com.padelium.data.datasource.remote.PadeliumEndPoint
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@InstallIn(SingletonComponent::class)
@Module
object EndPointModule {

    @Provides
    fun providePadeliumEndPoint(retrofit: Retrofit): PadeliumEndPoint{
        return retrofit.create(PadeliumEndPoint::class.java)
    }
}