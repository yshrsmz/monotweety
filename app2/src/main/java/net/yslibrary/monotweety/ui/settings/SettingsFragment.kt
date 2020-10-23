package net.yslibrary.monotweety.ui.settings

import android.os.Bundle
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.databinding.FragmentSettingsBinding
import net.yslibrary.monotweety.ui.base.ViewBindingFragment
import net.yslibrary.monotweety.ui.di.HasComponent
import net.yslibrary.monotweety.ui.di.ViewModelFactory
import net.yslibrary.monotweety.ui.di.getComponentProvider
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        viewLifecycleOwnerLiveData.observe(this) { owner ->
            bindEffects(owner.lifecycleScope)
            bindStates(owner.lifecycleScope)
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

    }
}
