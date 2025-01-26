package com.padelium.data.repositoriesImp

import com.padelium.domain.repositories.IGetProfileByIdRepository
import com.padelium.domain.repositories.IGetReservationRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule27 {

    @Binds
    @Singleton
    abstract fun bindGetProfileByIdRepository(
        getProfileByIdRepositoryImp: GetProfileByIdRepositoryImp
    ): IGetProfileByIdRepository
}