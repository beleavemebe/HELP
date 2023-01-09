package com.help.android.data.appdata

import androidx.datastore.core.Serializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object AppDataSerializer : Serializer<AppData> {
    override val defaultValue: AppData
        get() = AppData()

    override suspend fun readFrom(input: InputStream): AppData {
        return try {
            Json.decodeFromString(
                deserializer = AppData.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            defaultValue
        }
    }

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun writeTo(t: AppData, output: OutputStream) {
        output.write(
            Json.encodeToString(
                serializer = AppData.serializer(),
                value = t
            ).encodeToByteArray()
        )
    }
}