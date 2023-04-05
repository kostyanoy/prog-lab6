package commands

import ArgumentType
import exceptions.FileException
import org.jetbrains.kotlin.konan.file.File
import org.koin.core.component.inject
import utils.CommandManager
import utils.CommandResult
import utils.FileSaver

/**
 * The command loads the file with the collection
 *
 * * Fails if no saved file is found
 */
class Load() :  HiddenCommand() {
    private val saver: FileSaver by inject()
    override fun hidden(): Boolean = true
    override fun getDescription(): String = "load : загрузить коллекцию из файла"

    override fun execute(args: Map<String, Any>): CommandResult {
        if (!File("save.txt").exists) {
            return CommandResult.Failure("Load", FileException("Сохраненного файла не обнаружено"))
        }
        storage.clear()
        saver.load()
            .forEach { storage.insert(it.key, it.value) }
        return CommandResult.Success("Load")
    }

    override fun getArgumentTypes(): Array<ArgumentType> = arrayOf()
}
