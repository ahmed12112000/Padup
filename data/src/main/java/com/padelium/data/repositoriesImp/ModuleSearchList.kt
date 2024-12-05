package com.padelium.data.repositoriesImp

import com.padelium.domain.repositories.ISearchListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule1 {

    @Binds
    @Singleton
    abstract fun bindGetInitRepository(
        searchListRepositoryImp: SearchListRepositoryImp
    ): ISearchListRepository
}

