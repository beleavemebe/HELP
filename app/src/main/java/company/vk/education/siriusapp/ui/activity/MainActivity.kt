package company.vk.education.siriusapp.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import company.vk.education.siriusapp.ui.fuckpermissiosn.LocationPermissionTracker
import company.vk.education.siriusapp.ui.screens.Screens
import company.vk.education.siriusapp.ui.screens.main.MainScreenDeps
import company.vk.education.siriusapp.ui.screens.main.mainScreen
import company.vk.education.siriusapp.ui.screens.trip.TripScreenDeps
import company.vk.education.siriusapp.ui.screens.trip.tripScreen
import company.vk.education.siriusapp.ui.screens.user.UserScreenDeps
import company.vk.education.siriusapp.ui.screens.user.userScreen
import company.vk.education.siriusapp.ui.utils.log
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    companion object {
        var fuckYou: Provider<LocationPermissionTracker>? = null
    }

    @Inject
    lateinit var godOfMappers: GodOfMappers

    @Inject
    lateinit var locationPermissionTracker: Provider<LocationPermissionTracker>

    private val tripScreenDeps by lazy { TripScreenDeps(godOfMappers) }
    private val mainScreenDeps by lazy { MainScreenDeps(godOfMappers) }
    private val userScreenDeps by lazy { UserScreenDeps(godOfMappers) }

    @OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val sheetState = rememberModalBottomSheetState(
                initialValue = ModalBottomSheetValue.Hidden,
                skipHalfExpanded = true
            )
            val bottomSheetNavigator = remember { BottomSheetNavigator(sheetState) }
            val navController = rememberNavController(bottomSheetNavigator)

            ModalBottomSheetLayout(bottomSheetNavigator) {
                NavHost(
                    navController = navController,
                    startDestination = Screens.Main.route
                ) {
                    mainScreen(navController, mainScreenDeps)
                    tripScreen(tripScreenDeps)
                    userScreen(navController, userScreenDeps)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        fuckYou = locationPermissionTracker
    }

    override fun onResume() {
        super.onResume()
//        requestLocationPermission()
    }

    private fun requestLocationPermission() = when {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED -> {
            // ok
        }

        ActivityCompat.shouldShowRequestPermissionRationale(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) -> {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            log("show rationale")
        }

        else -> {
            // Ask for both the ACCESS_FINE_LOCATION and ACCESS_COARSE_LOCATION permissions.

        }
    }
//    if (ContextCompat.checkSelfPermission(
//                this,
//                "android.permission.ACCESS_FINE_LOCATION"
//            )
//            != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this, arrayOf("android.permission.ACCESS_FINE_LOCATION"), 1
//            )
//        }
}

