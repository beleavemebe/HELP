package company.vk.education.siriusapp.ui.screens.user

import company.vk.education.siriusapp.ui.base.BaseViewEffect

sealed class UserScreenViewEffect : BaseViewEffect {
    data class Navigate(val route: String) : UserScreenViewEffect()
}
