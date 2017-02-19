package net.yslibrary.monotweety.license.domain

import net.yslibrary.licenseadapter.LicenseEntry
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.license.LicenseRepository
import rx.Single
import javax.inject.Inject

@UserScope
class GetLicenses @Inject constructor(private val licenseRepository: LicenseRepository) {
  fun execute(): Single<List<LicenseEntry>> {
    return licenseRepository.get()
  }
}