package company.vk.education.siriusapp.domain.service

import company.vk.education.siriusapp.domain.model.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthService {
    val authState: Flow<AuthState>
    fun auth()
    fun init()
}