package commands

import ArgumentType
import utils.CommandResult

/**
 * The command terminates the program.
 */
class Exit : Command() {
    override fun getDescription(): String = "exit : завершить программу (без сохранения в файл)"

    override fun execute(args: ArrayList<Any>): CommandResult {
        interactor.exit()
        return CommandResult.Success("Exit")
    }

    override fun getArgumentTypes(): Array<ArgumentType> = arrayOf()
}
