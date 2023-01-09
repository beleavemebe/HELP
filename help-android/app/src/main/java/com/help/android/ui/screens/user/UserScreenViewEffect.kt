package com.help.android.ui.screens.user

import com.help.android.ui.base.BaseViewEffect

sealed class UserScreenViewEffect : BaseViewEffect {
    data class Navigate(val route: String) : UserScreenViewEffect()
}
