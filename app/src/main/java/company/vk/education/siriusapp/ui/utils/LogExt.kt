package company.vk.education.siriusapp.ui.utils

import android.util.Log

const val APP_TAG = "HELP"

@Suppress("unused")
fun Any.log(msg: String?) = Log.d(APP_TAG, msg.toString())
