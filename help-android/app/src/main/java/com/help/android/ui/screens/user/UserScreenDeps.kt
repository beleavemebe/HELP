package com.help.android.ui.screens.user

import com.help.android.ui.utils.unprovidedCompositionLocalOf

val LocalUserScreenDeps = unprovidedCompositionLocalOf<UserScreenDeps>()

class UserScreenDeps(
    val userScreenMappers: UserScreenMappers
)