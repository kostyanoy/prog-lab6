package commands

import ArgumentType
import org.koin.core.component.inject
import utils.CommandManager
import utils.CommandResult
import utils.FileSaver

/**
 * The command saves the collection to a file.
 */
class Save() :  HiddenCommand() {
    private val saver: FileSaver by inject()
    override fun hidden(): Boolean = true
    override fun getDescription(): String = "save : сохранить коллекцию в файл"

    override fun execute(args: Map<String, Any>): CommandResult {
        saver.save(storage.getCollection { true })
        return CommandResult.Success("Save")
    }

    override fun getArgumentTypes(): Array<ArgumentType> = arrayOf()

}
