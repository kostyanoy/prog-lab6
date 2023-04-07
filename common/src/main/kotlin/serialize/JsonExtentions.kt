package serialize

import ArgumentType
import data.MusicGenre
import kotlinx.serialization.json.*

fun JsonElement.isString() : Boolean {
    return try {
        if (this.jsonPrimitive.isString)
            return true
        false
    } catch (e: IllegalArgumentException) {
        false
    }
}

fun JsonElement.isInt() : Boolean {
    return try {
        this.jsonPrimitive.int
        true
    } catch (e: IllegalArgumentException) {
        false
    } catch (e: NumberFormatException) {
        false
    }
}

fun JsonElement.isObject() : Boolean {
    return try {
        this.jsonObject
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}

fun JsonElement.isArray() : Boolean {
    return try {
        this.jsonArray
        true
    } catch (e: IllegalArgumentException) {
        false
    }
}

fun JsonElement.isArgumentType() : Boolean {
    return try {
        if (this.isString() && jsonArray.jsonPrimitive.content in ArgumentType.values().map { it.toString() }){
            true
        }
        false
    } catch (e: IllegalArgumentException) {
        false
    }
}

fun JsonElement.isMusicGenre() : Boolean {
    return try {
        if (this.isString() && jsonArray.jsonPrimitive.content in MusicGenre.values().map { it.toString() }){
            true
        }
        false
    } catch (e: IllegalArgumentException) {
        false
    }
}

