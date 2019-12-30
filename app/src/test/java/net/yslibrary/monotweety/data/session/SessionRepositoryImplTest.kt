package net.yslibrary.monotweety.data.session

import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterSession
import io.mockk.MockKAnnotations
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import org.junit.Before
import org.junit.Test

class SessionRepositoryImplTest {

    @MockK
    lateinit var mockSessionManager: SessionManager<TwitterSession>
    lateinit var repository: SessionRepositoryImpl

    @Suppress("UNCHECKED_CAST")
    @Before
    fun setup() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        repository = SessionRepositoryImpl(mockSessionManager)
    }

    @Test
    fun getActiveSession() {
        every { mockSessionManager.activeSession } returns null

        repository.getActiveSession().test()
            .apply {
                assertValueCount(1)
                assertComplete()

                verify {
                    mockSessionManager.activeSession
                }

                confirmVerified(mockSessionManager)
            }
    }
}
