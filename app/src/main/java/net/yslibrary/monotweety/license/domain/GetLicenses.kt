package net.yslibrary.monotweety.license.domain

import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.license.LicenseRepository
import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/10/10.
 */
@UserScope
class GetLicenses @Inject constructor(private val licenseRepository: LicenseRepository) {
}