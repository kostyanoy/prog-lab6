package commands

import ArgumentType
import CommandResult

class Load() : HiddenCommand() {
    override fun hidden(): Boolean = true
    override fun getDescription(): String = "load : загрузить коллекцию из файла"
    override fun execute(args: Array<Any>): CommandResult {
        serverApp.loadCollection()
        return CommandResult.Success("Load")
    }

    override fun getArgumentTypes(): Array<ArgumentType> = arrayOf()
}