package net.yslibrary.monotweety.ui.loginform

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.databinding.FragmentLoginformBinding
import net.yslibrary.monotweety.ui.base.viewBinding
import net.yslibrary.monotweety.ui.di.HasComponent
import net.yslibrary.monotweety.ui.di.getComponentProvider

class LoginFormFragment : BottomSheetDialogFragment(), HasComponent<LoginFormFragmentComponent> {

    override val component: LoginFormFragmentComponent by lazy {
        requireActivity().getComponentProvider<LoginFormFragmentComponent.ComponentProvider>()
            .loginFormFragmentComponent()
            .build()
    }

    val binding: FragmentLoginformBinding by viewBinding { FragmentLoginformBinding.bind(it) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_loginform, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}

