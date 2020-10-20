package net.yslibrary.monotweety.ui.login

import dagger.Subcomponent
import net.yslibrary.monotweety.di.FragmentScope

@FragmentScope
@Subcomponent
interface LoginFragmentComponent {
    fun inject(fragment: LoginFragment)

    @Subcomponent.Factory
    interface Factory {
        fun build(): LoginFragmentComponent
    }
}

