package net.yslibrary.monotweety.license.domain

import io.reactivex.Single
import net.yslibrary.licenseadapter.Library
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.license.LicenseRepository
import javax.inject.Inject

@UserScope
class GetLicenses @Inject constructor(private val licenseRepository: LicenseRepository) {
    fun execute(): Single<List<Library>> {
        return licenseRepository.get()
    }
}
