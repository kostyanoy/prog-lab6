package serialize

import Frame
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

/**
 * Implements [Serializer] interface with JSON serialization for Frames
 */
class FrameSerializer : Serializer<Frame> {
    private val serializer = Json

    override fun serialize(collection: Frame): String = serializer.encodeToString(collection)
    override fun deserialize(serialized: String): Frame = serializer.decodeFromString(serialized)

}