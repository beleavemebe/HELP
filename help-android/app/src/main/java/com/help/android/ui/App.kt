package com.help.android.ui

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.yandex.mapkit.MapKitFactory
import com.help.android.BuildConfig
import com.help.android.core.CurrentActivityProviderImpl
import com.help.android.domain.service.AuthService
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber.DebugTree
import timber.log.Timber.Forest.plant
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun getWorkManagerConfiguration(): Configuration {
        return Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
    }

    @Inject
    lateinit var authService: AuthService
    @Inject
    lateinit var activityProvider: CurrentActivityProviderImpl

    override fun onCreate() {
        super.onCreate()
        MapKitFactory.setApiKey(BuildConfig.MAP_KIT_API_KEY)
        if (BuildConfig.DEBUG) {
            plant(DebugTree())
        }
        initActivityProvider()
    }

    private fun initActivityProvider() {
        registerActivityLifecycleCallbacks(
            object : ActivityLifecycleCallbacks {
                override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                    if (p0 is ComponentActivity) {
                        activityProvider.activityCreated(p0)
                        authService.prepare()
                    }
                }

                override fun onActivityDestroyed(p0: Activity) {
                    if (p0 is ComponentActivity) {
                        activityProvider.activityDestroyed(p0)
                    }
                }

                override fun onActivityStarted(p0: Activity) {}
                override fun onActivityResumed(p0: Activity) {}
                override fun onActivityPaused(p0: Activity) {}
                override fun onActivityStopped(p0: Activity) {}
                override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {}
            }
        )
    }
}