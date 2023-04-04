package utils

interface Saver<T> {
    fun save(collection: T)

    fun load(): T
}