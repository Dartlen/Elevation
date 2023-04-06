package com.dartlen.common.network.bus

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.qualifiers.ActivityContext
import kotlinx.coroutines.GlobalScope.coroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterIsInstance
import javax.inject.Inject

object EventBus {
    private val _events = MutableSharedFlow<Any>()
    val events = _events.asSharedFlow()

    suspend fun publish(event: Any) {
        _events.emit(event)
    }

    suspend inline fun <reified T> subscribe(crossinline onEvent: (T) -> Unit) {
        events.filterIsInstance<T>()
            .collectLatest { event ->
                coroutineContext.ensureActive()
                onEvent(event)
            }
    }
}

tailrec fun Context?.activity(): Activity? = this as? Activity
    ?: (this as? ContextWrapper)?.baseContext?.activity()