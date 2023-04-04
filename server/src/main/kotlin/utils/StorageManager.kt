package utils

import data.MusicBand
import java.time.LocalDateTime

class StorageManager : Storage<LinkedHashMap<Int, MusicBand>, Int, MusicBand> {
    private val date: LocalDateTime = LocalDateTime.now()
    val musicBandCollection = LinkedHashMap<Int, MusicBand>()

    override fun getCollection(predicate: Map.Entry<Int, MusicBand>.() -> Boolean): LinkedHashMap<Int, MusicBand> =
        LinkedHashMap(musicBandCollection.filter(predicate))

    override fun getInfo(): String {
        return "Коллекция  ${this.javaClass} \nтип: LinkedHashMap количество элементов  ${musicBandCollection.size} \nдата инициализации $date"
    }

    override fun insert(id: Int, element: MusicBand) {
        musicBandCollection[id] = element
    }

    override fun update(id: Int, element: MusicBand) {
        musicBandCollection[id] = element
    }

    override fun clear() {
        musicBandCollection.clear()
    }

    override fun removeKey(id: Int) {
        musicBandCollection.remove(id)
    }
}


