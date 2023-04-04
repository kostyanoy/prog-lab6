package utils

import data.MusicBand
import java.time.LocalDateTime

/**
 * Interacts with the collection
 *
 * @property [LocalDateTime] contains the initialization date
 */
class StorageManager : Storage<LinkedHashMap<Int, MusicBand>, Int, MusicBand> {
    private val date: LocalDateTime = LocalDateTime.now()
    val musicBandCollection = LinkedHashMap<Int, MusicBand>()

    override fun getCollection(predicate: Map.Entry<Int, MusicBand>.() -> Boolean): LinkedHashMap<Int, MusicBand> =
        LinkedHashMap(musicBandCollection.filter(predicate))

    /**
     * Used in the Info command
     */
    override fun getInfo(): String {
        return "Коллекция  ${this.javaClass} \nтип: LinkedHashMap количество элементов  ${musicBandCollection.size} \nдата инициализации $date"
    }

    /**
     * Used in the Insert command
     */
    override fun insert(id: Int, element: MusicBand) {
        musicBandCollection[id] = element
    }

    /**
     * Used in the Update command
     */
    override fun update(id: Int, element: MusicBand) {
        musicBandCollection[id] = element
    }

    /**
     * Used in the Clear command
     */
    override fun clear() {
        musicBandCollection.clear()
    }

    /**
     * Used in RemoveKey, RemoveGreaterKey and RemoveGreater commands
     */
    override fun removeKey(id: Int) {
        musicBandCollection.remove(id)
    }
}


