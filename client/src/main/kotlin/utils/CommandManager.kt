package utils

import ArgumentType
import ClientApp
import Frame
import exceptions.CommandException

class CommandManager {
    private val commands = mapOf("help" to arrayOf<ArgumentType>())

    fun updateCommands(clientApp: ClientApp) {
        clientApp.sendFrame(Frame())
        TODO()
    }

    fun getArgs(command: String): List<ArgumentType>{
        if (command !in commands){
            throw CommandException("Такой команды не существует")
        }

        return commands[command]!!.toList()
    }
}