package net.yslibrary.monotweety.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Section
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import net.yslibrary.monotweety.BuildConfig
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.databinding.FragmentSettingsBinding
import net.yslibrary.monotweety.ui.base.ViewBindingFragment
import net.yslibrary.monotweety.ui.di.HasComponent
import net.yslibrary.monotweety.ui.di.ViewModelFactory
import net.yslibrary.monotweety.ui.di.getComponentProvider
import net.yslibrary.monotweety.ui.settings.widget.DividerItemDecoration
import net.yslibrary.monotweety.ui.settings.widget.OneLineTextItem
import net.yslibrary.monotweety.ui.settings.widget.SubHeaderItem
import net.yslibrary.monotweety.ui.settings.widget.TwoLineTextItem
import net.yslibrary.monotweety.ui.settings.widget.UserItem
import timber.log.Timber
import javax.inject.Inject

class SettingsFragment : ViewBindingFragment<FragmentSettingsBinding>(
    R.layout.fragment_settings,
    FragmentSettingsBinding::bind,
), HasComponent<SettingsFragmentComponent> {

    override val component: SettingsFragmentComponent by lazy {
        requireActivity()
            .getComponentProvider<SettingsFragmentComponent.ComponentProvider>()
            .settingsFragmentComponent()
            .build()
    }

    @Inject
    lateinit var factory: ViewModelFactory<SettingsViewModel>

    private val viewModel: SettingsViewModel by viewModels { factory }

    private val userSection by lazy { Section(SubHeaderItem(getString(R.string.account))) }
    private val settingsSection by lazy { Section(SubHeaderItem(getString(R.string.settings))) }
    private val privacySection by lazy {
        Section(SubHeaderItem(getString(R.string.privacy)))
            .apply {
                add(OneLineTextItem(
                    item = OneLineTextItem.Item(
                        title = getString(R.string.privacy_policy),
                        enabled = true,
                    ),
                    onClick = {}
                ))
            }
    }
    private val othersSection by lazy {
        Section(SubHeaderItem(getString(R.string.others)))
            .apply {
                addAll(listOf(
                    TwoLineTextItem(
                        item = TwoLineTextItem.Item(
                            title = getString(R.string.version,
                                BuildConfig.VERSION_NAME,
                                BuildConfig.VERSION_CODE),
                            subTitle = getString(R.string.version_description),
                            enabled = true
                        ),
                        onClick = {}
                    ),
                    OneLineTextItem(
                        item = OneLineTextItem.Item(
                            title = getString(R.string.license),
                            enabled = true
                        ),
                        onClick = {}
                    )
                ))
            }
    }
    private val supportSection by lazy {
        Section(SubHeaderItem(getString(R.string.support_developer)))
            .apply {
                addAll(listOf(
                    TwoLineTextItem(
                        item = TwoLineTextItem.Item(
                            title = getString(R.string.follow_developer),
                            subTitle = getString(R.string.follow_developer_description),
                            enabled = true,
                        ),
                        onClick = {}
                    ),
                    TwoLineTextItem(
                        item = TwoLineTextItem.Item(
                            title = getString(R.string.share),
                            subTitle = getString(R.string.share_description),
                            enabled = true
                        ),
                        onClick = {}
                    ),
                    OneLineTextItem(
                        item = OneLineTextItem.Item(
                            title = getString(R.string.rate_on_store),
                            enabled = true
                        ),
                        onClick = {}
                    ),
                    OneLineTextItem(
                        item = OneLineTextItem.Item(
                            title = getString(R.string.star_github),
                            enabled = true
                        ),
                        onClick = {}
                    ),
                ))
            }
    }

    private val adapter by lazy {
        GroupAdapter<GroupieViewHolder>().apply {
            addAll(listOf(
                userSection,
                settingsSection,
                privacySection,
                othersSection,
                supportSection,
            ))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        viewLifecycleOwnerLiveData.observe(this) { owner ->
            bindEffects(owner.lifecycleScope)
            bindStates(owner.lifecycleScope)
        }
        viewModel.dispatch(SettingsIntent.Initialize)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.notificationSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            Timber.d("notificationSwitch: $isChecked")
            viewModel.dispatch(SettingsIntent.NotificationStateUpdated(enabled = isChecked))
        }

        binding.list.let { list ->
            (list.itemAnimator as? DefaultItemAnimator)?.supportsChangeAnimations = false
            list.layoutManager = LinearLayoutManager(requireContext())
            list.addItemDecoration(DividerItemDecoration(requireContext()))
            list.adapter = adapter
        }
    }

    private fun bindEffects(scope: LifecycleCoroutineScope) {
        scope.launchWhenStarted {
            viewModel.effects
                .onEach { effect -> handleEffect(effect) }
                .collect()
        }
    }

    private fun bindStates(scope: LifecycleCoroutineScope) {
        scope.launchWhenStarted {
            viewModel.states
                .onEach { state -> render(state) }
                .collect()
        }
    }

    private fun handleEffect(effect: SettingsEffect) {

    }

    private fun render(state: SettingsState) {
        Timber.d("newState: $state")
        userSection.update(listOf(UserItem(state.user)))
        settingsSection.update(
            listOf(
                OneLineTextItem(OneLineTextItem.Item("title", enabled = true)) {}
            ),
        )

        val notiEnabled = state.settings?.notificationEnabled == true
        val notiStateResId = if (notiEnabled) R.string.label_on else R.string.label_off
        binding.notificationSwitch.text =
            getString(R.string.label_notification_state, getString(notiStateResId))
        binding.notificationSwitch.isChecked = notiEnabled
    }
}
