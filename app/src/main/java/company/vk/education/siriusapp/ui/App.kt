package company.vk.education.siriusapp.ui

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.vk.api.sdk.VK
import com.vk.api.sdk.VKApiConfig
import com.yandex.mapkit.MapKitFactory
import company.vk.education.siriusapp.BuildConfig
import company.vk.education.siriusapp.core.CurrentActivityProviderImpl
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

const val GEOCODER_URL = "https://geocode-maps.yandex.ru/"

@HiltAndroidApp
class App : Application() {
    @Inject
    lateinit var activityProvider: CurrentActivityProviderImpl

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.MAP_KIT_API_KEY)
        VK.setConfig(VKApiConfig(this, appId = BuildConfig.VK_APP_ID.toInt()))
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                if (p0 is ComponentActivity)
                    activityProvider.activityResumed(p0)
            }

            override fun onActivityStarted(p0: Activity) {
            }

            override fun onActivityResumed(p0: Activity) {
            }

            override fun onActivityPaused(p0: Activity) {
                if (p0 is ComponentActivity)
                    activityProvider.activityPaused(p0)
            }

            override fun onActivityStopped(p0: Activity) {
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }

            override fun onActivityDestroyed(p0: Activity) {
            }

        })
    }
}