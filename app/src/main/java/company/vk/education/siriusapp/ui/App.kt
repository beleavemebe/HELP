package company.vk.education.siriusapp.ui

import android.app.Application
import com.yandex.mapkit.MapKitFactory
import dagger.hilt.android.HiltAndroidApp

const val MAP_KIT_API_KEY = "389adb7c-8004-48c6-b26b-17a90bfd97e4"

@HiltAndroidApp
class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(MAP_KIT_API_KEY)
    }
}