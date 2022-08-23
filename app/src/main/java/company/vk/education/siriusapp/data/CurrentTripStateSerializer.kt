package company.vk.education.siriusapp.data

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import company.vk.education.siriusapp.CurrentTripStateMsg
import java.io.InputStream
import java.io.OutputStream

object CurrentTripStateMsgSerializer : Serializer<CurrentTripStateMsg> {
    override val defaultValue: CurrentTripStateMsg = CurrentTripStateMsg.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): CurrentTripStateMsg {
        try {
            return CurrentTripStateMsg.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Could not read proto.", e)
        }
    }

    override suspend fun writeTo(t: CurrentTripStateMsg, output: OutputStream) = t.writeTo(output)
}
