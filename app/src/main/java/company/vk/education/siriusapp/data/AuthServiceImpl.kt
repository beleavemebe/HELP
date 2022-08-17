package company.vk.education.siriusapp.data

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import company.vk.education.siriusapp.domain.model.AuthState
import company.vk.education.siriusapp.domain.service.AuthService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class AuthServiceImpl @Inject constructor() : AuthService {
    override val authState: StateFlow<AuthState>
        get() = MutableStateFlow(AuthState())

    override fun authViaVk(activity: ComponentActivity) {
        val mainPreferences = activity.getSharedPreferences("main", Context.MODE_PRIVATE)
        Log.d("VK", "HI")
        if (mainPreferences.getString("VK_TOKEN", "").isNullOrBlank()) {
            Log.d("VK", "TRYING TO LOGIN")
            VK.login(activity) { result ->
                when (result) {
                    is VKAuthenticationResult.Failed -> {
                        Log.d("VK", "FAILED")
                    }
                    is VKAuthenticationResult.Success -> {
                        Log.d("VK", "SUCCESS")
                        Log.d("VK", "id: ${result.token.userId}")
                        mainPreferences.edit().apply {
                            putString("VK_ID", result.token.userId.toString())
                            putString("VK_TOKEN", result.token.accessToken)
                        }.apply()
                    }
                    null -> {
                        Log.d("VK", "NPE")
                    }
                }
            }
        }
        Log.d("VK", mainPreferences.all.toString())
    }
}