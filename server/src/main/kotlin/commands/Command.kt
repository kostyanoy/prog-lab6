package commands

import ArgumentType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import utils.CommandResult


/**
 * An abstract class for defining commands.
 */
abstract class Command : KoinComponent {
    /**
    Returns a description of the command.
     */
    abstract fun getDescription() : String

    /**
     * Starts the execution of the command
     *
     * @return [CommandResult] with the name of the command and data or exception returned by the command
     */
    abstract fun execute(args: Map<String, Any>) : CommandResult
    abstract fun getArgumentTypes() : Array<ArgumentType>
}
