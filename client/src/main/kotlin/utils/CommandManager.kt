package utils

import ArgumentType
import ClientApp
import Frame
import FrameType
import exceptions.CommandException

class CommandManager {
    private val commands = mutableMapOf("help" to arrayOf<ArgumentType>())

    fun updateCommands(clientApp: ClientApp) {
        val frame = Frame(FrameType.LIST_OF_COMMANDS_REQUEST)
        clientApp.sendFrame(frame)
        val respond = clientApp.receiveFrame()
        commands.clear()
        commands.putAll(respond.body as Map<String, Array<ArgumentType>>)

    }

    fun getArgs(command: String): List<ArgumentType>{
        if (command !in commands){
            throw CommandException("Такой команды не существует")
        }

        return commands[command]!!.toList()
    }
}