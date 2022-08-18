package company.vk.education.siriusapp.data.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult

class LoginActivity() : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("VK", "callback1")
        VK.login(this) { result ->
            Log.d("VK", "callback")
            when (result) {
                is VKAuthenticationResult.Failed -> {
                    Log.d("VK", "FAILED")
                }
                is VKAuthenticationResult.Success -> {
                    Log.d("VK", "SUCCESS")
                    Log.d("VK", "id: ${result.token.userId}")
                    VK.saveAccessToken(
                        this,
                        VK.getUserId(),
                        result.token.accessToken,
                        result.token.secret
                    )
                }
                null -> {
                    Log.d("VK", "NPE")
                }
            }
        }.launch(listOf())
        finish()
    }
}