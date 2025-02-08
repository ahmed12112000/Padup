package com.padelium.data.repositoriesImp


import com.padelium.domain.repositories.IPaymentPartRepository
import com.padelium.domain.repositories.IPaymentRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule35 {

    @Binds
    @Singleton
    abstract fun bindPaymentPartRepository(
        paymentPartRepositoryImp: PaymentPartRepositoryImp
    ): IPaymentPartRepository
}

