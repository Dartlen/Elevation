package com.dartlen.mylibrary.di

import android.content.Context
//import com.dartlen.mylibrary.ImplMessageDataStore
//import com.dartlen.mylibrary.MessageDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MessageModule {

//    @Provides
//    fun bindsMessageDataStore(@ApplicationContext context: Context): MessageDataStore = ImplMessageDataStore(context)

}