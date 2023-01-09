package com.help.android.ui.base

interface HelpViewModel<State : BaseViewState, Intent : BaseViewIntent, Error : BaseError, ViewEffect : BaseViewEffect> :
    StateContainer<State>, IntentConsumer<Intent>, ErrorSource<Error>,
    ViewEffectSource<ViewEffect> {
}
