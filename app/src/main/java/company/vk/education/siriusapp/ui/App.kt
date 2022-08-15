package company.vk.education.siriusapp.ui

import android.app.Application
import com.yandex.mapkit.MapKitFactory

const val MAP_KIT_API_KEY = ""

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(MAP_KIT_API_KEY)
    }
}