package com.padelium.data.repositoriesImp

import com.padelium.domain.repositories.ISaveBookingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule4 {

    @Binds
    @Singleton
    abstract fun bindSaveBookingRepository(
        saveBookingRepositoryImp: SaveBookingRepositoryImp
    ): ISaveBookingRepository
}
