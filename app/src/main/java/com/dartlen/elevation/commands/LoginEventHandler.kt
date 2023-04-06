package com.dartlen.elevation.commands

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.dartlen.common.network.bus.EventBus
import com.dartlen.common.network.bus.activity
import com.dartlen.mylibrary.LocationService
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginEventHandler @Inject constructor(@ActivityContext val context: Context) {
    init {
        subscribeLoginEvent(context.activity() as ComponentActivity)
    }

    suspend fun postLoginEvent(loginEvent: String) {
        EventBus.publish(loginEvent)
    }

    fun subscribeLoginEvent(lifecycleOwner: LifecycleOwner) {
        lifecycleOwner.lifecycleScope.launch {
            EventBus.subscribe<String> { command ->
               Intent(context.activity(), LocationService::class.java).also {
                    it.action = command
                    context.activity()?.startService(it)
                }
            }
        }
    }
}