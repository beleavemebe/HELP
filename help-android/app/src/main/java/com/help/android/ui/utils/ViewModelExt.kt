@file:SuppressLint("ComposableNaming")
package com.help.android.ui.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import com.help.android.ui.base.*
import com.help.android.ui.base.BaseViewEffect
import com.help.android.ui.base.ViewEffectSource

@Composable
fun <VE: BaseViewEffect> ViewEffectSource<VE>.collectViewEffects(
    onViewEffect: suspend (viewEffect: VE?) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(viewEffects, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewEffects.collect { viewEffect ->
                onViewEffect(viewEffect)
            }
        }
    }
}
