package com.nevaDev.padeliummarhaba.viewmodels

import com.padelium.data.mappers.GetBookingMapper
import com.padelium.data.mappers.GetInitMapper
import com.padelium.data.mappers.InitBookingMapper
import com.padelium.data.mappers.KeyMapper
import com.padelium.data.mappers.SearchListMapper
import com.padelium.domain.usecase.KeyUseCase
import com.padelium.domain.usecases.GetBookingUseCase
import com.padelium.domain.usecases.GetInitUseCase
import com.padelium.domain.usecases.InitBookingUseCase
import com.padelium.domain.usecases.SearchListUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideGetBookingViewModel(
        getBookingUseCase: GetBookingUseCase,
        getBookingMapper: GetBookingMapper
    ): GetBookingViewModel {
        return GetBookingViewModel(getBookingUseCase, getBookingMapper)
    }

    @Provides
    fun provideInitBookingViewModel(
        initBookingUseCase: InitBookingUseCase,
        initBookingMapper: InitBookingMapper,
        getBookingViewModel: GetBookingViewModel // Dependency from GetBookingViewModel
    ): InitBookingViewModel {
        return InitBookingViewModel(initBookingUseCase, initBookingMapper, getBookingViewModel)
    }

    @Provides
    fun provideSearchListViewModel(
        searchListUseCase: SearchListUseCase,
        searchListMapper: SearchListMapper,
        initBookingViewModel: InitBookingViewModel // Dependency from InitBookingViewModel
    ): SearchListViewModel {
        return SearchListViewModel(searchListUseCase, searchListMapper, initBookingViewModel)
    }

@Provides
fun provideGetInitViewModel(
    getInitUseCase: GetInitUseCase,
    getInitMapper: GetInitMapper,
    searchListViewModel: SearchListViewModel
): GetInitViewModel {
    return GetInitViewModel(getInitUseCase, getInitMapper, searchListViewModel)
}
    @Provides
    fun provideKeyViewModel(
        keyUseCase: KeyUseCase,
        keyMapper: KeyMapper,
        getInitViewModel: GetInitViewModel, // Injecting GetInitViewModel
        searchListUseCase: SearchListUseCase,
        searchListMapper: SearchListMapper,
        initBookingUseCase: InitBookingUseCase,
        initBookingMapper: InitBookingMapper,
        getBookingUseCase: GetBookingUseCase,
        getBookingMapper: GetBookingMapper
    ): KeyViewModel {
        return KeyViewModel(
            keyUseCase,
            keyMapper,
            getInitViewModel,
            searchListUseCase,
            searchListMapper,
            initBookingUseCase,
            initBookingMapper,
            getBookingUseCase,
            getBookingMapper
        )
    }
}