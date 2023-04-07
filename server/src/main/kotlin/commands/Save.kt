package commands
import ArgumentType
import utils.CommandResult

class Save() : HiddenCommand() {
    override fun hidden(): Boolean = true
    override fun getDescription(): String = "save : сохранить коллекцию в файл"

    override fun execute(args: Map<String, Any>): CommandResult {
        serverApp.saveCollection()
        return CommandResult.Success("Save")
    }

    override fun getArgumentTypes(): Array<ArgumentType> = arrayOf()
}