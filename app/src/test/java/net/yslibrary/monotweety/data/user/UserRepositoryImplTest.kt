package net.yslibrary.monotweety.data.user

import com.gojuno.koptional.None
import com.google.gson.Gson
import com.nhaarman.mockito_kotlin.*
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import net.yslibrary.monotweety.base.Clock
import net.yslibrary.monotweety.data.user.local.UserLocalRepository
import net.yslibrary.monotweety.data.user.remote.UserRemoteRepository
import net.yslibrary.monotweety.readJsonFromAssets
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.util.concurrent.TimeUnit
import com.twitter.sdk.android.core.models.User as TwitterUser

@RunWith(JUnit4::class)
class UserRepositoryImplTest {

    lateinit var clock: Clock
    lateinit var localRepository: UserLocalRepository
    lateinit var remoteRepository: UserRemoteRepository
    lateinit var repository: UserRepositoryImpl

    val gson = Gson()

    @Before
    fun setup() {
        clock = mock<Clock>()
        localRepository = mock<UserLocalRepository>()
        remoteRepository = mock<UserRemoteRepository>()
        repository = UserRepositoryImpl(remoteRepository = remoteRepository,
            localRepository = localRepository,
            clock = clock)
    }

    @Test
    fun get() {
        whenever(localRepository.getById(any()))
            .thenReturn(Flowable.just(None))

        repository.get(1).test()
            .apply {
                assertValue(None)

                verify(localRepository).getById(1)
                verifyNoMoreInteractions(localRepository, remoteRepository)
            }
    }

    @Test
    fun set() {
        val time = System.currentTimeMillis()
        val twitterUser = gson.fromJson(readJsonFromAssets("user.json"), TwitterUser::class.java)
        val user = User(id = twitterUser.id,
            name = twitterUser.name,
            screenName = twitterUser.screenName,
            profileImageUrl = twitterUser.profileImageUrl,
            _updatedAt = time)
        whenever(localRepository.set(user)).thenReturn(Completable.complete())

        repository.set(user).test()
            .apply {
                assertNoValues()
                assertComplete()

                verify(localRepository).set(user)
                verifyNoMoreInteractions(localRepository, remoteRepository)
            }
    }

    @Test
    fun delete() {
        whenever(localRepository.delete(any())).thenReturn(Completable.complete())

        repository.delete(1234).test()
            .apply {
                assertNoValues()
                assertComplete()

                verify(localRepository).delete(1234)
                verifyNoMoreInteractions(localRepository, remoteRepository)
            }
    }

    @Test
    fun fetch() {
        val time = System.currentTimeMillis()
        val twitterUser = gson.fromJson(readJsonFromAssets("user.json"), TwitterUser::class.java)
        val user = User(id = twitterUser.id,
            name = twitterUser.name,
            screenName = twitterUser.screenName,
            profileImageUrl = twitterUser.profileImageUrl,
            _updatedAt = time)

        whenever(remoteRepository.get())
            .thenReturn(Single.just(user.copy(_updatedAt = -1)))
        whenever(localRepository.set(user)).thenReturn(Completable.complete())

        whenever(clock.currentTimeMillis()).thenReturn(time)

        repository.fetch().test()
            .apply {
                assertNoValues()
                assertComplete()

                verify(remoteRepository).get()
                verify(localRepository).set(user)
                verifyNoMoreInteractions(remoteRepository, localRepository)
            }
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
