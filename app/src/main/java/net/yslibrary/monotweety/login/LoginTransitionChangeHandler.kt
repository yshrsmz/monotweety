package net.yslibrary.monotweety.login

import android.transition.*
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import com.bluelinelabs.conductor.changehandler.TransitionChangeHandler
import net.yslibrary.monotweety.R


/**
 * Created by yshrsmz on 2016/10/29.
 */
class LoginTransitionChangeHandler : TransitionChangeHandler() {
  override fun getTransition(container: ViewGroup, from: View?, to: View?, isPush: Boolean): Transition {
    val context = container.context

    val move = TransitionInflater.from(context)
        .inflateTransition(android.R.transition.move)
    move.addTarget(context.getString(R.string.transition_name_logo))

    val fade = Fade(Fade.MODE_IN)
    fade.addTarget(R.id.login)
    fade.addTarget(R.id.app_icon)

    val transitionSet = TransitionSet()
        .setOrdering(TransitionSet.ORDERING_SEQUENTIAL)
        .addTransition(move)
        .addTransition(fade)

    transitionSet.pathMotion = ArcMotion()
    transitionSet.interpolator = DecelerateInterpolator()

    return transitionSet
  }
}