package net.yslibrary.monotweety.appdata.user

import com.gojuno.koptional.None
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single
import net.yslibrary.monotweety.appdata.user.local.UserLocalRepository
import net.yslibrary.monotweety.appdata.user.remote.UserRemoteRepository
import net.yslibrary.monotweety.base.Clock
import net.yslibrary.monotweety.readJsonFromAssets
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit
import com.twitter.sdk.android.core.models.User as TwitterUser

class UserRepositoryImplTest {

    @MockK
    lateinit var clock: Clock

    @MockK
    lateinit var localRepository: UserLocalRepository

    @MockK
    lateinit var remoteRepository: UserRemoteRepository
    lateinit var repository: UserRepositoryImpl

    val gson = Gson()

    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)

        repository = UserRepositoryImpl(
            remoteRepository = remoteRepository,
            localRepository = localRepository,
            clock = clock
        )
    }

    @Test
    fun get() {

        every { localRepository.getById(any()) } returns Flowable.just(None)

        repository.get(1).test()
            .apply {
                assertValue(None)

                verify {
                    localRepository.getById(1)
                }
                confirmVerified(localRepository, remoteRepository, clock)
            }
    }

    @Test
    fun set() {
        val time = System.currentTimeMillis()
        val twitterUser = gson.fromJson(readJsonFromAssets("user.json"), TwitterUser::class.java)
        val user = User(
            id = twitterUser.id,
            name = twitterUser.name,
            screenName = twitterUser.screenName,
            profileImageUrl = twitterUser.profileImageUrlHttps,
            _updatedAt = time
        )

        every { localRepository.set(user) } returns Completable.complete()

        repository.set(user).test()
            .apply {
                assertNoValues()
                assertComplete()

                verify {
                    localRepository.set(user)
                }

                confirmVerified(localRepository, remoteRepository, clock)
            }
    }

    @Test
    fun delete() {
        every { localRepository.delete(any()) } returns Completable.complete()

        repository.delete(1234).test()
            .apply {
                assertNoValues()
                assertComplete()

                verify {
                    localRepository.delete(1234)
                }
                confirmVerified(localRepository, remoteRepository, clock)
            }
    }

    @Test
    fun fetch() {
        val time = System.currentTimeMillis()
        val twitterUser = gson.fromJson(readJsonFromAssets("user.json"), TwitterUser::class.java)
        val user = User(
            id = twitterUser.id,
            name = twitterUser.name,
            screenName = twitterUser.screenName,
            profileImageUrl = twitterUser.profileImageUrl,
            _updatedAt = time
        )

        every { remoteRepository.get() } returns Single.just(user.copy(_updatedAt = -1))
        every { localRepository.set(user) } returns Completable.complete()
        every { clock.currentTimeMillis() } returns time

        repository.fetch().test()
            .apply {
                assertNoValues()
                assertComplete()

                verify {
                    remoteRepository.get()
                    localRepository.set(user)
                    clock.currentTimeMillis()
                }
                confirmVerified(remoteRepository, localRepository, clock)
            }
    }

    @Test
    fun isValid_valid() {
        val timestamp = System.currentTimeMillis()
        val before11hours = timestamp - TimeUnit.HOURS.toMillis(11)

        every { clock.currentTimeMillis() } returns timestamp

        val user = User(
            id = 1,
            name = "test_name",
            screenName = "test_screen_name",
            profileImageUrl = "https://test.com/profile.jpg",
            _updatedAt = before11hours
        )

        assertThat(repository.isValid(user)).isTrue()
    }

    @Test
    fun isValid_null_user() {
        assertThat(repository.isValid(null)).isFalse()
    }

    @Test
    fun isValid_outdated() {
        val timestamp = System.currentTimeMillis()
        val before13hours = timestamp - TimeUnit.HOURS.toMillis(13)

        every { clock.currentTimeMillis() } returns timestamp

        val user = User(
            id = 1,
            name = "test_name",
            screenName = "test_screen_name",
            profileImageUrl = "https://test.com/profile.jpg",
            _updatedAt = before13hours
        )

        assertThat(repository.isValid(user)).isFalse()
    }
}
