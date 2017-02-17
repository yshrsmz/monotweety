package net.yslibrary.monotweety.license

import net.yslibrary.licenseadapter.LicenseEntry
import net.yslibrary.monotweety.license.domain.GetLicenses
import rx.Single

class LicenseViewModel(private val getLicenses: GetLicenses) {
  val licenses: Single<List<LicenseEntry>>
    get() = getLicenses.execute()
}