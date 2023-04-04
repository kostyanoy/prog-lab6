package utils

import data.MusicBand
import serialize.Serializer
import utils.serialize.SerializeManager


class FileSaver(
    private val pathToSaveFile: String = "save.txt",
    private val serializer: Serializer<LinkedHashMap<Int, MusicBand>> = SerializeManager(),
    private val fileManager: FileManager = FileManager()
) : Saver<LinkedHashMap<Int, MusicBand>> {
    override fun load(): LinkedHashMap<Int, MusicBand> =
        serializer.deserialize(fileManager.readFile(pathToSaveFile))

    override fun save(collection: LinkedHashMap<Int, MusicBand>) {
        fileManager.writeFile(pathToSaveFile, serializer.serialize(collection))
    }
}