package net.yslibrary.monotweety.data.license

import net.yslibrary.licenseadapter.LicenseEntry
import rx.Single

/**
 * Created by yshrsmz on 2016/10/10.
 */
interface LicenseRepository {
  fun get(): Single<List<LicenseEntry>>
}