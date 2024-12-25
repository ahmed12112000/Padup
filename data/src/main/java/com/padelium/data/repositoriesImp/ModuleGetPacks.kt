package com.padelium.data.repositoriesImp

import com.padelium.domain.repositories.IGetBookingRepository
import com.padelium.domain.repositories.IGetPacksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule11 {

    @Binds
    @Singleton
    abstract fun bindGetBookingRepository(
        getPacksRepositoryImp: GetPacksRepositoryImp
    ): IGetPacksRepository
}