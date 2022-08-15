package company.vk.education.siriusapp.domain.repository

import company.vk.education.siriusapp.domain.model.AuthState
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    val authState: Flow<AuthState>
}