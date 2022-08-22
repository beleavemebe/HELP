@file:Suppress("unused")
package company.vk.education.siriusapp.ui.utils

import timber.log.Timber

const val APP_TAG = "HELP"

fun Any.log(msg: String?) = Timber.tag(APP_TAG).d(msg.toString())

fun log(msg: String?) = Unit.log(msg)
