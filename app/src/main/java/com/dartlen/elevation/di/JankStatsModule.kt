package com.dartlen.elevation.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ActivityComponent::class)
object MainModule {


}

@Module
@InstallIn(ViewModelComponent::class)
interface BusModule {

//    @Binds
//    fun provideCommandHandler(activity: Activity): Activity
}

