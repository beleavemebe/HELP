package company.vk.education.siriusapp.data.appdata

import android.content.Context
import androidx.datastore.dataStore
import company.vk.education.siriusapp.data.APP_DATA_FILE_NAME

val Context.appDataStore by dataStore(APP_DATA_FILE_NAME, AppDataSerializer)