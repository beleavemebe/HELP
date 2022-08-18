package company.vk.education.siriusapp.data

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import company.vk.education.siriusapp.core.CurrentActivityProvider
import company.vk.education.siriusapp.data.activity.LoginActivity
import company.vk.education.siriusapp.domain.model.AuthState
import company.vk.education.siriusapp.domain.service.AuthService
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ActivityRetainedScoped
class AuthServiceImpl @Inject constructor(
    private val currentActivityProvider: CurrentActivityProvider
) : AuthService {
    override val authState: StateFlow<AuthState>
        get() = MutableStateFlow(AuthState())

    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "main")


    override fun auth() {
        val componentActivity = currentActivityProvider.currentActivity
        componentActivity!!.dataStore.data
            .map { it[stringPreferencesKey("VK_TOKEN")] }
            .onEach {
            if (it == null) {
                componentActivity.startActivity(
                    Intent(componentActivity, LoginActivity::class.java)
                )
            }
        }.flowOn(Dispatchers.Main)
            .launchIn(GlobalScope)

        return
        val currentActivity = componentActivity ?: error("")
        val mainPreferences = currentActivity.dataStore
        Log.d("VK", "HI")

//        runBlocking { //TODO get scope from viewmodel
//            val token = mainPreferences.data.first()[stringPreferencesKey("VK_TOKEN")] ?: ""
//            Log.d("VK", "TOKEN: $token")

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

//    }
}