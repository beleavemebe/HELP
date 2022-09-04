package company.vk.education.siriusapp.ui.library.yandexmaps

import android.content.Context
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.directions.DirectionsFactory
import com.yandex.mapkit.mapview.MapView

class MapKitInitializer(
    private val initialize: MapView.() -> Unit,
    private val mapView: MapView,
    private val context: Context
) : DefaultLifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {
        MapKitFactory.initialize(context)
        MapKitFactory.getInstance().resetLocationManagerToDefault()
        DirectionsFactory.initialize(context)
        mapView.initialize()
    }

    override fun onStart(owner: LifecycleOwner) {
        mapView.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop(owner: LifecycleOwner) {
        mapView.onStop()
        MapKitFactory.getInstance().onStop()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)
    }
}