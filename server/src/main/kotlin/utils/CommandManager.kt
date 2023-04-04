package utils

import commands.*
import org.koin.core.component.KoinComponent

/**
 * The class is used to refer to commands
 */
class CommandManager() : KoinComponent {
    val commands = mapOf<String, Command>(
        "help" to Help(),
        "info" to Info(),
        "show" to Show(),
        "clear" to Clear(),
        "insert" to Insert(),
        "update" to Update(),
        "remove_key" to RemoveKey(),
        "save" to Save(),
        "load" to Load(),
        "execute_script" to ExecuteScript(),
        "exit" to Exit(),
        "remove_greater" to RemoveGreater(),
        "replace_if_lowe" to ReplaceIfLowe(),
        "remove_greater_key" to RemoveGreaterKey(),
        "count_greater_than_description" to CountGreaterThanDescription(),
        "filter_less_than_genre" to FilterLessThanGenre(),
        "undo" to Undo()
    )

    /**
     * Checks if the command exists
     */
}