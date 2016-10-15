package net.yslibrary.monotweety.status

import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.Config
import net.yslibrary.monotweety.base.di.ControllerScope
import net.yslibrary.monotweety.setting.domain.KeepDialogOpenManager
import net.yslibrary.monotweety.status.domain.CheckStatusLength
import net.yslibrary.monotweety.status.domain.GetPreviousStatus
import net.yslibrary.monotweety.status.domain.UpdateStatus

/**
 * Created by yshrsmz on 2016/10/02.
 */
@Module
class ComposeStatusViewModule(private val status: String?) {

  @ControllerScope
  @Provides
  fun provideComposeStatusViewModel(config: Config,
                                    checkStatusLength: CheckStatusLength,
                                    updateStatus: UpdateStatus,
                                    getPreviousStatus: GetPreviousStatus,
                                    keepDialogOpenManager: KeepDialogOpenManager): ComposeStatusViewModel {
    val _status: String = if (status.isNullOrBlank()) "" else status!!
    return ComposeStatusViewModel(_status,
        config,
        checkStatusLength,
        updateStatus,
        getPreviousStatus,
        keepDialogOpenManager)
  }
}