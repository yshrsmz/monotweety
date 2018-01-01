package net.yslibrary.monotweety.license

import dagger.Module
import dagger.Provides
import net.yslibrary.monotweety.base.EventBus
import net.yslibrary.monotweety.base.di.ControllerScope
import net.yslibrary.monotweety.base.di.Names
import net.yslibrary.monotweety.license.domain.GetLicenses
import javax.inject.Named

@Module
class LicenseViewModule(private val activityBus: EventBus) {

  @ControllerScope
  @Provides
  @Named(Names.FOR_ACTIVITY)
  fun provideActivityBus(): EventBus = activityBus

  @ControllerScope
  @Provides
  fun provideLicenseViewModel(getLicenses: GetLicenses): LicenseViewModel {
    return LicenseViewModel(getLicenses)
  }

  interface DependencyProvider {
    @Named(Names.FOR_ACTIVITY)
    fun activityBus(): EventBus
  }
}