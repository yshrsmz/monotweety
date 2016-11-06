package net.yslibrary.monotweety.setting.domain

import net.yslibrary.monotweety.base.di.AppScope
import net.yslibrary.monotweety.data.setting.SettingDataManager
import rx.Observable
import rx.lang.kotlin.PublishSubject
import rx.subjects.PublishSubject

import javax.inject.Inject

/**
 * Created by yshrsmz on 2016/11/05.
 */
@AppScope
class FooterStateManager @Inject constructor(private val settingDataManager: SettingDataManager) {

  private val subject: PublishSubject<Unit> = PublishSubject()

  fun get(): Observable<FooterState> {
    return subject
        .startWith(Unit)
        .switchMap {
          Observable.zip(
              settingDataManager.footerEnabled(),
              settingDataManager.footerText(),
              ::FooterState).first()
        }
  }

  fun set(state: FooterState) {
    settingDataManager.footerEnabled(state.enabled)
    settingDataManager.footerText(state.text.trim())
    subject.onNext(Unit)
  }

  data class FooterState(val enabled: Boolean,
                         val text: String)
}