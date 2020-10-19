package net.yslibrary.monotweety.base

import kotlin.coroutines.CoroutineContext

interface CoroutineDispatchers {
    val main: CoroutineContext
    val background: CoroutineContext
}
