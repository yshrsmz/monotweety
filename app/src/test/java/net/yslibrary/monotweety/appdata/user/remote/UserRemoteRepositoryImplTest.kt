package net.yslibrary.monotweety.appdata.user.remote

import com.google.gson.Gson
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.Result
import com.twitter.sdk.android.core.services.AccountService
import io.mockk.CapturingSlot
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import io.mockk.verify
import net.yslibrary.monotweety.appdata.user.User
import net.yslibrary.monotweety.readJsonFromAssets
import org.junit.Before
import org.junit.Test
import retrofit2.Call
import com.twitter.sdk.android.core.models.User as TwitterUser

class UserRemoteRepositoryImplTest {

    lateinit var repository: UserRemoteRepositoryImpl

    @MockK
    lateinit var mockAccountService: AccountService

    @MockK
    lateinit var mockCall: Call<TwitterUser>
    lateinit var callbackCaptor: CapturingSlot<Callback<TwitterUser>>

    val gson = Gson()


    @Suppress("UNCHECKED_CAST")
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        callbackCaptor = slot()

        repository = UserRemoteRepositoryImpl(mockAccountService)
    }

    @Test
    fun get() {
        val user = gson.fromJson(readJsonFromAssets("user.json"), TwitterUser::class.java)
        val result = User(
            id = user.id,
            name = user.name,
            screenName = user.screenName,
            profileImageUrl = user.profileImageUrlHttps,
            _updatedAt = -1
        )

        every { mockAccountService.verifyCredentials(any(), any(), any()) } returns mockCall
        every { mockCall.enqueue(capture(callbackCaptor)) } returns Unit

        repository.get().test()
            .apply {

                verify {
                    mockAccountService.verifyCredentials(false, true, false)
                    mockCall.enqueue(callbackCaptor.captured)
                }
                confirmVerified(mockAccountService, mockCall)

                callbackCaptor.captured.success(Result(user, null))

                assertValue(result)
                assertComplete()
            }
    }
}
