package net.yslibrary.monotweety.login

import com.bluelinelabs.conductor.changehandler.FadeChangeHandler
import com.bluelinelabs.conductor.changehandler.TransitionChangeHandlerCompat

/**
 * Created by yshrsmz on 2017/01/04.
 */
class LoginTransitionChangeHandlerCompat : TransitionChangeHandlerCompat(LoginTransitionChangeHandler(), FadeChangeHandler())