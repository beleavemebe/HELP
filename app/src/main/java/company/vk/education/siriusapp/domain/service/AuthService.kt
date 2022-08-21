package company.vk.education.siriusapp.domain.service

import company.vk.education.siriusapp.domain.model.AuthState
import kotlinx.coroutines.flow.StateFlow

interface AuthService {
    val authState: StateFlow<AuthState>
    fun auth()
    fun prepare()
}