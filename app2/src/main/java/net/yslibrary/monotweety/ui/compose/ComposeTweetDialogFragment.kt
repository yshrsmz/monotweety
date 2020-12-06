package net.yslibrary.monotweety.ui.compose

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.razir.progressbutton.DrawableButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.analytics.Analytics
import net.yslibrary.monotweety.databinding.FragmentComposeTweetBinding
import net.yslibrary.monotweety.ui.arch.ULIEState
import net.yslibrary.monotweety.ui.base.consumeEffects
import net.yslibrary.monotweety.ui.base.consumeStates
import net.yslibrary.monotweety.ui.base.hideKeyboard
import net.yslibrary.monotweety.ui.base.showKeyboard
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

    @Inject
    lateinit var analytics: Analytics

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

        analytics.screenView(Analytics.Screen.Compose)
        viewModel.dispatch(ComposeTweetIntent.Initialize(arguments?.getString(KEY_STATUS) ?: ""))
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        Timber.d("onCreateDialog")
        return MaterialAlertDialogBuilder(requireContext(), R.style.AppTheme_ComposeDialog)
            .setTitle(R.string.compose_tweet)
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
                    dismissDialog()
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
            if (viewModel.state.tweetLength > 0) {
                // has something in editor
                showDismissConfirmDialog()
            } else {
                dismissDialog()
            }
        }

        binding.tweet.afterTextChanges()
            .skipInitialValue()
            .debounce(200)
            .map { it.editable?.toString() ?: "" }
            .distinctUntilChanged()
            .map { ComposeTweetIntent.TweetUpdated(it) }
            .onEach { viewModel.dispatch(it) }
            .launchIn(lifecycleScope)

        binding.tweet.post {
            binding.tweet.showKeyboard()
        }
    }

    private fun handleEffect(effect: ComposeTweetEffect) {
        when (effect) {
            is ComposeTweetEffect.UpdateTweetString -> {
                binding.tweet.setText(effect.tweet)
            }
            ComposeTweetEffect.Dismiss -> {
                dismissDialog()
            }
            ComposeTweetEffect.Tweeted -> {
                Toast.makeText(requireContext(), R.string.tweet_succeeded, Toast.LENGTH_SHORT)
                    .show()
                dismissDialog()
            }
        }
    }

    private fun render(state: ComposeTweetState) {
        if (state.state == ULIEState.UNINITIALIZED || state.state == ULIEState.LOADING) return

        val context = alertDialog.context
        val counterColor = if (state.tweetState.isValid) {
            counterOriginalColor
        } else {
            ContextCompat.getColor(context, R.color.red)
        }
        binding.counter.setTextColor(counterColor)
        binding.counter.text =
            getString(R.string.status_counter, state.tweetLength, state.tweetMaxLength)

        binding.tweetContainer.error = if (state.tweetState.isInvalid && state.tweetLength > 0) {
            getString(R.string.status_too_long_error)
        } else null

        val positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        val negative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)

        positive.isEnabled = state.tweetState.isValid
        negative.isEnabled = state.tweetState.isAtMost(ComposeTweetState.TweetState.VALID)
        binding.tweetContainer.isEnabled =
            state.tweetState.isAtMost(ComposeTweetState.TweetState.VALID)
        if (state.tweetState.isAtLeast(ComposeTweetState.TweetState.TWEETING)) {
            positive.showProgress {
                gravity = DrawableButton.GRAVITY_CENTER
            }
        } else {
            positive.hideProgress(R.string.tweet)
        }
    }

    private fun showDismissConfirmDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.confirm)
            .setMessage(R.string.quit_without_tweeting)
            .setPositiveButton(R.string.quit) { _, _ -> dismissDialog() }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun cleanupProgress() {
        val positive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positive.hideProgress(R.string.tweet)
    }

    private fun dismissDialog() {
        binding.tweet.hideKeyboard()
        cleanupProgress()
        alertDialog.dismiss()
        activity?.finish()
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
