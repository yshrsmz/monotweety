package net.yslibrary.monotweety.ui.footereditor

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.databinding.FragmentFooterEditorBinding
import net.yslibrary.monotweety.ui.base.ViewBindingBottomSheetDialogFragment
import net.yslibrary.monotweety.ui.base.consumeEffects
import net.yslibrary.monotweety.ui.base.consumeStates
import net.yslibrary.monotweety.ui.base.setDebounceClickListener
import net.yslibrary.monotweety.ui.di.HasComponent
import net.yslibrary.monotweety.ui.di.ViewModelFactory
import net.yslibrary.monotweety.ui.di.getComponentProvider
import javax.inject.Inject

class FooterEditorFragment :
    ViewBindingBottomSheetDialogFragment<FragmentFooterEditorBinding>(
        R.layout.fragment_footer_editor,
        FragmentFooterEditorBinding::bind
    ), HasComponent<FooterEditorFragmentComponent> {

    override val component: FooterEditorFragmentComponent by lazy {
        requireActivity().getComponentProvider<FooterEditorFragmentComponent.ComponentProvider>()
            .footerEditorFragmentComponent()
            .build()
    }

    @Inject
    lateinit var factory: ViewModelFactory<FooterEditorViewModel>

    private val viewModel: FooterEditorViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewLifecycleOwnerLiveData.observe(this) { owner ->
            viewModel.consumeEffects(owner.lifecycleScope, this::handleEffect)
            viewModel.consumeStates(owner.lifecycleScope, this::render)
        }
        viewModel.dispatch(FooterEditorIntent.Initialize)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindEvents()
    }

    private fun bindEvents() {
        binding.cancel.setDebounceClickListener { }
        binding.save.setDebounceClickListener { }
    }

    private fun handleEffect(effect: FooterEditorEffect) {

    }

    private fun render(state: FooterEditorState) {

    }
}
