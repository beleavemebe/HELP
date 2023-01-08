package company.vk.education.siriusapp.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.activity.result.registerForActivityResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.lifecycle.lifecycleScope
import company.vk.education.siriusapp.core.CurrentActivityProvider
import company.vk.education.siriusapp.data.appdata.AppData
import company.vk.education.siriusapp.data.appdata.appDataStore
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class LocationPermissionStatus {
    UNKNOWN,
    NOT_REQUESTED,
    IS_BEING_REQUESTED,
    NEEDS_EXPLANATION,
    PERMANENTLY_DENIED,
    FINE_NOT_GRANTED,
    GRANTED
}

@ActivityScoped
class LocationPermissionHandler @Inject constructor(
    private val currentActivityProvider: CurrentActivityProvider,
) {
    private val _status = MutableStateFlow(LocationPermissionStatus.UNKNOWN)
    val status = _status.asStateFlow()

    private val activity: ComponentActivity
        get() = currentActivityProvider.currentActivity
            ?: error("Could not get an activity")

    private val appDataStore: DataStore<AppData>
        get() = activity.appDataStore

    private lateinit var requestLocationPermissionsLauncher: ActivityResultLauncher<Unit>

    fun init() {
        activity.lifecycleScope.launch {
            val appData = appDataStore.data.first()
            updateStatus(appData.wasLocationPermissionRequested)
        }

        requestLocationPermissionsLauncher = activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) { permissionsGranted ->
            updateStatus(permissionsGranted)
        }
    }

    private fun updateStatus(wasLocationPermissionRequested: Boolean) {
        _status.value = if (wasLocationPermissionRequested.not()) {
            LocationPermissionStatus.NOT_REQUESTED
        } else if (isLocationPermissionGranted()) {
            LocationPermissionStatus.GRANTED
        } else if (shouldShowRationale()) {
            LocationPermissionStatus.NEEDS_EXPLANATION
        } else {
            LocationPermissionStatus.PERMANENTLY_DENIED
        }
    }

    private fun updateStatus(
        permissionsGranted: Map<String, Boolean>,
    ) {
        val granted = permissionsGranted.filter { it.value }
        val revoked = permissionsGranted.filter { !it.value }

        _status.value = if (revoked.isEmpty()) {
            LocationPermissionStatus.GRANTED
        } else if (granted.size == revoked.size) {
            LocationPermissionStatus.FINE_NOT_GRANTED
        } else if (shouldShowRationale()) {
            LocationPermissionStatus.NEEDS_EXPLANATION
        } else {
            LocationPermissionStatus.PERMANENTLY_DENIED
        }
    }

    fun requestLocationPermissions() {
        activity.lifecycleScope.launch {
            appDataStore.updateData {
                it.copy(wasLocationPermissionRequested = true)
            }
            _status.value = LocationPermissionStatus.IS_BEING_REQUESTED
            requestLocationPermissionsLauncher.launch()
        }
    }

    private fun shouldShowRationale(): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            activity,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}
