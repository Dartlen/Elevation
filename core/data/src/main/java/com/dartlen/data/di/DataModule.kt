package com.dartlen.data.di

import com.dartlen.data.ImplLocationRepository
import com.dartlen.data.LocationRepository
import com.dartlen.data.OfflineFirstUserDataRepository
import com.dartlen.data.UserDataRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindLocationRepository(locationRepository: ImplLocationRepository): LocationRepository

    @Binds
    fun bindsUserDataRepository(userDataRepository: OfflineFirstUserDataRepository): UserDataRepository

}