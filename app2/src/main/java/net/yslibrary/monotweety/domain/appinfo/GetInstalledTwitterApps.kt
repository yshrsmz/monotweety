package net.yslibrary.monotweety.domain.appinfo

import net.yslibrary.monotweety.data.appinfo.AppInfo
import net.yslibrary.monotweety.data.appinfo.TwitterAppRepository
import javax.inject.Inject

interface GetInstalledTwitterApps {
    suspend operator fun invoke(): List<AppInfo>
}

internal class GetInstalledTwitterAppsImpl @Inject constructor(
    private val repository: TwitterAppRepository,
) : GetInstalledTwitterApps {
    override suspend fun invoke(): List<AppInfo> {
        return repository.getInstalledApps()
    }
}
