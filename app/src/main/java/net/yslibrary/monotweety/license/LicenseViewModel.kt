package net.yslibrary.monotweety.license

import io.reactivex.Single
import net.yslibrary.licenseadapter.Library
import net.yslibrary.monotweety.license.domain.GetLicenses

class LicenseViewModel(private val getLicenses: GetLicenses) {
    val licenses: Single<List<Library>>
        get() = getLicenses.execute()
}
