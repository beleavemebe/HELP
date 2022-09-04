package company.vk.education.siriusapp.ui.utils

import androidx.compose.runtime.compositionLocalOf

inline fun <reified T> unprovidedCompositionLocalOf() = compositionLocalOf<T> {
    error("${T::class.java.simpleName} is not provided.")
}