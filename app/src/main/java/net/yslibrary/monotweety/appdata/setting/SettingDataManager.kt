package net.yslibrary.monotweety.appdata.setting

import io.reactivex.Completable
import io.reactivex.Observable

interface SettingDataManager {
    fun notificationEnabled(): Observable<Boolean>
    fun notificationEnabled(enabled: Boolean)

    fun footerEnabled(): Observable<Boolean>
    fun footerEnabled(enabled: Boolean)

    fun footerText(): Observable<String>
    fun footerText(text: String)


    fun timelineAppEnabled(): Observable<Boolean>
    fun timelineAppEnabled(enabled: Boolean)

    fun timelineAppPackageName(): Observable<String>
    fun timelineAppPackageName(packageName: String)

    fun clear(): Completable
}
