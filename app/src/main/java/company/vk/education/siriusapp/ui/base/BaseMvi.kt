package company.vk.education.siriusapp.ui.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BaseViewState

interface StateContainer<State : BaseViewState> {
    val initialState: State
    val viewState: StateFlow<State>
}

interface BaseViewIntent

fun interface IntentConsumer<Intent : BaseViewIntent> {
    fun consume(intent: Intent): Any
}

interface BaseError

interface ErrorSource<Error : BaseError> {
    val errors: Flow<Error>
}

interface BaseViewEffect

interface ViewEffectSource<ViewEffect : BaseViewEffect> {
    val viewEffects: Flow<ViewEffect>
}