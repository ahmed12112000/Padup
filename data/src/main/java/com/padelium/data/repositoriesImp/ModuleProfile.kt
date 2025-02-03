package com.padelium.data.repositoriesImp

import com.padelium.data.repositories.ProfileRepositoryImp
import com.padelium.domain.repositories.IProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule9 {

    @Binds
    @Singleton
    abstract fun bindProfileRepository(
        profileRepositoryImp: ProfileRepositoryImp
    ): IProfileRepository
}