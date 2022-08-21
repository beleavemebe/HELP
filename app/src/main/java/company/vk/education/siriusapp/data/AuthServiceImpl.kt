package company.vk.education.siriusapp.data

import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiCallback
import com.vk.api.sdk.auth.VKAuthenticationResult
import com.vk.api.sdk.auth.VKScope
import com.vk.dto.common.id.UserId
import com.vk.sdk.api.account.AccountService
import com.vk.sdk.api.account.dto.AccountUserSettings
import com.vk.sdk.api.photos.PhotosService
import com.vk.sdk.api.photos.dto.PhotosGetResponse
import company.vk.education.siriusapp.core.CurrentActivityProvider
import company.vk.education.siriusapp.domain.model.AuthState
import company.vk.education.siriusapp.domain.model.User
import company.vk.education.siriusapp.domain.model.UserContacts
import company.vk.education.siriusapp.domain.service.AuthService
import company.vk.education.siriusapp.ui.utils.log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    private val currentActivityProvider: CurrentActivityProvider
) : AuthService {
    private val _authState = MutableStateFlow(AuthState())
    override val authState = _authState.asStateFlow()


    private val vkContract = ActivityResultCallback<VKAuthenticationResult> {
        when (it) {
            is VKAuthenticationResult.Success -> {
                log("Success login")
            }
            is VKAuthenticationResult.Failed -> {
                log( "Login failed")
            }
        }
    }

    private lateinit var vkLogin: ActivityResultLauncher<Collection<VKScope>>

    override fun auth() {
        if (!::vkLogin.isInitialized) {
            throw IllegalStateException("You should init service before calling 'auth' method")
        }
        if (!VK.isLoggedIn()) {
            vkLogin.launch(
                listOf(
                    VKScope.OFFLINE,
                    VKScope.STATS,
                    VKScope.PHOTOS,
                    VKScope.PHONE
                )
            )
        }
        VK.execute(
            AccountService().accountGetProfileInfo(),
            object : VKApiCallback<AccountUserSettings> {
                override fun fail(error: Exception) {
                    log("Failed to get user info")
                }

                override fun success(result: AccountUserSettings) {
                    log(result.toString())
                    with(result) {
                        val user = User(
                            id.toString(),
                            "$firstName $lastName",
                            null,
                            UserContacts(phone ?: "", buildProfileURL(id), null)
                        )
                        getPhoto(user)
                    }
                }
            }
        )
    }

    private fun getPhoto(user: User) {
        VK.execute(
            PhotosService().photosGet(UserId(user.id.toLong()), "profile", rev = true),
            object : VKApiCallback<PhotosGetResponse> {
                override fun fail(error: Exception) {
                    log("Get photo failed")
                    authCompleted(user)
                }

                override fun success(result: PhotosGetResponse) {
                    authCompleted(
                        user.copy(
                            imageUrl = result.items.firstOrNull()?.sizes?.maxBy { it.height }?.url
                        )
                    )
                }
            })
    }

    private fun authCompleted(user: User) {
        log(user.toString())
        _authState.value = AuthState(false, user)
    }

    private fun buildProfileURL(userId: UserId) = "https://vk.com/id$userId"

    override fun prepare() {
        log("AuthService initialization")
        vkLogin = VK.login(
            currentActivityProvider.currentActivity
                ?: throw IllegalStateException("Activity is not initialized"), vkContract
        )
    }
}