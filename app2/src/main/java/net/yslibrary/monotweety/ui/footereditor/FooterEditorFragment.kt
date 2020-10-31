package net.yslibrary.monotweety.ui.footereditor

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import net.yslibrary.monotweety.R
import net.yslibrary.monotweety.databinding.FragmentFooterEditorBinding
import net.yslibrary.monotweety.ui.base.ViewBindingBottomSheetDialogFragment

class FooterEditorFragment :
    ViewBindingBottomSheetDialogFragment<FragmentFooterEditorBinding>(
        R.layout.fragment_footer_editor,
        FragmentFooterEditorBinding::bind
    ) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewLifecycleOwnerLiveData.observe(this) { owner ->
            bindEffects(owner.lifecycleScope)
            bindStates(owner.lifecycleScope)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindEvents()
    }

    private fun bindEvents() {

    }

    private fun bindEffects(scope: LifecycleCoroutineScope) {

    }

    private fun bindStates(scope: LifecycleCoroutineScope) {

    }
}
