package net.yslibrary.monotweety.data.license

import net.yslibrary.monotweety.ConfiguredRobolectricTestRunner
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlin.properties.Delegates

@RunWith(ConfiguredRobolectricTestRunner::class)
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
