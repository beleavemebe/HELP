package company.vk.education.siriusapp.core

import androidx.activity.ComponentActivity
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentActivityProviderImpl @Inject constructor() : CurrentActivityProvider {

    private val resumedActivityList = LinkedList<ActivityRecord>()
    
    private val lastResumedActivity: ComponentActivity?
        get() = resumedActivityList.maxByOrNull { it.lastResumedTime }?.activity

    override val currentActivity: ComponentActivity?
        get() = lastResumedActivity
    
    fun activityResumed(activity: ComponentActivity) {
        resumedActivityList.removeAll { it.activity == activity }
        resumedActivityList.addFirst(ActivityRecord(activity, System.currentTimeMillis()))
    }

    fun activityPaused(activity: ComponentActivity) {
        resumedActivityList.removeAll { it.activity == activity }
    }

    private data class ActivityRecord(
        val activity: ComponentActivity,
        val lastResumedTime: Long
    )
}