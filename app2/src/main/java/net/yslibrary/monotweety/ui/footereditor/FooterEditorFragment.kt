package net.yslibrary.monotweety.ui.footereditor

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.databinding.FragmentFooterEditorBinding
import net.yslibrary.monotweety.ui.arch.ULIEState
import net.yslibrary.monotweety.ui.base.ViewBindingBottomSheetDialogFragment
import net.yslibrary.monotweety.ui.base.consumeEffects
import net.yslibrary.monotweety.ui.base.consumeStates
import net.yslibrary.monotweety.ui.di.HasComponent
import net.yslibrary.monotweety.ui.di.ViewModelFactory
import net.yslibrary.monotweety.ui.di.getComponentProvider
import reactivecircus.flowbinding.android.view.clicks
import reactivecircus.flowbinding.android.widget.afterTextChanges
import reactivecircus.flowbinding.android.widget.checkedChanges
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
        component.inject(this)

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

    @OptIn(FlowPreview::class)
    private fun bindEvents() {
        val scope = viewLifecycleOwner.lifecycleScope
        binding.enableFooterSwitch.checkedChanges()
            .skipInitialValue()
            .map { isChecked -> FooterEditorIntent.EnableStateUpdated(isChecked) }
            .onEach { intent -> viewModel.dispatch(intent) }
            .launchIn(scope)

        binding.footerEditText.afterTextChanges()
            .skipInitialValue()
            .debounce(200)
            .map { it.editable?.toString() ?: "" }
            .distinctUntilChanged()
            .map { FooterEditorIntent.FooterTextUpdated(it) }
            .onEach { intent -> viewModel.dispatch(intent) }
            .launchIn(scope)

        binding.cancel.clicks()
            .debounce(800)
            .onEach { viewModel.dispatch(FooterEditorIntent.CancelRequested) }
            .launchIn(scope)

        binding.save.clicks()
            .debounce(800)
            .onEach { viewModel.dispatch(FooterEditorIntent.SaveRequested) }
            .launchIn(scope)
    }

    private fun handleEffect(effect: FooterEditorEffect) {
        when (effect) {
            FooterEditorEffect.Close -> findNavController().popBackStack()
        }
    }

    private fun render(state: FooterEditorState) {
        if (state.state == ULIEState.UNINITIALIZED) return

        binding.enableFooterSwitch.isChecked = state.footerEnabled
        if (!binding.footerEditText.hasFocus()) {
            binding.footerEditText.setText(state.footerText)
        }

        binding.footerInput.error = if (!state.isValidFooterText) {
            getString(R.string.footer_error)
        } else null

        binding.save.isEnabled = state.isValidFooterText
    }
}
