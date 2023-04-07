import org.koin.core.component.KoinComponent


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
    abstract fun execute(args: Array<Any>) : CommandResult
    abstract fun getArgumentTypes() : Array<ArgumentType>
}
