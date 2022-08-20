@file:Suppress("unused")
package company.vk.education.siriusapp.ui.utils

import android.util.Log

const val APP_TAG = "HELP"

fun Any.log(msg: String?) = Log.d(APP_TAG, msg.toString())

fun log(msg: String?) = Unit.log(msg)
