package commands

import ArgumentType
import org.koin.core.component.inject
import utils.CommandResult

/**
Represents an undo command that undoes the previous executed command.
 */
class Undo : Command() {
    private val history: CommandHistory by inject()
    override fun getDescription(): String = "undo : отмена последней выполненной команды"
    override fun execute(args: ArrayList<Any>): CommandResult = history.undoLastCommand()
    override fun getArgumentTypes(): Array<ArgumentType> = arrayOf()

}