package company.vk.education.siriusapp.ui.screens.user

import company.vk.education.siriusapp.ui.utils.unprovidedCompositionLocalOf

val LocalUserScreenDeps = unprovidedCompositionLocalOf<UserScreenDeps>()

class UserScreenDeps(
    val userScreenMappers: UserScreenMappers
)