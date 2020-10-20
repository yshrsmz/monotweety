package net.yslibrary.monotweety.ui.loginform

import dagger.Subcomponent

@Subcomponent
interface LoginFormFragmentComponent {
    fun inject(fragment: LoginFormFragment)

    @Subcomponent.Factory
    interface Factory {
        fun build(): LoginFormFragmentComponent
    }

    interface ComponentProvider {
        fun loginFormFragmentComponent(): Factory
    }
}
