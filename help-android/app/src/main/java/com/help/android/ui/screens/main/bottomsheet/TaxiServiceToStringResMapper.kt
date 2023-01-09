package com.help.android.ui.screens.main.bottomsheet

import androidx.annotation.StringRes
import com.help.android.R
import com.help.android.core.Mapper
import com.help.android.domain.model.TaxiService
import javax.inject.Inject

class TaxiServiceToStringResMapper @Inject constructor() : Mapper<TaxiService, Int> {
    @StringRes
    override fun map(arg: TaxiService): Int {
        return when (arg) {
            is TaxiService.Yandex -> R.string.yandex_taxi
            is TaxiService.Uber -> R.string.uber
        }
    }
}