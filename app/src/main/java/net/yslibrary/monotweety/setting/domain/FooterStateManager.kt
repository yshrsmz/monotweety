package net.yslibrary.monotweety.setting.domain

import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.PublishSubject
import net.yslibrary.monotweety.appdata.setting.SettingDataManager
import net.yslibrary.monotweety.base.di.AppScope
import javax.inject.Inject

@AppScope
class FooterStateManager @Inject constructor(
    private val settingDataManager: SettingDataManager
) {

    private val subject: PublishSubject<Unit> = PublishSubject.create()

    fun get(): Observable<State> {
        return subject
            .startWith(Unit)
            .switchMapSingle {
                Observable.zip(
                    settingDataManager.footerEnabled(),
                    settingDataManager.footerText(),
                    BiFunction { enabled: Boolean, footer: String -> State(enabled, footer) }
                ).firstOrError()
            }
    }

    fun set(state: State) {
        settingDataManager.footerEnabled(state.enabled)
        settingDataManager.footerText(state.text.trim())
        subject.onNext(Unit)
    }

    data class State(
        val enabled: Boolean,
        val text: String
    )
}
