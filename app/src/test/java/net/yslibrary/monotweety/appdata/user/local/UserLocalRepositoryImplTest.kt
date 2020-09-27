package net.yslibrary.monotweety.data.user.local

import com.gojuno.koptional.toOptional
import com.google.common.truth.Truth.assertThat
import com.google.gson.Gson
import com.pushtorefresh.storio3.sqlite.StorIOSQLite
import net.yslibrary.monotweety.data.local.LocalModule
import net.yslibrary.monotweety.data.local.singleObject
import net.yslibrary.monotweety.data.local.withObject
import net.yslibrary.monotweety.data.user.User
import net.yslibrary.monotweety.readJsonFromAssets
import net.yslibrary.monotweety.targetApplication
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.properties.Delegates
import com.twitter.sdk.android.core.models.User as TwitterUser

@RunWith(RobolectricTestRunner::class)
class UserLocalRepositoryImplTest {

    var storio by Delegates.notNull<StorIOSQLite>()

    var repository by Delegates.notNull<UserLocalRepositoryImpl>()

    val gson = Gson()

    @Before
    fun setup() {
        val module = LocalModule()

        storio = module.provideStorIOSQLite(module.provideDbOpenHelper(targetApplication))

        repository = UserLocalRepositoryImpl(storio)
    }

    @Test
    fun getById() {
        val twitterUser = gson.fromJson(readJsonFromAssets("user.json"), TwitterUser::class.java)
        val time = System.currentTimeMillis()
        val user = User(
            id = twitterUser.id,
            name = twitterUser.name,
            screenName = twitterUser.screenName,
            profileImageUrl = twitterUser.profileImageUrl,
            _updatedAt = time
        )

        storio.put().withObject(user).prepare().executeAsBlocking()

        repository.getById(user.id).test()
            .apply {
                awaitCount(1)

                assertValue(user.toOptional())
                assertNotComplete()
            }
    }

    @Test
    fun set() {
        val twitterUser = gson.fromJson(readJsonFromAssets("user.json"), TwitterUser::class.java)
        val time = System.currentTimeMillis()
        val user = User(
            id = twitterUser.id,
            name = twitterUser.name,
            screenName = twitterUser.screenName,
            profileImageUrl = twitterUser.profileImageUrl,
            _updatedAt = time
        )

        repository.set(user).test()
            .apply {
                awaitTerminalEvent()
                assertNoValues()
                assertComplete()
            }

        val result = storio.get()
            .singleObject(User::class.java)
            .withQuery(UserTable.queryById(user.id))
            .prepare().executeAsBlocking()

        assertThat(result).isEqualTo(user)
    }

    @Test
    fun delete() {
        val twitterUser = gson.fromJson(readJsonFromAssets("user.json"), TwitterUser::class.java)
        val time = System.currentTimeMillis()
        val user = User(
            id = twitterUser.id,
            name = twitterUser.name,
            screenName = twitterUser.screenName,
            profileImageUrl = twitterUser.profileImageUrl,
            _updatedAt = time
        )

        storio.put().withObject(user).prepare().executeAsBlocking()

        repository.delete(user.id).test()
            .apply {
                awaitTerminalEvent()
                assertNoValues()
                assertComplete()
            }

        val result = storio.get().numberOfResults()
            .withQuery(UserTable.queryById(user.id))
            .prepare()
            .executeAsBlocking()

        assertThat(result).isEqualTo(0)
    }
}
