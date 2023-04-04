package commands

import ArgumentType
import exceptions.FileException
import org.jetbrains.kotlin.konan.file.File

import utils.CommandResult

/**
 * The command reads and executes the script from the specified file.
 *
 * Fails if no file is found.
 */

class ExecuteScript : Command() {
    override fun getDescription(): String =
        "execute_script : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме."

    override fun execute(args: ArrayList<Any>): CommandResult {
        val path = args[0] as String
        if (!File(path).exists) {
            return CommandResult.Failure("Execute_script", FileException("Файла команд не обнаружено"))
        }
        try {
            interactor.executeCommandFile(path)
        } catch (e: Throwable) {
            return CommandResult.Failure("Execute_script", e)
        }
        return CommandResult.Success("Execute_script")
    }

    override fun getArgumentTypes(): Array<ArgumentType> = arrayOf(ArgumentType.STRING)
}
