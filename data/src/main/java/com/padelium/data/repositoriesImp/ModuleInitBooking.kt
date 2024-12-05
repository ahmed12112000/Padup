package com.padelium.data.repositoriesImp

import com.padelium.domain.repositories.IInitBookingRepository
import com.padelium.domain.repositories.ISearchListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule2 {

    @Binds
    @Singleton
    abstract fun bindInitBookingRepository(
        initBookingRepositorylmp: InitBookingRepositorylmp
    ): IInitBookingRepository
}