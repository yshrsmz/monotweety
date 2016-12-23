package net.yslibrary.monotweety.data.user.local

import com.google.gson.Gson
import com.pushtorefresh.storio.sqlite.StorIOSQLite
import net.yslibrary.monotweety.ConfiguredRobolectricTestRunner
import net.yslibrary.monotweety.assertThat
import net.yslibrary.monotweety.data.local.LocalModule
import net.yslibrary.monotweety.data.local.singleObject
import net.yslibrary.monotweety.data.local.withObject
import net.yslibrary.monotweety.data.user.User
import net.yslibrary.monotweety.readJsonFromAssets
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import rx.observers.TestSubscriber
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.properties.Delegates
import com.twitter.sdk.android.core.models.User as TwitterUser

/**
 * Created by yshrsmz on 2016/12/09.
 */
@RunWith(ConfiguredRobolectricTestRunner::class)
class UserLocalRepositoryImplTest {

  var storio by Delegates.notNull<StorIOSQLite>()

  var repository by Delegates.notNull<UserLocalRepositoryImpl>()

  val gson = Gson()

  @Before
  fun setup() {
    val module = LocalModule()

    storio = module.provideStorIOSQLite(module.provideDbOpenHelper(RuntimeEnvironment.application))

    repository = UserLocalRepositoryImpl(storio)
  }

  @Test
  fun getById() {
    val ts = TestSubscriber<User>()
    val latch = CountDownLatch(1)
    val twitterUser = gson.fromJson(readJsonFromAssets("user.json"), TwitterUser::class.java)
    val time = System.currentTimeMillis()
    val user = User(id = twitterUser.id,
        name = twitterUser.name,
        screenName = twitterUser.screenName,
        profileImageUrl = twitterUser.profileImageUrl,
        _updatedAt = time)

    storio.put().withObject(user).prepare().executeAsBlocking()

    repository.getById(user.id)
        .test()
        .awaitValueCount(1, 3, TimeUnit.SECONDS)
        .assertValue(user)
        .assertNotCompleted()
  }

  @Test
  fun set() {
    val twitterUser = gson.fromJson(readJsonFromAssets("user.json"), TwitterUser::class.java)
    val time = System.currentTimeMillis()
    val user = User(id = twitterUser.id,
        name = twitterUser.name,
        screenName = twitterUser.screenName,
        profileImageUrl = twitterUser.profileImageUrl,
        _updatedAt = time)

    repository.set(user)
        .test()
        .awaitTerminalEvent()
        .assertNoValues()
        .assertCompleted()

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
    val user = User(id = twitterUser.id,
        name = twitterUser.name,
        screenName = twitterUser.screenName,
        profileImageUrl = twitterUser.profileImageUrl,
        _updatedAt = time)

    storio.put().withObject(user).prepare().executeAsBlocking()

    repository.delete(user.id)
        .test()
        .awaitTerminalEvent()
        .assertNoValues()
        .assertCompleted()

    val result = storio.get().numberOfResults()
        .withQuery(UserTable.queryById(user.id))
        .prepare()
        .executeAsBlocking()

    assertThat(result).isEqualTo(0)
  }
}