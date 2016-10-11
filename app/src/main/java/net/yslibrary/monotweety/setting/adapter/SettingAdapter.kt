package net.yslibrary.monotweety.setting.adapter

import android.content.res.Resources
import com.hannesdorfmann.adapterdelegates3.ListDelegationAdapter
import net.yslibrary.monotweety.BuildConfig
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.data.user.User

/**
 * Created by yshrsmz on 2016/10/07.
 */
class SettingAdapter(res: Resources, listener: Listener) : ListDelegationAdapter<List<SettingAdapter.Item>>() {

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

    delegatesManager.addDelegate(SwitchAdapterDelegate(object : SwitchAdapterDelegate.Listener {
      override fun onClick(item: SwitchAdapterDelegate.Item, checked: Boolean) {
        when (item.type) {
          ViewType.KEEP_DIALOG_OPEN -> listener.onKeepDialogOpenClick(checked)
          else -> {
            // no-op
          }
        }
      }
    }))

    delegatesManager.addDelegate(OneLineTextAdapterDelegate(object : OneLineTextAdapterDelegate.Listener {
      override fun onClick(item: OneLineTextAdapterDelegate.Item) {
        when (item.type) {
          ViewType.HOWTO -> listener.onHowtoClick()
          ViewType.LICENSE -> listener.onLicenseClick()
          ViewType.GOOGLE_PLAY -> listener.onGooglePlayClick()
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
          else -> {
            // no-op
          }
        }
      }
    }))

    items = mutableListOf(
        SubHeaderAdapterDelegate.Item(res.getString(R.string.label_account), ViewType.SUBHEADER_ACCOUNT),
        ProfileAdapterDelegate.Item.empty(),
        SubHeaderAdapterDelegate.Item(res.getString(R.string.label_setting), ViewType.SUBHEADER_SETTING),
        SwitchAdapterDelegate.Item(res.getString(R.string.label_keep_dialog), false, false, ViewType.KEEP_DIALOG_OPEN),
        SubHeaderAdapterDelegate.Item(res.getString(R.string.label_others), ViewType.SUBHEADER_OTHERS),
        OneLineTextAdapterDelegate.Item(res.getString(R.string.label_howto), true, ViewType.HOWTO),
        TwoLineTextAdapterDelegate.Item(res.getString(R.string.label_app_version, BuildConfig.VERSION_NAME, BuildConfig.VERSION_CODE),
            res.getString(R.string.sub_label_app_version), true, ViewType.APP_VERSION),
        OneLineTextAdapterDelegate.Item(res.getString(R.string.label_license), true, ViewType.LICENSE),
        TwoLineTextAdapterDelegate.Item(res.getString(R.string.label_developer),
            res.getString(R.string.sub_label_developer), true, ViewType.DEVELOPER),
        OneLineTextAdapterDelegate.Item(res.getString(R.string.label_googleplay), true, ViewType.GOOGLE_PLAY)
    )
  }

  fun updateProfile(user: User) {
    (items as MutableList).set(ViewType.PROFILE.ordinal, ProfileAdapterDelegate.Item.from(user))
    notifyItemChanged(ViewType.PROFILE.ordinal)
  }

  fun updateKeepDialogOpen(enabled: Boolean) {
    val item = items[ViewType.KEEP_DIALOG_OPEN.ordinal] as SwitchAdapterDelegate.Item
    if (item.checked == enabled && item.enabled) {
      return
    }
    (items as MutableList).set(ViewType.KEEP_DIALOG_OPEN.ordinal, item.copy(checked = enabled, enabled = true))
    notifyItemChanged(ViewType.KEEP_DIALOG_OPEN.ordinal)
  }

  enum class ViewType {
    SUBHEADER_ACCOUNT,
    PROFILE,
    SUBHEADER_SETTING,
    KEEP_DIALOG_OPEN,
    SUBHEADER_OTHERS,
    HOWTO,
    APP_VERSION,
    LICENSE,
    DEVELOPER,
    GOOGLE_PLAY;
  }

  interface Item {
    val type: ViewType
  }

  interface Listener {
    fun onOpenProfileClick()
    fun onLogoutClick()
    fun onKeepDialogOpenClick(enabled: Boolean)
    fun onHowtoClick()
    fun onAppVersionClick()
    fun onLicenseClick()
    fun onDeveloperClick()
    fun onGooglePlayClick()
  }
}