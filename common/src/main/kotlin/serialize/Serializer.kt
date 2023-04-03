package serialize

/**
 * Interface for serialization of [LinkedHashMap]
 */
interface Serializer<T> {
    /**
     * Serialize [LinkedHashMap] collection to [String]
     *
     * @param collection the [LinkedHashMap] collection that will be serialized
     * @return serialized [String]
     */
    fun serialize(collection: T): String

    /**
     * Deserialize [LinkedHashMap] collection from [String]
     *
     * @param serialized the serialized [String] that will be deserialized
     * @return deserialized [LinkedHashMap] collection
     */
    fun deserialize(serialized: String): T
}