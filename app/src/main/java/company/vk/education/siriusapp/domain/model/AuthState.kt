package company.vk.education.siriusapp.domain.model

data class AuthState(
    val isUnknown: Boolean = true,
    val user: User? = null
)

val unknownAuthState = AuthState()
val fakeAuthState = AuthState(
    isUnknown = false,
    user = fakeUser
)