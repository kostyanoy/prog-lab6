package serialize

import ArgumentType
import CommandResult
import data.MusicBand
import data.MusicGenre
import kotlinx.serialization.*
import kotlinx.serialization.builtins.ArraySerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PolymorphicKind
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.int
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@OptIn(ExperimentalSerializationApi::class, InternalSerializationApi::class)
object KAnySerializer : KSerializer<Any> {

    override val descriptor: SerialDescriptor = buildSerialDescriptor("AnySerializer", PolymorphicKind.SEALED) {
        element<String>("string")
        element<Int>("int")
        element("musicBand", MusicBand.serializer().descriptor)
        element("musicGenre", MusicGenre.serializer().descriptor)
        element<CommandResult.Success>("success")
        element<CommandResult.Failure>("failure")
        element<Array<@Contextual Any>>("list")
        element<Map<String, Array<ArgumentType>>>("map")
    }


    override fun serialize(encoder: Encoder, value: Any) {
        when (value) {
            is Int -> encoder.encodeInt(value)
            is String -> encoder.encodeString(value)
            is MusicBand -> encoder.encodeSerializableValue(MusicBand.serializer(), value)
            is MusicGenre -> encoder.encodeSerializableValue(MusicGenre.serializer(), value)
            is CommandResult.Success -> encoder.encodeSerializableValue(CommandResult.Success.serializer(), value)
            is CommandResult.Failure -> encoder.encodeSerializableValue(CommandResult.Failure.serializer(), value)
            is Array<*> -> encoder.encodeSerializableValue(ArraySerializer(KAnySerializer), value as Array<Any>)
            is Map<*, *> -> {
                encoder.encodeSerializableValue(
                    MapSerializer(
                        String.serializer(),
                        ArraySerializer(ArgumentType.serializer()),
                    ), value as Map<String, Array<ArgumentType>>
                )
            }

            else -> throw SerializationException("Unknown type: $value")
        }
    }

    override fun deserialize(decoder: Decoder): Any {
        val jsonDecoder = decoder as? JsonDecoder ?: throw SerializationException("Only JSON format is supported")
        val jsonElement = jsonDecoder.decodeJsonElement()
        return when {
            jsonElement.isArgumentType() -> jsonDecoder.json.decodeFromJsonElement(
                ArgumentType.serializer(),
                jsonElement
            )

            jsonElement.isMusicGenre() -> jsonDecoder.json.decodeFromJsonElement(
                MusicGenre.serializer(),
                jsonElement
            )

            jsonElement.isString() -> jsonElement.jsonPrimitive.content
            jsonElement.isInt() -> jsonElement.jsonPrimitive.int
            jsonElement.isObject() && jsonElement.jsonObject.containsKey("commandName")
                    && jsonElement.jsonObject.containsKey("throwable") -> jsonDecoder.json.decodeFromJsonElement(
                CommandResult.Failure.serializer(), jsonElement
            )

            jsonElement.isObject() && jsonElement.jsonObject.containsKey("commandName") -> jsonDecoder.json.decodeFromJsonElement(
                CommandResult.Success.serializer(), jsonElement
            )

            jsonElement.isObject() && jsonElement.jsonObject.containsKey("name") -> jsonDecoder.json.decodeFromJsonElement(
                MusicBand.serializer(), jsonElement
            )

            jsonElement.isArray() -> jsonDecoder.json.decodeFromJsonElement(
                ArraySerializer(KAnySerializer), jsonElement
            )

            else -> jsonDecoder.json.decodeFromJsonElement(
                MapSerializer(
                    String.serializer(),
                    ArraySerializer(ArgumentType.serializer()),
                ), jsonElement
            )
        }

    }
}


