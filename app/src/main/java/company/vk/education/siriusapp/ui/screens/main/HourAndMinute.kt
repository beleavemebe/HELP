package company.vk.education.siriusapp.ui.screens.main

const val MINUTE_MS = 60 * 1000L
const val HOUR_MS = 60 * MINUTE_MS

const val DEFAULT_HOUR = 12
const val DEFAULT_MINUTE = 0

@JvmInline
value class HourAndMinute(
    private val pair: Pair<Int, Int> = DEFAULT_HOUR to DEFAULT_MINUTE
) {
    val hour get() = pair.first
    val minute get() = pair.second

    fun toMillis(): Long = (hour * HOUR_MS) + (minute * MINUTE_MS)
}

