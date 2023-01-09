package com.help.android.ui.utils

import androidx.compose.runtime.compositionLocalOf

inline fun <reified T> unprovidedCompositionLocalOf() = compositionLocalOf<T> {
    error("${T::class.java.simpleName} is not provided.")
}