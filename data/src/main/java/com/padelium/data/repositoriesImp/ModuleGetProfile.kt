package com.padelium.data.repositoriesImp

import com.padelium.domain.repositories.IGetPaymentRepository
import com.padelium.domain.repositories.IGetProfileRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule10 {

    @Binds
    @Singleton
    abstract fun bindPaymentRepository(
        getProfileRepositoryImp: GetProfileRepositoryImp
    ): IGetProfileRepository
}
