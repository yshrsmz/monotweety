package net.yslibrary.monotweety.data.user

import net.yslibrary.monotweety.base.Clock
import net.yslibrary.monotweety.data.user.local.UserLocalRepository
import net.yslibrary.monotweety.data.user.remote.UserRemoteRepository
import net.yslibrary.monotweety.mock
import net.yslibrary.monotweety.whenever
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.concurrent.TimeUnit

/**
 * Created by yshrsmz on 2016/10/23.
 */
@RunWith(JUnit4::class)
class UserRepositoryImplTest {

  lateinit var clock: Clock
  lateinit var localRepository: UserLocalRepository
  lateinit var remoteRepository: UserRemoteRepository
  lateinit var repository: UserRepositoryImpl

  @Before
  fun setup() {
    clock = mock(Clock::class)
    localRepository = mock(UserLocalRepository::class)
    remoteRepository = mock(UserRemoteRepository::class)
    repository = UserRepositoryImpl(remoteRepository = remoteRepository,
        localRepository = localRepository,
        clock = clock)
  }

  @Test
  fun isValid_valid() {
    val timestamp = System.currentTimeMillis()
    val before11hours = timestamp - TimeUnit.HOURS.toMillis(11)
    whenever(clock.currentTimeMillis()).thenReturn(timestamp)

    val user = User(id = 1,
        name = "test_name",
        screenName = "test_screen_name",
        profileImageUrl = "http://test.com/profile.jpg",
        _updatedAt = before11hours)

    Assertions.assertThat(repository.isValid(user)).isTrue()
  }

  @Test
  fun isValid_null_user() {
    Assertions.assertThat(repository.isValid(null)).isFalse()
  }

  @Test
  fun isValid_outdated() {
    val timestamp = System.currentTimeMillis()
    val before13hours = timestamp - TimeUnit.HOURS.toMillis(13)
    whenever(clock.currentTimeMillis()).thenReturn(timestamp)

    val user = User(id = 1,
        name = "test_name",
        screenName = "test_screen_name",
        profileImageUrl = "http://test.com/profile.jpg",
        _updatedAt = before13hours)

    Assertions.assertThat(repository.isValid(user)).isFalse()
  }
}