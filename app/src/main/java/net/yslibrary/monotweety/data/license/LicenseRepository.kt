package net.yslibrary.monotweety.data.license

import net.yslibrary.licenseadapter.Library
import rx.Single

interface LicenseRepository {
  fun get(): Single<List<Library>>
}