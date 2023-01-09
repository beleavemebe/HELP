package com.help.android.data.appdata

import android.content.Context
import androidx.datastore.dataStore
import com.help.android.data.APP_DATA_FILE_NAME

val Context.appDataStore by dataStore(APP_DATA_FILE_NAME, AppDataSerializer)