package company.vk.education.siriusapp.ui

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import company.vk.education.siriusapp.BuildConfig
import dagger.hilt.android.HiltAndroidApp

const val GEOCODER_URL = "https://geocode-maps.yandex.ru/"

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.MAP_KIT_API_KEY)
    }
}