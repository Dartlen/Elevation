package com.dartlen.mylibrary.di

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.dartlen.elevation.core.designsystem.R
import com.google.android.gms.location.FusedLocationProviderClient
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    // We need this client to access user location
    fun provideFusedLocationProviderClient(@ApplicationContext context: Context) = FusedLocationProviderClient(context)

    @ServiceScoped
    @Provides
    // This is the activity where our service belongs to
    fun providePendingIntent(@ApplicationContext context: Context) = PendingIntent.getActivity(
        context, 0, Intent(context, Activity::class.java).also {
            it.action = "ACTION_SHOW_TRACKING_ACTIVITY"
        }, PendingIntent.FLAG_IMMUTABLE
    )

    @ServiceScoped
    @Provides
    // Building the actual notification that will be displayed
    fun provideBaseNotificationBuilder(@ApplicationContext context: Context, pendingIntent: PendingIntent) = NotificationCompat.Builder(context, "1")
        .setAutoCancel(false)
        .setOngoing(true)
        .setSmallIcon(R.drawable.landscape)
        .setContentTitle("Elevation")
        .setContentText("00:00:00")
        .setContentIntent(pendingIntent)


}