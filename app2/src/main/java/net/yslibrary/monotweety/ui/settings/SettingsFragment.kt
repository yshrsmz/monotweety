package net.yslibrary.monotweety.ui.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.databinding.FragmentSettingsBinding
import net.yslibrary.monotweety.ui.base.ViewBindingFragment
import net.yslibrary.monotweety.ui.di.HasComponent
import net.yslibrary.monotweety.ui.di.ViewModelFactory
import net.yslibrary.monotweety.ui.di.getComponentProvider
import net.yslibrary.monotweety.ui.settings.widget.UserItem
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

    private val adapter = GroupAdapter<GroupieViewHolder>()

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
        binding.list.layoutManager = LinearLayoutManager(requireContext())
        binding.list.adapter = adapter
        adapter.add(UserItem())
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
