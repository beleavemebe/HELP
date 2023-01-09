package company.vk.education.siriusapp.ui.screens

object Screens {
    object Main {
        const val route = "main"
    }

    object User {
        const val KEY_USER_ID = "userId"
        const val route = "user/{$KEY_USER_ID}"

        fun buildRoute(userId: String) = "user/$userId"
    }

    object Trip {
        const val KEY_TRIP_ID = "tripId"
        const val route = "trip/{$KEY_TRIP_ID}"

        fun buildRoute(tripId: String) = "trip/$tripId"
    }
}