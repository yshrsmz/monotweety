package net.yslibrary.monotweety.ui.di

import android.app.Activity

interface HasComponent<C> {
    val component: C
}

@Suppress("UNCHECKED_CAST")
fun <P> Activity.getComponentProvider(): P {
    return (this as HasComponent<P>).component
}
