package commands

import ArgumentType
import utils.CommandResult

/**
 * The command outputs all the elements of the collection in a string representation to the standard output stream
 */
class Show : StorageCommand() {
    override fun getDescription(): String =
        "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении"

    override fun execute(args: ArrayList<Any>): CommandResult {
        val message = buildString {
            appendLine("Коллекция содержит: ")
            storage.getCollection { true }
                .forEach { appendLine("${it.key} = ${it.value}") }
        }
        return CommandResult.Success("Show", message)
    }

    override fun getArgumentTypes(): Array<ArgumentType> = arrayOf()
}
