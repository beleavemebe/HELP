package company.vk.education.siriusapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import company.vk.education.siriusapp.ui.screens.main.MainScreenState
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : BaseViewState, Intent : BaseViewIntent, Error>(
) : ViewModel(), HelpViewModel<State, Intent, Error> {

    private val _viewState: MutableStateFlow<State> by lazy { MutableStateFlow(initialState) }
    override val viewState: StateFlow<State> = _viewState

    @ShitiusDsl
    protected fun reduce(f: suspend (prevState: State) -> State) {
        viewModelScope.launch {
            val newState = f(_viewState.value)
            _viewState.value = newState
        }
    }

    override fun accept(intent: Intent): Any = Unit

    open fun mapThrowable(throwable: Throwable): Error = error(throwable.toString())

    private val _errors = MutableSharedFlow<Error>(replay = 1)
    override val errors: Flow<Error> = _errors

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            val err = mapThrowable(throwable)
            _errors.emit(err)
        }
    }

    protected fun execute(block: suspend () -> Unit) {
        viewModelScope.launch(exceptionHandler) {
            block()
        }
    }
}
