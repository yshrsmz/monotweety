package net.yslibrary.monotweety.data.license

import net.yslibrary.licenseadapter.LicenseEntry
import net.yslibrary.monotweety.ConfiguredRobolectricTestRunner
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import rx.observers.TestSubscriber
import kotlin.properties.Delegates

/**
 * Created by yshrsmz on 2016/12/06.
 */
@RunWith(ConfiguredRobolectricTestRunner::class)
class LicenseRepositoryImplTest {

  var repository: LicenseRepositoryImpl by Delegates.notNull<LicenseRepositoryImpl>()

  @Before
  fun setup() {
    repository = LicenseRepositoryImpl()
  }

  @Test
  fun get() {
    val ts = TestSubscriber<List<LicenseEntry>>()

    repository.get().subscribe(ts)

    ts.assertValueCount(1)
    ts.assertCompleted()
  }
}