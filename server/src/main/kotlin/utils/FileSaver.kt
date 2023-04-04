package utils

import data.MusicBand
import serialize.Serializer
import utils.serialize.SerializeManager


/**
 * Implements the [Saver] interface with file
 *
 * @param pathToSaveFile the path to the save file
 * @param serializer implementation of the [Serializer] interface used to serialize the collection
 * @param fileManager used to work with files
 */
class FileSaver(
    private val pathToSaveFile: String = "save.txt",
    private val serializer: Serializer<LinkedHashMap<Int, MusicBand>> = SerializeManager(),
    private val fileManager: FileManager = FileManager()
) : Saver<LinkedHashMap<Int, MusicBand>> {
    /**
     * Loads the [LinkedHashMap] collection from the save file
     *
     * @return [LinkedHashMap] collection deserialized from the content of the save file
     */
    override fun load(): LinkedHashMap<Int, MusicBand> =
        serializer.deserialize(fileManager.readFile(pathToSaveFile))

    /**
     * Saves the [LinkedHashMap] collection to the save file, serializes the collection
     *
     * @param collection the [LinkedHashMap] that should be saved to file
     */
    override fun save(collection: LinkedHashMap<Int, MusicBand>) {
        fileManager.writeFile(pathToSaveFile, serializer.serialize(collection))
    }
}