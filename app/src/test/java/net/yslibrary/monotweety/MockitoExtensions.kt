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

fun verifyNoMoreInteractions(vararg mocks: Any) = Mockito.verifyNoMoreInteractions(*mocks)

fun <T : Any> spy(any: T): T = Mockito.spy(any)

fun anyLong(): Long = Mockito.anyLong()