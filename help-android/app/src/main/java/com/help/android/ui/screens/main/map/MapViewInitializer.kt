package com.help.android.ui.screens.main.map

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.yandex.mapkit.mapview.MapView

class MapViewInitializer(
    private val initialize: MapView.() -> Unit,
    private val mapView: MapView,
) : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        mapView.initialize()
    }

    override fun onStart(owner: LifecycleOwner) {
        mapView.onStart()
    }

    override fun onStop(owner: LifecycleOwner) {
        mapView.onStop()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }
}