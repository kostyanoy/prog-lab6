package utils

import Command
import commands.*
import exceptions.CommandException
import org.koin.core.component.KoinComponent

class CommandManager() : KoinComponent {
    val commands = mapOf<String, Command>(
        "help" to Help(),
        "info" to Info(),
        "show" to Show(),
        "clear" to Clear(),
        "insert" to Insert(),
        "update" to Update(),
        "remove_key" to RemoveKey(),
        "remove_greater" to RemoveGreater(),
        "replace_if_lowe" to ReplaceIfLowe(),
        "remove_greater_key" to RemoveGreaterKey(),
        "count_greater_than_description" to CountGreaterThanDescription(),
        "filter_less_than_genre" to FilterLessThanGenre(),
        "undo" to Undo()
    )

    fun getCommand(name: String): Command {
        val command = commands[name] ?: throw CommandException("Такой команды не существует")
        if (command is HiddenCommand && command.hidden()) {
            throw CommandException("Недостаточно прав доступа для выполнения этой команды")
        }
        return command
    }
}