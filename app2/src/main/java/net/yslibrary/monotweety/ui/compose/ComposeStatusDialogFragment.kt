package net.yslibrary.monotweety.ui.compose

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.databinding.FragmentComposeStatusBinding
import net.yslibrary.monotweety.ui.arch.ULIEState
import net.yslibrary.monotweety.ui.base.consumeEffects
import net.yslibrary.monotweety.ui.base.consumeStates
import net.yslibrary.monotweety.ui.di.HasComponent
import net.yslibrary.monotweety.ui.di.ViewModelFactory
import net.yslibrary.monotweety.ui.di.getComponentProvider
import reactivecircus.flowbinding.android.widget.afterTextChanges
import timber.log.Timber
import javax.inject.Inject

class ComposeStatusDialogFragment : DialogFragment(),
    HasComponent<ComposeStatusDialogFragmentComponent> {

    override val component by lazy {
        requireActivity().getComponentProvider<ComposeStatusDialogFragmentComponent.ComponentProvider>()
            .composeStatusDialogFragmentComponent()
            .build()
    }

    @Inject
    lateinit var factory: ViewModelFactory<ComposeStatusViewModel>

    private val viewModel: ComposeStatusViewModel by viewModels { factory }

    private val binding by lazy {
        FragmentComposeStatusBinding.bind(
            requireDialog().findViewById(R.id.composeStatusContainer))
    }

    private var counterOriginalColor: Int = Color.GRAY

    private val alertDialog: AlertDialog
        get() = dialog as AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false

        component.inject(this)

        viewModel.dispatch(ComposeStatusIntent.Initialize(arguments?.getString(KEY_STATUS) ?: ""))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Timber.d("onCreateDialog")
        return MaterialAlertDialogBuilder(requireContext(), R.style.AppTheme_ComposeDialog)
            .setTitle("Title")
            .setView(R.layout.fragment_compose_status)
            .setNegativeButton(R.string.cancel, null)
            .setPositiveButton(R.string.tweet, null)
            .create()
            .also { setupDialog(it) }
    }

    private fun setupDialog(dialog: AlertDialog) {
        dialog.setCanceledOnTouchOutside(false)
        dialog.setOnShowListener {
            counterOriginalColor = binding.counter.currentTextColor
            setupEvents(dialog)
            viewModel.consumeEffects(lifecycleScope, this::handleEffect)
            viewModel.consumeStates(lifecycleScope, this::render)
        }
        dialog.setOnKeyListener { d, keyCode, event ->
            if (event.action == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                if (viewModel.state.statusLength > 0) {
                    // has something in editor
                    showDismissConfirmDialog()
                } else {
                    d.dismiss()
                }
                true
            } else false
        }
    }

    @OptIn(FlowPreview::class)
    private fun setupEvents(dialog: AlertDialog) {
        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        positiveButton.setOnClickListener {
            viewModel.dispatch(ComposeStatusIntent.Tweet)
        }
        negativeButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.status.afterTextChanges()
            .skipInitialValue()
            .debounce(200)
            .map { it.editable?.toString() ?: "" }
            .distinctUntilChanged()
            .map { ComposeStatusIntent.StatusUpdated(it) }
            .onEach { viewModel.dispatch(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleEffect(effect: ComposeStatusEffect) {
        when (effect) {
            is ComposeStatusEffect.UpdateStatus -> {
                binding.status.setText(effect.status)
            }
        }
    }

    private fun render(state: ComposeStatusState) {
        if (state.state == ULIEState.UNINITIALIZED || state.state == ULIEState.LOADING) return

        val context = alertDialog.context
        val counterColor = if (state.isStatusValid) {
            counterOriginalColor
        } else {
            ContextCompat.getColor(context, R.color.red)
        }
        binding.counter.setTextColor(counterColor)
        binding.counter.text =
            getString(R.string.status_counter, state.statusLength, state.statusMaxLength)

        binding.statusContainer.error = if (!state.isStatusValid && state.statusLength > 0) {
            getString(R.string.status_too_long_error)
        } else null

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = state.isStatusValid
    }

    private fun showDismissConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.confirm)
            .setMessage(R.string.quit_without_tweeting)
            .setPositiveButton(R.string.quit) { dialog, which ->
                alertDialog.dismiss()
                activity?.finish()
            }
            .setNegativeButton(R.string.no, null)
            .show()
    }

    companion object {
        const val TAG = "ComposeStatusDialogFragment"

        private const val KEY_STATUS = "status"
        fun newInstance(status: String? = null): ComposeStatusDialogFragment {
            return ComposeStatusDialogFragment().apply {
                if (status != null) {
                    arguments = bundleOf(KEY_STATUS to status)
                }
            }
        }
    }
}
