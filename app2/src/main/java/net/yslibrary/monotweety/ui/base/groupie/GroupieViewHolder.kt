package net.yslibrary.monotweety.ui.base.groupie

import androidx.viewbinding.ViewBinding

class GroupieViewHolder<T : ViewBinding>(
    @Suppress("MemberVisibilityCanBePrivate") val binding: T,
) : com.xwray.groupie.GroupieViewHolder(binding.root)
