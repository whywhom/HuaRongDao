package com.huarongdao.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import okio.Path.Companion.toPath
import java.io.File

actual fun createDataStore(): DataStore<Preferences> {
    val dir = File(System.getProperty("user.home"), ".huarongdao").also { it.mkdirs() }
    return PreferenceDataStoreFactory.createWithPath(
        produceFile = { File(dir, DATASTORE_FILE).absolutePath.toPath() }
    )
}
