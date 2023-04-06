package com.dartlen.database.di

import com.dartlen.database.ElvDatabase
import com.dartlen.database.dao.LocationDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {

    @Provides
    fun providesLocationDao(database: ElvDatabase): LocationDao = database.locationDao()
}