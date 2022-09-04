package company.vk.education.siriusapp.ui.activity

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
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import company.vk.education.siriusapp.ui.screens.Screens
import company.vk.education.siriusapp.ui.screens.main.*
import company.vk.education.siriusapp.ui.screens.main.trip.tripScreen
import company.vk.education.siriusapp.ui.screens.trip.*
import company.vk.education.siriusapp.ui.screens.user.UserScreenDeps
import company.vk.education.siriusapp.ui.screens.user.userScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject lateinit var godOfMappers: GodOfMappers

    private val tripScreenDeps by lazy { TripScreenDeps(godOfMappers) }
    private val mainScreenDeps by lazy { MainScreenDeps(godOfMappers) }
    private val userScreenDeps by lazy { UserScreenDeps(godOfMappers) }

    @OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalMaterialApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestLocationPermission()
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

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                "android.permission.ACCESS_FINE_LOCATION"
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf("android.permission.ACCESS_FINE_LOCATION"), 1
            )
        }
    }
}

