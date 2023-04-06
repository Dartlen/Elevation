package com.dartlen.common.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val niaDispatcher: NiaDispatchers)

enum class NiaDispatchers {
    IO,
}
