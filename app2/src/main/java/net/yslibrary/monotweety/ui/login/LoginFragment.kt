package net.yslibrary.monotweety.ui.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.analytics.Analytics
import net.yslibrary.monotweety.databinding.FragmentLoginBinding
import net.yslibrary.monotweety.ui.base.ViewBindingFragment
import net.yslibrary.monotweety.ui.base.navigateSafe
import net.yslibrary.monotweety.ui.base.setDebounceClickListener
import net.yslibrary.monotweety.ui.di.HasComponent
import net.yslibrary.monotweety.ui.di.ViewModelFactory
import net.yslibrary.monotweety.ui.di.getComponentProvider
import javax.inject.Inject

class LoginFragment : ViewBindingFragment<FragmentLoginBinding>(
    R.layout.fragment_login,
    FragmentLoginBinding::bind
), HasComponent<LoginFragmentComponent> {

    override val component: LoginFragmentComponent by lazy {
        requireActivity().getComponentProvider<LoginFragmentComponent.ComponentProvider>()
            .loginFragmentComponent()
            .build()
    }

    @Inject
    lateinit var factory: ViewModelFactory<LoginViewModel>

    @Inject
    lateinit var analytics: Analytics

    private val viewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        viewLifecycleOwnerLiveData.observe(this) { owner ->
            bindEffects(owner)
            bindStates(owner)
        }

        viewModel.dispatch(LoginIntent.Initialize)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.login.setDebounceClickListener { viewModel.dispatch(LoginIntent.DoLogin) }
    }

    private fun bindEffects(owner: LifecycleOwner) {
        owner.lifecycleScope.launchWhenStarted {
            viewModel.effects
                .onEach { effect -> handleEffect(effect) }
                .collect()
        }
    }

    private fun bindStates(owner: LifecycleOwner) {
        owner.lifecycleScope.launchWhenStarted {
            viewModel.states
                .onEach { state -> render(state) }
                .collect()
        }
    }

    private fun handleEffect(effect: LoginEffect) {
        when (effect) {
            LoginEffect.ToMain -> {
            }
            LoginEffect.ToLoginForm -> {
                findNavController().navigateSafe(LoginFragmentDirections.toLoginForm())
            }
        }
    }

    private fun render(state: LoginState) {

    }
}
