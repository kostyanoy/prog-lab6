package utils

import exceptions.FileException
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.util.*

class FileManager {
    fun writeFile(path: String, text: String) {
        FileOutputStream(path).use { fos ->
            OutputStreamWriter(fos, Charsets.UTF_8).use { osw ->
                BufferedWriter(osw).use { bf -> bf.write(text) }
            }
        }
    }
    fun readFile(path: String): String {
        if (!File(path).canRead()) {
            throw FileException("Не получается открыть файл")
        }
        val scanner = Scanner(File(path))
        return buildString {
            while (scanner.hasNextLine()) {
                append(scanner.nextLine())
                if (scanner.hasNextLine()) append("\n")
            }
        }
    }
}