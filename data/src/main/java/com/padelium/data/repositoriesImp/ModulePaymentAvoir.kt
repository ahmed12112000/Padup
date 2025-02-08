package com.padelium.data.repositoriesImp

import com.padelium.domain.repositories.IPaymentAvoirRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule19 {

    @Binds
    @Singleton
    abstract fun bindPaymentAvoirRepository(
        paymentAvoirRepositoryImp: PaymentAvoirRepositoryImp
    ): IPaymentAvoirRepository
}
