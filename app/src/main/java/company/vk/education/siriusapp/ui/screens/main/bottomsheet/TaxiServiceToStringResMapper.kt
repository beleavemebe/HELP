package company.vk.education.siriusapp.ui.screens.main.bottomsheet

import androidx.annotation.StringRes
import company.vk.education.siriusapp.R
import company.vk.education.siriusapp.core.Mapper
import company.vk.education.siriusapp.domain.model.TaxiService
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