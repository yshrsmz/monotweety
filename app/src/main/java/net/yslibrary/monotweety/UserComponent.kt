package net.yslibrary.monotweety

import dagger.Subcomponent
import net.yslibrary.monotweety.analytics.Analytics
import net.yslibrary.monotweety.base.di.UserScope
import net.yslibrary.monotweety.data.UserDataModule
import net.yslibrary.monotweety.license.domain.GetLicenses
import net.yslibrary.monotweety.login.domain.DoLogout
import net.yslibrary.monotweety.setting.domain.*
import net.yslibrary.monotweety.status.domain.CheckStatusLength
import net.yslibrary.monotweety.status.domain.ClearPreviousStatus
import net.yslibrary.monotweety.status.domain.GetPreviousStatus
import net.yslibrary.monotweety.status.domain.UpdateStatus
import net.yslibrary.monotweety.user.domain.GetUser

@UserScope
@Subcomponent(
    modules = arrayOf(UserDataModule::class)
)
interface UserComponent : AppModule.Provider {

  fun notificationEnabledManager(): NotificationEnabledManager

  fun keepOpenManager(): KeepOpenManager

  fun footerStateManager(): FooterStateManager

  fun checkStatusLength(): CheckStatusLength

  fun updateStatus(): UpdateStatus

  fun getPreviousStatus(): GetPreviousStatus

  fun clearPreviousStatus(): ClearPreviousStatus

  fun getUser(): GetUser

  fun doLogout(): DoLogout

  fun getLicenses(): GetLicenses

  fun getInstalledSupportedApps(): GetInstalledSupportedApps

  fun selectedTimelineAppInfoManager(): SelectedTimelineAppInfoManager

  fun analytics(): Analytics

  interface ComponentProvider {
    fun userComponent(): UserComponent
  }
}