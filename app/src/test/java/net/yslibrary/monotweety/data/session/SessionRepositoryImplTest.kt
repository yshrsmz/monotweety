package net.yslibrary.monotweety.data.session

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import com.nhaarman.mockito_kotlin.whenever
import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterSession
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import rx.observers.TestSubscriber
import kotlin.properties.Delegates

/**
 * Created by yshrsmz on 2016/12/06.
 */
@RunWith(JUnit4::class)
class SessionRepositoryImplTest {

  var mockSessionManager: SessionManager<TwitterSession> by Delegates.notNull<SessionManager<TwitterSession>>()
  var repository: SessionRepositoryImpl by Delegates.notNull<SessionRepositoryImpl>()

  @Suppress("UNCHECKED_CAST")
  @Before
  fun setup() {
    mockSessionManager = mock<SessionManager<TwitterSession>>()
    repository = SessionRepositoryImpl(mockSessionManager)
  }

  @Test
  fun getActiveSession() {
    val ts = TestSubscriber<TwitterSession?>()
    whenever(mockSessionManager.activeSession).thenReturn(null)

    repository.getActiveSession().subscribe(ts)

    ts.assertValueCount(1)
    ts.assertCompleted()

    verify(mockSessionManager).activeSession
  }
}