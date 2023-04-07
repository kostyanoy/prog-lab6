package utils

import ArgumentType
import ClientApp
import CommandResult
import Frame
import FrameType
import commands.ExecuteScript
import commands.Exit
import commands.Help
import commands.UpdateCommands
import exceptions.CommandException
import java.nio.channels.ServerSocketChannel

class CommandManager {
    private val clientCommands = mapOf(
        "help" to Help(),
        "execute_script" to ExecuteScript(),
        "exit" to Exit(),
        "update_commands" to UpdateCommands(),
    )

    val commands = clientCommands.mapValues { e -> e.value.getArgumentTypes() }.toMutableMap()

    fun updateCommands(clientApp: ClientApp): Boolean {
        val frame = Frame(FrameType.LIST_OF_COMMANDS_REQUEST)
        clientApp.sendFrame(frame)
        val respond = clientApp.receiveFrame()
        val serverCommands = respond.body["commands"] as? Map<String, Array<ArgumentType>> ?: return false
        commands.clear()
        commands.putAll(clientCommands.mapValues { e -> e.value.getArgumentTypes() })
        commands.putAll(serverCommands)
        return true
    }

    private fun isClientCommand(command: String): Boolean = command in clientCommands

    fun executeCommand(clientApp: ClientApp, command: String, args: Array<Any>): CommandResult? {
        if (isClientCommand(command)) {
            return clientCommands[command]!!.execute(args)
        }

        val frame = Frame(FrameType.COMMAND_REQUEST)
        frame.setValue("name", command)
        frame.setValue("args", args)
        clientApp.sendFrame(frame)
        return clientApp.receiveFrame().body["data"] as? CommandResult
    }

    fun getArgs(command: String): Array<ArgumentType> {
        if (command !in commands) {
            throw CommandException("Такой команды не существует")
        }
        return commands[command]!!
    }
}

