package company.vk.education.siriusapp.domain.service

import androidx.activity.ComponentActivity
import company.vk.education.siriusapp.domain.model.AuthState
import kotlinx.coroutines.flow.Flow

interface AuthService {
    val authState: Flow<AuthState>
    fun authViaVk(context: ComponentActivity)
}