package net.yslibrary.monotweety.data.session

import com.twitter.sdk.android.core.SessionManager
import com.twitter.sdk.android.core.TwitterSession
import net.yslibrary.monotweety.mock
import net.yslibrary.monotweety.verify
import net.yslibrary.monotweety.whenever
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

  @Before
  fun setup() {
    mockSessionManager = mock(SessionManager::class) as SessionManager<TwitterSession>
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