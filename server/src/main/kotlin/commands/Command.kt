package commands

import ArgumentType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import utils.CommandResult
import utils.Interactor


/**
 * An abstract class for defining commands.
 */
abstract class Command : KoinComponent {
    val interactor: Interactor by inject()
    /**
    Returns a description of the command.
     */
    abstract fun getDescription() : String

    /**
     * Starts the execution of the command
     *
     * @return [CommandResult] with the name of the command and data or exception returned by the command
     */
    abstract fun execute(args: ArrayList<Any>) : CommandResult
    abstract fun getArgumentTypes() : Array<ArgumentType>
}
