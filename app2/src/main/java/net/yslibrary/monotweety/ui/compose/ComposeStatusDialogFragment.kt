package net.yslibrary.monotweety.ui.compose

import android.app.Dialog
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.databinding.FragmentComposeStatusBinding
import net.yslibrary.monotweety.ui.di.HasComponent
import net.yslibrary.monotweety.ui.di.ViewModelFactory
import net.yslibrary.monotweety.ui.di.getComponentProvider
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

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false

        component.inject(this)

        viewLifecycleOwnerLiveData.observe(this) { owner ->

        }

        requireActivity().onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
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
        dialog.setOnShowListener {
            val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)

            positiveButton.setOnClickListener {

            }
            negativeButton.setOnClickListener {

            }
        }
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
