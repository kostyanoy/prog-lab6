package commands

import ArgumentType
import utils.CommandResult

/**
 * The command that clears the collection.
 */
class Clear : StorageCommand() {
    override fun getDescription(): String = "clear : очистить коллекцию"

    override fun execute(args: Map<String, Any>): CommandResult {
        storage.clear()
        return CommandResult.Success("Clear")
    }

    override fun getArgumentTypes(): Array<ArgumentType> = arrayOf()
}