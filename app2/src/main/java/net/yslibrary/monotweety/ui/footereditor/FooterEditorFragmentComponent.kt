package net.yslibrary.monotweety.ui.footereditor

import dagger.Subcomponent

@Subcomponent
interface FooterEditorFragmentComponent {
    fun inject(fragment: FooterEditorFragment)

    @Subcomponent.Factory
    interface Factory {
        fun build(): FooterEditorFragmentComponent
    }

    interface ComponentProvider {
        fun footerEditorFragmentComponent(): FooterEditorFragmentComponent.Factory
    }
}
