package net.yslibrary.monotweety.ui.splash

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.databinding.FragmentSplashBinding
import net.yslibrary.monotweety.ui.base.ViewBindingFragment
import net.yslibrary.monotweety.ui.di.HasComponent
import net.yslibrary.monotweety.ui.di.ViewModelFactory
import net.yslibrary.monotweety.ui.di.getComponentProvider
import javax.inject.Inject

class SplashFragment : ViewBindingFragment<FragmentSplashBinding>(
    R.layout.fragment_splash,
    FragmentSplashBinding::bind
), HasComponent<SplashFragmentComponent> {

    override val component: SplashFragmentComponent by lazy {
        requireActivity().getComponentProvider<SplashFragmentComponent.ComponentProvider>()
            .splashFragmentComponent()
            .build()
    }

    @Inject
    lateinit var factory: ViewModelFactory<SplashViewModel>

    private val viewModel: SplashViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        component.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
