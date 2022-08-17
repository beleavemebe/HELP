package company.vk.education.siriusapp.ui

import android.app.Application
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiConfig
import com.yandex.mapkit.MapKitFactory
import company.vk.education.siriusapp.BuildConfig
import dagger.hilt.android.HiltAndroidApp

const val GEOCODER_URL = "https://geocode-maps.yandex.ru/"

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.MAP_KIT_API_KEY)
        VK.setConfig(VKApiConfig(this, appId = BuildConfig.VK_APP_ID.toInt()))
    }
}