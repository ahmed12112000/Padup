package com.padelium.data.repositoriesImp


import com.padelium.domain.repositories.IPaymentGetAvoirRepository
import com.padelium.domain.repositories.IPaymentPartBookingRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule31 {

    @Binds
    @Singleton
    abstract fun bindPaymentPartBookingRepository(
        paymentPartBookingRepositoryImp: PaymentPartBookingRepositoryImp
    ): IPaymentPartBookingRepository
}
