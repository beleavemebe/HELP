package company.vk.education.siriusapp.data

import company.vk.education.siriusapp.domain.model.AuthState
import company.vk.education.siriusapp.domain.service.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeAuthService : AuthService {
    override val authState: StateFlow<AuthState>
        get() = MutableStateFlow(AuthState())

    override fun authViaVk() {}
}