package net.yslibrary.monotweety

import org.mockito.Mockito
import org.mockito.stubbing.OngoingStubbing
import kotlin.reflect.KClass

/**
 * Created by yshrsmz on 2016/10/20.
 */

fun <T : Any> mock(clazz: KClass<T>): T = Mockito.mock(clazz.java)

fun <T> whenever(methodCall: T): OngoingStubbing<T> = Mockito.`when`(methodCall)

fun <T> verify(methodCall: T): T = Mockito.verify(methodCall)