package com.help.android.domain.service

import com.help.android.domain.model.AuthState
import kotlinx.coroutines.flow.StateFlow

interface AuthService {
    val authState: StateFlow<AuthState>
    fun auth()
    fun prepare()
}