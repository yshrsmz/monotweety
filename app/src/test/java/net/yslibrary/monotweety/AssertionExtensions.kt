package net.yslibrary.monotweety

import org.assertj.core.api.AbstractIntegerAssert
import org.assertj.core.api.AbstractObjectAssert
import org.assertj.core.api.Assertions

/**
 * Created by yshrsmz on 2016/10/20.
 */
fun <T> assertThat(actual: T): AbstractObjectAssert<*, T> = Assertions.assertThat(actual)

fun assertThat(actual: Int): AbstractIntegerAssert<*> = Assertions.assertThat(actual)