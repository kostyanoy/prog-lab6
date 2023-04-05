package commands

import ArgumentType
import utils.CommandResult

/**
 * The command outputs information about the collection to the standard output stream
 */
class Info : StorageCommand() {
    override fun getDescription(): String = "info : вывести в стандартный поток вывода информацию о коллекции"

    override fun execute(args: Map<String, Any>): CommandResult {
        return CommandResult.Success("Info", storage.getInfo())
    }

    override fun getArgumentTypes(): Array<ArgumentType> = arrayOf()
}
