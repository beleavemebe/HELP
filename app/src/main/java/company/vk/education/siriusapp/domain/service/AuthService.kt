package company.vk.education.siriusapp.domain.service

interface AuthService {
    fun auth(login: String, password: String)
    fun authViaGoogle(token: String)
    fun authViaVk()
}