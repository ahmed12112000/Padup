package com.padelium.data.repositoriesImp


import com.padelium.domain.repositories.IErrorCreditRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule33 {

    @Binds
    @Singleton
    abstract fun bindErrorCreditRepository(
        errorCreditRepositoryImp: ErrorCreditRepositoryImp
    ): IErrorCreditRepository
}
