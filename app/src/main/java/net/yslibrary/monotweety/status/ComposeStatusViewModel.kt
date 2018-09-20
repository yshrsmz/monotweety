package net.yslibrary.monotweety.status

import com.twitter.sdk.android.core.TwitterApiException
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Singles
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import net.yslibrary.monotweety.Config
import net.yslibrary.monotweety.setting.domain.FooterStateManager
import net.yslibrary.monotweety.status.domain.CheckStatusLength
import net.yslibrary.monotweety.status.domain.UpdateStatus
import timber.log.Timber

class ComposeStatusViewModel(status: String,
                             private val config: Config,
                             private val checkStatusLength: CheckStatusLength,
                             private val updateStatus: UpdateStatus,
                             private val footerStateManager: FooterStateManager) {

    private val disposables = CompositeDisposable()

    private val isSendableStatusSubject = BehaviorSubject.createDefault(false)

    private val statusInfoSubject = BehaviorSubject.create<StatusInfo>()

    private val statusUpdatedSubject = PublishSubject.create<Unit>()

    private val progressEventsSubject = BehaviorSubject.createDefault(ProgressEvent.FINISHED)

    private val messagesSubject = PublishSubject.create<String>()

    private val allowCloseViewSubject = BehaviorSubject.createDefault(false)

    val isSendableStatus: Observable<Boolean>
        get() = isSendableStatusSubject

    val statusInfo: Observable<StatusInfo>
        get() = statusInfoSubject

    val statusUpdated: Observable<Unit>
        get() = statusUpdatedSubject

    val progressEvents: Observable<ProgressEvent>
        get() = progressEventsSubject

    val messages: Observable<String>
        get() = messagesSubject

    val allowCloseView: Observable<Boolean>
        get() = allowCloseViewSubject

    val statusMaxLength: Single<Int>
        get() = Single.just(config.statusMaxLength)

    val closeViewRequests: Observable<Unit>
        get() {
            return statusUpdatedSubject
                .map { Unit }
        }

    val footerState: Observable<FooterStateManager.State>
        get() = footerStateManager.get()

    val canClose: Boolean
        get() {
            val isSending = progressEventsSubject.value == ProgressEvent.IN_PROGRESS
            val hasContent = statusInfoSubject.value!!.length > 0
            val allowCloseView = allowCloseViewSubject.value!!

            return !isSending && (!hasContent || allowCloseView)
        }

    init {

        footerStateManager.get().firstElement()
            .subscribe {
                val statusText = if (it.enabled)
                    "$status ${it.text}"
                else
                    status
                onStatusChanged(statusText)
            }
            .let(disposables::add)
    }

    fun onStatusChanged(status: String) {
        checkStatusLength.execute(status)
            .subscribeOn(Schedulers.io())
            .subscribeBy {
                statusInfoSubject.onNext(StatusInfo(status, it.valid, it.length, it.maxLength))

                isSendableStatusSubject.onNext(it.valid)
            }
    }

    fun onSendStatus() {
        Singles.zip(
            isSendableStatus.firstOrError(),
            statusInfo.firstOrError()
        ) { _, (status) -> status }
            .doOnSuccess { progressEventsSubject.onNext(ProgressEvent.IN_PROGRESS) }
            .flatMapCompletable { updateStatus.execute(it) }
            .doOnComplete {
                Timber.d("status updated - complete")
                statusUpdatedSubject.onNext(Unit)
            }
            .doOnTerminate { progressEventsSubject.onNext(ProgressEvent.FINISHED) }
            .subscribeBy(onError = { t ->
                Timber.e(t)
                if (t is TwitterApiException && t.errorMessage != null) {
                    messagesSubject.onNext(t.errorMessage)
                } else {
                    messagesSubject.onNext("Something wrong happened")
                }
            })
    }

    fun onConfirmCloseView(allowCloseView: Boolean) {
        allowCloseViewSubject.onNext(allowCloseView)
    }

    fun onDestroy() {
        disposables.dispose()
    }

    data class StatusInfo(val status: String, val valid: Boolean, val length: Int, val maxLength: Int)

    enum class ProgressEvent {
        IN_PROGRESS,
        FINISHED
    }
}
