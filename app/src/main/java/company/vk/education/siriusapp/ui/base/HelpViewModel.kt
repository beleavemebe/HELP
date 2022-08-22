package company.vk.education.siriusapp.ui.base

import androidx.annotation.CallSuper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface HelpViewModel<State : BaseViewState, Intent : BaseViewIntent, Error> {
    val initialState: State
    val viewState: StateFlow<State>
    val errors: Flow<Error>

    fun accept(intent: Intent): Any
}