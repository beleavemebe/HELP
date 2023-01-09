@file:SuppressLint("ComposableNaming")
package company.vk.education.siriusapp.ui.utils

import android.annotation.SuppressLint
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import company.vk.education.siriusapp.ui.base.*

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
