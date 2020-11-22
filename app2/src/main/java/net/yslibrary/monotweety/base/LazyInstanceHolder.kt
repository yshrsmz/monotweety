package net.yslibrary.monotweety.base

class LazyInstanceHolder<in P, out T>(val function: (P) -> T) {
    private var instance: T? = null
    fun get(param: P): T {
        return instance ?: function(param).also { instance = it }
    }
}

fun <P, T> lazyWithParam(function: (P) -> T): Lazy<LazyInstanceHolder<P, T>> {
    return lazy { LazyInstanceHolder(function) }
}
