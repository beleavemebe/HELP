package company.vk.education.siriusapp.domain.model

sealed class AuthState {
    object Unknown : AuthState()
    object NotAuthenticated : AuthState()
    data class Authenticated(val user: User) : AuthState()
}
