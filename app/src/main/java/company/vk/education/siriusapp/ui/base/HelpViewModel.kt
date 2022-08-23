package company.vk.education.siriusapp.ui.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface HelpViewModel<State : BaseViewState, Intent : BaseViewIntent, Error, ViewEffect> {
    val initialState: State
    val viewState: StateFlow<State>
    val errors: Flow<Error>
    val viewEffects: Flow<ViewEffect>

    fun accept(intent: Intent): Any
}
