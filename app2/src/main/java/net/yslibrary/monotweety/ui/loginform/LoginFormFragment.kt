package net.yslibrary.monotweety.ui.loginform

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.github.razir.progressbutton.DrawableButton
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.databinding.FragmentLoginformBinding
import net.yslibrary.monotweety.ui.base.ViewBindingBottomSheetDialogFragment
import net.yslibrary.monotweety.ui.base.navigateSafe
import net.yslibrary.monotweety.ui.base.navigateToBrowser
import net.yslibrary.monotweety.ui.base.setDebounceClickListener
import net.yslibrary.monotweety.ui.di.HasComponent
import net.yslibrary.monotweety.ui.di.ViewModelFactory
import net.yslibrary.monotweety.ui.di.getComponentProvider
import timber.log.Timber
import javax.inject.Inject

class LoginFormFragment : ViewBindingBottomSheetDialogFragment<FragmentLoginformBinding>(
    R.layout.fragment_loginform,
    FragmentLoginformBinding::bind,
), HasComponent<LoginFormFragmentComponent> {

    override val component: LoginFormFragmentComponent by lazy {
        requireActivity()
            .getComponentProvider<LoginFormFragmentComponent.ComponentProvider>()
            .loginFormFragmentComponent()
            .build()
    }

    @Inject
    lateinit var factory: ViewModelFactory<LoginFormViewModel>

    private val viewModel: LoginFormViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)

        viewLifecycleOwnerLiveData.observe(this) { owner ->
            bindEffects(owner.lifecycleScope)
            bindStates(owner.lifecycleScope)
        }
        viewModel.dispatch(LoginFormIntent.Initialize)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindEvents()
    }

    private fun bindEvents() {
        bindProgressButton(binding.openBrowser)
        bindProgressButton(binding.authorize)

        binding.openBrowser.setDebounceClickListener { viewModel.dispatch(LoginFormIntent.OpenBrowser) }
        binding.authorize.setDebounceClickListener { viewModel.dispatch(LoginFormIntent.Authorize) }
        binding.pinCode.editText?.addTextChangedListener { editable ->
            val text = editable?.toString() ?: ""
            viewModel.dispatch(LoginFormIntent.PinCodeUpdated(text))
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

    private fun handleEffect(effect: LoginFormEffect) {
        Timber.d("newEffect: $effect")
        when (effect) {
            is LoginFormEffect.OpenBrowser -> {
                context?.navigateToBrowser(effect.url)
            }
            LoginFormEffect.ToMain -> {
                findNavController().navigateSafe(LoginFormFragmentDirections.toMain())
                activity?.finish()
            }
        }
    }

    private fun render(state: LoginFormState) {
        Timber.d("newState: $state")
        val flowState = state.loginFlowState

        binding.pinCode.isEnabled = flowState.isAtLeast(LoginFlowState.WaitForPinCode) &&
                flowState.isAtMost(LoginFlowState.LoadAccessTokenError)

        binding.authorize.isEnabled = state.requestToken != null && state.pinCodeIsValid &&
                flowState.isAtLeast(LoginFlowState.WaitForPinCode) &&
                flowState.isAtMost(LoginFlowState.LoadAccessTokenError) &&
                flowState != LoginFlowState.Authorizing

        if (flowState == LoginFlowState.LoadingRequestToken) {
            binding.openBrowser.showProgress {
                gravity = DrawableButton.GRAVITY_CENTER
            }
        } else {
            binding.openBrowser.hideProgress(R.string.open_browser)
        }

        if (flowState == LoginFlowState.Authorizing) {
            binding.authorize.showProgress {
                gravity = DrawableButton.GRAVITY_CENTER
            }
        } else {
            binding.authorize.hideProgress(R.string.login_authorize)
        }
    }
}

