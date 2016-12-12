package net.yslibrary.monotweety.data.user.remote

import com.google.gson.Gson
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.services.AccountService
import net.yslibrary.monotweety.*
import net.yslibrary.monotweety.data.user.User
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import retrofit2.Call
import rx.observers.TestSubscriber
import com.twitter.sdk.android.core.models.User as TwitterUser

/**
 * Created by yshrsmz on 2016/12/12.
 */
class UserRemoteRepositoryImplTest {

  lateinit var repository: UserRemoteRepositoryImpl
  lateinit var mockAccountService: AccountService
  lateinit var mockCall: Call<TwitterUser>
  lateinit var callbackCaptor: ArgumentCaptor<Callback<TwitterUser>>

  val gson = Gson()


  @Before
  fun setup() {
    mockAccountService = mock(AccountService::class)
    mockCall = mock(Call::class) as Call<TwitterUser>
    callbackCaptor = ArgumentCaptor.forClass(Callback::class.java) as ArgumentCaptor<Callback<TwitterUser>>

    repository = UserRemoteRepositoryImpl(mockAccountService)
  }

  @Test
  fun get() {
    val ts = TestSubscriber<User>()
    val user = gson.fromJson(readJsonFromAssets("user.json"), TwitterUser::class.java)
    val result = User(
        id = user.id,
        name = user.name,
        screenName = user.screenName,
        profileImageUrl = user.profileImageUrl,
        _updatedAt = -1)

    whenever(mockAccountService.verifyCredentials(anyBoolean(), anyBoolean()))
        .thenReturn(mockCall)

    repository.get().subscribe(ts)

    verify(mockCall).enqueue(callbackCaptor.capture())
    callbackCaptor.value.success(Result(user, null))

    ts.assertValue(result)
    ts.assertCompleted()
  }
}