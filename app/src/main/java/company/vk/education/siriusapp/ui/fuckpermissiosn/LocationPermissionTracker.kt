package company.vk.education.siriusapp.ui.fuckpermissiosn

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import company.vk.education.siriusapp.core.CurrentActivityProvider
import company.vk.education.siriusapp.core.DtoMapper
import company.vk.education.siriusapp.data.DataStoreStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

enum class LocationPermissionStatus {
    NOT_REQUESTED,
    NEEDS_EXPLANATION,
    PERMANENTLY_DENIED,
    FINE_NOT_GRANTED,
    GRANTED
}

@ActivityScoped
class LocationPermissionTracker @Inject constructor(
    private val activityProvider: CurrentActivityProvider,
    private val store: LocationPermissionStateStore,
) {
    private val _status = MutableStateFlow<LocationPermissionStatus?>(null)
    val status = _status.asStateFlow()

    private val activity get() = activityProvider.currentActivity
        ?: error("Could not get an activity")

    init {
        updateStatus()
    }

    private fun updateStatus() {
        _status.value = if (isLocationPermissionGranted()) {
            LocationPermissionStatus.GRANTED
        } else if (wasLocationPermissionRequested().not()) {
            LocationPermissionStatus.NOT_REQUESTED
        } else if (shouldShowRationale()) {
            LocationPermissionStatus.NEEDS_EXPLANATION
        } else {
            LocationPermissionStatus.PERMANENTLY_DENIED
        }
    }

    private fun wasLocationPermissionRequested(): Boolean {
        return runBlocking { // fixme
            store.read()?.wasPermissionRequested ?: false
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

    fun requestLocationPermissions() {
        activity.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions(),
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        ) { map ->
            val granted = map.filter { it.value }
            val revoked = map.filter { !it.value }

            _status.value = if (granted.size == revoked.size) {
                LocationPermissionStatus.FINE_NOT_GRANTED
            } else if (granted.isEmpty()) {
                LocationPermissionStatus.NEEDS_EXPLANATION
            } else {
                LocationPermissionStatus.GRANTED
            }
        }

        runBlocking {
            store.save(LocationPermissionState(wasPermissionRequested = true))
        }
    }
}

data class LocationPermissionState(val wasPermissionRequested: Boolean)

object LocationPermissionStateMapper : DtoMapper<LocationPermissionState, String> {
    override fun mapToDto(arg: LocationPermissionState): String {
        return arg.wasPermissionRequested.toString()
    }

    override fun mapToEntity(arg: String): LocationPermissionState {
        return LocationPermissionState(wasPermissionRequested = arg.toBoolean())
    }
}

class LocationPermissionStateStore @Inject constructor(
    @ApplicationContext val context: Context
) : DataStoreStorage<LocationPermissionState>(
    "locationPermissionState",
    LocationPermissionStateMapper,
    context
)