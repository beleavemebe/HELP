package company.vk.education.siriusapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.vk.api.sdk.VK
import com.vk.api.sdk.auth.VKAuthenticationResult

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("VK", "HI")
        VK.login(this) { result ->
            when (result) {
                is VKAuthenticationResult.Failed -> {
                    Log.d("VK", "FAILED")
                }
                is VKAuthenticationResult.Success -> {
                    Log.d("VK", "SUCCESS")
                    Log.d("VK", "id: ${result.token.userId}")
                }
                null -> {
                    Log.d("VK", "NPE")
                }
            }
        }
    }
}