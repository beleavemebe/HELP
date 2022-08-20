package company.vk.education.siriusapp.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import company.vk.education.siriusapp.ui.utils.log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

abstract class BaseViewModel<State : BaseViewState, Intent : BaseViewIntent, Error>(
) : ViewModel(), HelpViewModel<State, Intent, Error> {

    private val _viewState: MutableStateFlow<State> by lazy { MutableStateFlow(initialState) }
    override val viewState: StateFlow<State> by lazy { _viewState.asStateFlow() }

    @ShitiusDsl
    protected fun reduce(f: suspend (prevState: State) -> State) {
        viewModelScope.launch {
            val newState = f(_viewState.value)
            _viewState.value = newState
            log("New state: $newState")
        }
    }

    override fun accept(intent: Intent): Any = Unit

    open fun mapThrowable(throwable: Throwable): Error = throw throwable

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
