package com.dartlen.database.di

import android.content.Context
import androidx.room.Room
import com.dartlen.database.ElvDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providesElvDatabase(@ApplicationContext context: Context): ElvDatabase = Room.databaseBuilder(
        context,
        ElvDatabase::class.java,
        "elv-database"
    ).build()
}