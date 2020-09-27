package net.yslibrary.monotweety.appdata.license

import io.reactivex.Single
import net.yslibrary.licenseadapter.Library

interface LicenseRepository {
    fun get(): Single<List<Library>>
}
