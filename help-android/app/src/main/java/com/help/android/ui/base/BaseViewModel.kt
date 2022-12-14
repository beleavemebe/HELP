package com.help.android.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.help.android.ui.utils.log
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex

abstract class BaseViewModel<State : BaseViewState, Intent : BaseViewIntent, Error : BaseError, ViewEffect : BaseViewEffect>(
) : ViewModel(), HelpViewModel<State, Intent, Error, ViewEffect> {

    private val _viewState: MutableStateFlow<State> by lazy { MutableStateFlow(initialState) }
    override val viewState: StateFlow<State> by lazy { _viewState.asStateFlow() }

    private val viewStateMutex = Mutex()

    @ShitiusDsl
    protected fun reduce(f: suspend (prevState: State) -> State) = execute {
        viewStateMutex.lock()
        try {
            val newState = f(_viewState.value)
            _viewState.value = newState
            log("New state: $newState")
        } catch (throwable: Throwable) {
            throw throwable
        } finally {
            viewStateMutex.unlock()
        }
    }

    override fun consume(intent: Intent): Any = Unit

    open fun mapThrowable(throwable: Throwable): Error = throw throwable

    private val _errors = MutableSharedFlow<Error>(replay = 1)
    override val errors: Flow<Error> = _errors

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            val err = mapThrowable(throwable)
            _errors.emit(err)
        }
    }

    @ShitiusDsl
    protected fun execute(block: suspend () -> Unit) {
        viewModelScope.launch(exceptionHandler) {
            block()
        }
    }

    private val _viewEffects = MutableSharedFlow<ViewEffect>()
    override val viewEffects = _viewEffects.asSharedFlow()

    @ShitiusDsl
    protected fun postViewEffect(produceViewEffect: suspend () -> ViewEffect?) = execute {
        val effect = produceViewEffect()
        log("View effect: $effect")
        _viewEffects.emit(
            effect ?: return@execute
        )
    }
}
