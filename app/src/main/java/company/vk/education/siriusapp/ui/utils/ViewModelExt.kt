package company.vk.education.siriusapp.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import company.vk.education.siriusapp.ui.base.BaseViewIntent
import company.vk.education.siriusapp.ui.base.BaseViewState
import company.vk.education.siriusapp.ui.base.HelpViewModel


@Composable
fun <S : BaseViewState, I : BaseViewIntent, E, VE> HelpViewModel<S, I, E, VE>.collectViewEffects(
    onViewEffect: suspend (viewEffect: VE) -> Unit
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

@Composable
fun <S : BaseViewState, I : BaseViewIntent, E, VE> HelpViewModel<S, I, E, VE>.collectErrors(
    onError: suspend (error: E) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(viewEffects, lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            errors.collect { viewEffect ->
                onError(viewEffect)
            }
        }
    }
}