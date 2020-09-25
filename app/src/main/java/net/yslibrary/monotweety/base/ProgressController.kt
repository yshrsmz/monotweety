package net.yslibrary.monotweety.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import net.yslibrary.monotweety.R

/**
 * Controller to show ProgressBar
 * Created by yshrsmz on 2016/10/05.
 */
class ProgressController : BaseController() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup,
        savedViewState: Bundle?
    ): View {
        return inflater.inflate(R.layout.controller_progress, container, false)
    }
}
