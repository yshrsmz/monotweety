package net.yslibrary.monotweety.license

import net.yslibrary.licenseadapter.Library
import net.yslibrary.monotweety.license.domain.GetLicenses
import rx.Single

class LicenseViewModel(private val getLicenses: GetLicenses) {
  val licenses: Single<List<Library>>
    get() = getLicenses.execute()
}