package com.huarongdao.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import okio.Path.Companion.toPath
import com.huarongdao.data.database.appContext

actual fun createDataStore(): DataStore<Preferences> {
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = {
            appContext.filesDir.resolve(DATASTORE_FILE).absolutePath.toPath()
        }
    )
}
