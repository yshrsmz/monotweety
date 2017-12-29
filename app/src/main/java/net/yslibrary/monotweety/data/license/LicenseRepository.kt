package net.yslibrary.monotweety.data.license

import io.reactivex.Single
import net.yslibrary.licenseadapter.Library

interface LicenseRepository {
  fun get(): Single<List<Library>>
}