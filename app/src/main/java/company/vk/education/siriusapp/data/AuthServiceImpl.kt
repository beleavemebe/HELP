package company.vk.education.siriusapp.data

import android.util.Log
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AuthServiceImpl @Inject constructor(
    private val currentActivityProvider: CurrentActivityProvider
) : AuthService {
    private val _authState = MutableStateFlow(AuthState())

    override val authState: StateFlow<AuthState>
        get() = _authState


    private val vkContract = ActivityResultCallback<VKAuthenticationResult> {
        when (it) {
            is VKAuthenticationResult.Success -> {
                Log.d("VK", "Success login")
            }
            is VKAuthenticationResult.Failed -> {
                Log.d("VK", "Login failed")
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
                    Log.d("VK", "Failed to get user info")
                }

                override fun success(result: AccountUserSettings) {
                    Log.d("VK", result.toString())
                    with(result) {
                        val user = User(
                            id.toString(),
                            "$firstName $lastName",
                            null,
                            UserContacts(phone ?: "", "vk.com/id$id", null)
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
                    Log.d("VK", "Get photo failed")
                    authCompleted(user)
                }

                override fun success(result: PhotosGetResponse) {
                    user.imageUrl = result.items.firstOrNull()?.sizes?.maxBy { it.height }?.url
                    authCompleted(user)
                }
            })
    }

    private fun authCompleted(user: User) {
        Log.d("VK", user.toString())
        _authState.value = AuthState(false, user)
    }

    override fun init() {
        Log.d("VK", "AuthService initialization")
        vkLogin = VK.login(
            currentActivityProvider.currentActivity
                ?: throw IllegalStateException("Activity is not initialized"), vkContract
        )
    }
}