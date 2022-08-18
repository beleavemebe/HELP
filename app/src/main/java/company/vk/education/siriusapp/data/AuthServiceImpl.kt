package company.vk.education.siriusapp.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult
import company.vk.education.siriusapp.core.CurrentActivityProvider
import company.vk.education.siriusapp.domain.model.AuthState
import company.vk.education.siriusapp.domain.service.AuthService
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@ActivityRetainedScoped
class AuthServiceImpl @Inject constructor(
    private val currentActivityProvider: CurrentActivityProvider
) : AuthService {
    override val authState: StateFlow<AuthState>
        get() = MutableStateFlow(AuthState())

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "main")
    override fun auth() {
        val currentActivity = currentActivityProvider.currentActivity ?: error("")
        val mainPreferences = currentActivity.dataStore
        Log.d("VK", "HI")

        runBlocking { //TODO get scope from viewmodel
            val token = mainPreferences.data.first()[stringPreferencesKey("VK_TOKEN")] ?: ""
            Log.d("VK", "TOKEN: $token")
            VK.login(currentActivity) { result ->
                when (result) {
                    is VKAuthenticationResult.Failed -> {
                        Log.d("VK", "FAILED")
                    }
                    is VKAuthenticationResult.Success -> {
                        Log.d("VK", "SUCCESS")
                        Log.d("VK", "id: ${result.token.userId}")
//                            mainPreferences.edit().apply {
//                                putString("VK_ID", result.token.userId.toString())
//                                putString("VK_TOKEN", result.token.accessToken)
//                            }.apply()
                        VK.saveAccessToken(
                            currentActivity,
                            VK.getUserId(),
                            result.token.accessToken,
                            result.token.secret
                        )
                    }
                    null -> {
                        Log.d("VK", "NPE")
                    }
                }
            }
            Log.d("VK", "Logged in")
//            if (token.isEmpty()) {
//                val data = VK.getVKAuthActivityResultContract()
//                    .getSynchronousResult(currentActivity, listOf())?.value as? VKAuthenticationResult.Success
//                data?.let {
//                    Log.d("VK", data.token.userId.toString())
//                }
//
//            }
        }
//            Log.d("VK", mainPreferences.toString())

    }
}