package company.vk.education.siriusapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : BaseViewState, Intent : BaseViewIntent, Err> : ViewModel() {

    abstract val initialState: State

    private val _viewState: MutableStateFlow<State> by lazy { MutableStateFlow(initialState) }
    val viewState: StateFlow<State> = _viewState

    @ShitiusDsl
    protected fun reduce(f: suspend (prevState: State) -> State) = viewModelScope.launch {
        val newState = f(_viewState.value)
        _viewState.value = newState
    }

    open fun accept(intent: Intent): Any = Unit

    open fun mapThrowable(throwable: Throwable): Err = error(throwable.toString())

    private val _errors = MutableSharedFlow<Err>(replay = 1)
    val errors: Flow<Err> = _errors

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
