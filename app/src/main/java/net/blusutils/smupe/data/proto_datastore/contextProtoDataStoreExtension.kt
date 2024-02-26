package net.blusutils.smupe.data.proto_datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import net.blusutils.smupe.SmupeSettings

val Context.settingsDataStore: DataStore<SmupeSettings> by dataStore(
    fileName = "settings.pb",
    serializer = SettingsProtobufSerializer
)

