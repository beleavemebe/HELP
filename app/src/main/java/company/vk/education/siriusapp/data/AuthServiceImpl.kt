package company.vk.education.siriusapp.data

import android.content.Context
import company.vk.education.siriusapp.domain.model.AuthState
import company.vk.education.siriusapp.domain.service.AuthService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthService {
    override val authState: StateFlow<AuthState>
        get() = MutableStateFlow(AuthState())

    override fun authViaVk() {

    }
}