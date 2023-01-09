package com.help.android.core

import androidx.activity.ComponentActivity
import java.util.LinkedList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrentActivityProviderImpl @Inject constructor() : CurrentActivityProvider {

    private val resumedActivityList = LinkedList<ComponentActivity>()

    override val currentActivity: ComponentActivity?
        get() = resumedActivityList.first
    
    fun activityCreated(activity: ComponentActivity) {
        resumedActivityList.removeAll { it == activity }
        resumedActivityList.addFirst(activity)
    }

    fun activityDestroyed(activity: ComponentActivity) {
        resumedActivityList.removeAll { it == activity }
    }
}