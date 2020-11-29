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
import net.yslibrary.monotweety.databinding.FragmentComposeTweetBinding
import net.yslibrary.monotweety.ui.arch.ULIEState
import net.yslibrary.monotweety.ui.base.consumeEffects
import net.yslibrary.monotweety.ui.base.consumeStates
import net.yslibrary.monotweety.ui.di.HasComponent
import net.yslibrary.monotweety.ui.di.ViewModelFactory
import net.yslibrary.monotweety.ui.di.getComponentProvider
import reactivecircus.flowbinding.android.widget.afterTextChanges
import timber.log.Timber
import javax.inject.Inject

class ComposeTweetDialogFragment : DialogFragment(),
    HasComponent<ComposeTweetDialogFragmentComponent> {

    override val component by lazy {
        requireActivity().getComponentProvider<ComposeTweetDialogFragmentComponent.ComponentProvider>()
            .composeTweetDialogFragmentComponent()
            .build()
    }

    @Inject
    lateinit var factory: ViewModelFactory<ComposeTweetViewModel>

    private val viewModel: ComposeTweetViewModel by viewModels { factory }

    private val binding by lazy {
        FragmentComposeTweetBinding.bind(
            requireDialog().findViewById(R.id.composeTweetContainer))
    }

    private var counterOriginalColor: Int = Color.GRAY

    private val alertDialog: AlertDialog
        get() = dialog as AlertDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false

        component.inject(this)

        viewModel.dispatch(ComposeTweetIntent.Initialize(arguments?.getString(KEY_STATUS) ?: ""))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Timber.d("onCreateDialog")
        return MaterialAlertDialogBuilder(requireContext(), R.style.AppTheme_ComposeDialog)
            .setTitle("Title")
            .setView(R.layout.fragment_compose_tweet)
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
                if (viewModel.state.tweetLength > 0) {
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
            viewModel.dispatch(ComposeTweetIntent.Tweet)
        }
        negativeButton.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        binding.tweet.afterTextChanges()
            .skipInitialValue()
            .debounce(200)
            .map { it.editable?.toString() ?: "" }
            .distinctUntilChanged()
            .map { ComposeTweetIntent.TweetUpdated(it) }
            .onEach { viewModel.dispatch(it) }
            .launchIn(lifecycleScope)
    }

    private fun handleEffect(effect: ComposeTweetEffect) {
        when (effect) {
            is ComposeTweetEffect.UpdateTweetString -> {
                binding.tweet.setText(effect.tweet)
            }
        }
    }

    private fun render(state: ComposeTweetState) {
        if (state.state == ULIEState.UNINITIALIZED || state.state == ULIEState.LOADING) return

        val context = alertDialog.context
        val counterColor = if (state.isTweetValid) {
            counterOriginalColor
        } else {
            ContextCompat.getColor(context, R.color.red)
        }
        binding.counter.setTextColor(counterColor)
        binding.counter.text =
            getString(R.string.status_counter, state.tweetLength, state.tweetMaxLength)

        binding.tweetContainer.error = if (!state.isTweetValid && state.tweetLength > 0) {
            getString(R.string.status_too_long_error)
        } else null

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = state.isTweetValid
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
        const val TAG = "ComposeTweetDialogFragment"

        private const val KEY_STATUS = "status"
        fun newInstance(status: String? = null): ComposeTweetDialogFragment {
            return ComposeTweetDialogFragment().apply {
                if (status != null) {
                    arguments = bundleOf(KEY_STATUS to status)
                }
            }
        }
    }
}
