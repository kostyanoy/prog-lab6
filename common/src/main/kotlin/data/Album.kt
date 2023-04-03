package data

import kotlinx.serialization.Serializable

/**
 * Data class, represents the album
 *
 * @param name the name of album, should not be empty
 * @param length the length of album in milliseconds
 */
@Serializable
data class Album(
    val name: String, //Поле не может быть null, Строка не может быть пустой
    val length: Long  //Значение поля должно быть больше 0
)