package commands

import ArgumentType
import exceptions.FileException
import org.jetbrains.kotlin.konan.file.File
import utils.CommandResult

/**
 * The command loads the file with the collection
 *
 * * Fails if no saved file is found
 */
class Load : StorageCommand() {
    override fun getDescription(): String = "load : загрузить коллекцию из файла"

    override fun execute(args: ArrayList<Any>): CommandResult {
        if (!File("save.txt").exists) {
            return CommandResult.Failure("Load", FileException("Сохраненного файла не обнаружено"))
        }
        storage.clear()
        interactor.load()
            .forEach { storage.insert(it.key, it.value) }
        return CommandResult.Success("Load")
    }

    override fun getArgumentTypes(): Array<ArgumentType> = arrayOf()
}