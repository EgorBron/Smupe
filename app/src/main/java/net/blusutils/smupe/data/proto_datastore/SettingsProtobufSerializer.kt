package net.blusutils.smupe.data.proto_datastore

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import net.blusutils.smupe.SmupeSettings
import java.io.InputStream
import java.io.OutputStream


object SettingsProtobufSerializer : Serializer<SmupeSettings> {
    override val defaultValue: SmupeSettings =
        SmupeSettings
            .getDefaultInstance()
            .toBuilder()
            .apply {
                actionMenuElementsOrder = "012345"
            }
            .build()

    override suspend fun readFrom(input: InputStream): SmupeSettings {
        try {
            return SmupeSettings.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto for settings.", exception)
        }
    }

    override suspend fun writeTo(
        t: SmupeSettings,
        output: OutputStream
    ) = t.writeTo(output)
}
