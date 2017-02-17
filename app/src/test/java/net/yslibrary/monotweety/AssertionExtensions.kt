package net.yslibrary.monotweety

import org.assertj.core.api.*

fun <T> assertThat(actual: T): AbstractObjectAssert<*, T> = Assertions.assertThat(actual)

fun assertThat(actual: Int): AbstractIntegerAssert<*> = Assertions.assertThat(actual)

fun assertThat(actual: Boolean): AbstractBooleanAssert<*> = Assertions.assertThat(actual)

fun assertThat(actual: String): AbstractCharSequenceAssert<*, String> = Assertions.assertThat(actual)

fun <T> assertThat(actual: List<T>): ListAssert<T> = Assertions.assertThat(actual)
