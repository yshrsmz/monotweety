package net.yslibrary.monotweety.setting.adapter

import android.content.res.Resources
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import net.yslibrary.monotweety.BuildConfig
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.data.appinfo.AppInfo
import net.yslibrary.monotweety.data.user.User

/**
 * Created by yshrsmz on 2016/10/07.
 */
class SettingAdapter(private val res: Resources, listener: Listener) : ListDelegationAdapter<List<SettingAdapter.Item>>() {

  init {

    delegatesManager.addDelegate(SubHeaderAdapterDelegate())

    delegatesManager.addDelegate(ProfileAdapterDelegate(object : ProfileAdapterDelegate.Listener {
      override fun onLogoutClick() {
        listener.onLogoutClick()
      }

      override fun onOpenProfileClick() {
        listener.onOpenProfileClick()
      }
    }))

    delegatesManager.addDelegate(TwoLineSwitchAdapterDelegate(object : TwoLineSwitchAdapterDelegate.Listener {
      override fun onClick(item: TwoLineSwitchAdapterDelegate.Item, checked: Boolean) {
        when (item.type) {
          ViewType.KEEP_OPEN -> listener.onKeepOpenClick(checked)
          else -> {
            // no-op
          }
        }
      }
    }))

    delegatesManager.addDelegate(OneLineTextAdapterDelegate(object : OneLineTextAdapterDelegate.Listener {
      override fun onClick(item: OneLineTextAdapterDelegate.Item) {
        when (item.type) {
          ViewType.LICENSE -> listener.onLicenseClick()
          ViewType.GOOGLE_PLAY -> listener.onGooglePlayClick()
          ViewType.GITHUB -> listener.onGitHubClick()
          else -> {
            // no-op
          }
        }
      }
    }))

    delegatesManager.addDelegate(TwoLineTextAdapterDelegate(object : TwoLineTextAdapterDelegate.Listener {
      override fun onClick(item: TwoLineTextAdapterDelegate.Item) {
        when (item.type) {
          ViewType.APP_VERSION -> listener.onAppVersionClick()
          ViewType.DEVELOPER -> listener.onDeveloperClick()
          ViewType.SHARE -> listener.onShareClick()
          else -> {
            // no-op
          }
        }
      }
    }))

    delegatesManager.addDelegate(FooterEditorAdapterDelegate(object : FooterEditorAdapterDelegate.Listener {
      override fun onFooterUpdated(enabled: Boolean, footerText: String) {
        listener.onFooterStateChanged(enabled, footerText)
      }
    }))

    delegatesManager.addDelegate(TimelineAppAdapterDelegate(object : TimelineAppAdapterDelegate.Listener {
      override fun onTimelineAppChanged(selectedApp: AppInfo) {
        listener.onTimelineAppChanged(selectedApp)
      }
    }))

    items = mutableListOf(
        SubHeaderAdapterDelegate.Item(res.getString(R.string.label_account), ViewType.SUBHEADER_ACCOUNT),
        ProfileAdapterDelegate.Item.empty(),
        SubHeaderAdapterDelegate.Item(res.getString(R.string.label_setting), ViewType.SUBHEADER_SETTING),
        TwoLineSwitchAdapterDelegate.Item(res.getString(R.string.label_keep),
            res.getString(R.string.sub_label_keep), false, false, ViewType.KEEP_OPEN),
        FooterEditorAdapterDelegate.Item(true, false, "", ViewType.FOOTER),
        TimelineAppAdapterDelegate.Item(true, emptyList(), AppInfo.empty()),
        SubHeaderAdapterDelegate.Item(res.getString(R.string.label_others), ViewType.SUBHEADER_OTHERS),
        TwoLineTextAdapterDelegate.Item(res.getString(R.string.label_app_version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE),
            res.getString(R.string.sub_label_app_version), true, ViewType.APP_VERSION),
        OneLineTextAdapterDelegate.Item(res.getString(R.string.label_license), true, ViewType.LICENSE),
        SubHeaderAdapterDelegate.Item(res.getString(R.string.label_support_developer), ViewType.SUBHEADER_DEVELOPER),
        TwoLineTextAdapterDelegate.Item(res.getString(R.string.label_developer),
            res.getString(R.string.sub_label_developer), true, ViewType.DEVELOPER),
        TwoLineTextAdapterDelegate.Item(res.getString(R.string.label_share), res.getString(R.string.sub_label_share), true, ViewType.SHARE),
        OneLineTextAdapterDelegate.Item(res.getString(R.string.label_googleplay), true, ViewType.GOOGLE_PLAY),
        OneLineTextAdapterDelegate.Item(res.getString(R.string.label_github), true, ViewType.GITHUB)
    )
  }

  fun updateProfile(user: User) {
    (items as MutableList).set(ViewType.PROFILE.ordinal, ProfileAdapterDelegate.Item.from(user))
    notifyItemChanged(ViewType.PROFILE.ordinal)
  }

  fun updateKeepOpen(enabled: Boolean) {
    val item = items[ViewType.KEEP_OPEN.ordinal] as TwoLineSwitchAdapterDelegate.Item
    if (item.checked == enabled && item.enabled) {
      return
    }
    (items as MutableList).set(ViewType.KEEP_OPEN.ordinal, item.copy(checked = enabled, enabled = true))
    notifyItemChanged(ViewType.KEEP_OPEN.ordinal)
  }

  fun updateFooterState(enabled: Boolean, footerText: String) {
    val item = items[ViewType.FOOTER.ordinal] as FooterEditorAdapterDelegate.Item
    (items as MutableList).set(ViewType.FOOTER.ordinal, item.copy(enabled = true, checked = enabled, footerText = footerText))
    notifyItemChanged(ViewType.FOOTER.ordinal)
  }

  fun updateTimelineApp(selectedApp: AppInfo, apps: List<AppInfo>) {
    val item = items[ViewType.TIMELINE_APP.ordinal] as TimelineAppAdapterDelegate.Item
    (items as MutableList).set(ViewType.TIMELINE_APP.ordinal, item.copy(enabled = true, selectedApp = selectedApp, apps = apps))
    notifyItemChanged(ViewType.TIMELINE_APP.ordinal)
  }

  enum class ViewType {
    SUBHEADER_ACCOUNT,
    PROFILE,
    SUBHEADER_SETTING,
    KEEP_OPEN,
    FOOTER,
    TIMELINE_APP,
    SUBHEADER_OTHERS,
    APP_VERSION,
    LICENSE,
    SUBHEADER_DEVELOPER,
    DEVELOPER,
    SHARE,
    GOOGLE_PLAY,
    GITHUB;
  }

  interface Item {
    val type: ViewType
  }

  interface Listener {
    fun onOpenProfileClick()
    fun onLogoutClick()
    fun onKeepOpenClick(enabled: Boolean)
    fun onFooterStateChanged(enabled: Boolean, text: String)
    fun onTimelineAppChanged(selectedApp: AppInfo)
    fun onAppVersionClick()
    fun onLicenseClick()
    fun onDeveloperClick()
    fun onShareClick()
    fun onGooglePlayClick()
    fun onGitHubClick()
  }
}