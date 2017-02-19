package net.yslibrary.monotweety.data.license

import net.yslibrary.licenseadapter.LicenseEntry
import rx.Single

interface LicenseRepository {
  fun get(): Single<List<LicenseEntry>>
}