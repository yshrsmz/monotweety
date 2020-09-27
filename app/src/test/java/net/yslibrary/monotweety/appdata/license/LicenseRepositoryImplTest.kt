package net.yslibrary.monotweety.appdata.license

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import kotlin.properties.Delegates

@RunWith(RobolectricTestRunner::class)
class LicenseRepositoryImplTest {

    var repository: LicenseRepositoryImpl by Delegates.notNull<LicenseRepositoryImpl>()

    @Before
    fun setup() {
        repository = LicenseRepositoryImpl()
    }

    @Test
    fun get() {
        repository.get().test()
            .apply {
                assertValueCount(1)
                assertComplete()
            }
    }
}
