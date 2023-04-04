package utils

import commands.Command
import data.MusicBand

/**
 * Interface for connecting parts of the program
 */
interface Interactor : Saver<LinkedHashMap<Int, MusicBand>> {
    /**
     * Starts the interaction with the user
     */
    fun start()

    /**
     * Ends interaction with user
     */
    fun exit()

    /**
     * Show [message] to the user
     *
     * @param message the message that user must see
     */
    fun showMessage(message: String)

    /**
     * Show the invitation message to the user. Does not create new line
     *
     * @param message the invitation for input
     */
    fun showInvitation(message: String)

    /**
     * Executes the command file located at [path] in the OS
     *
     * @param path the location of the file in the OS
     */
    fun executeCommandFile(path: String)
    fun getArgs(command: Command): ArrayList<Any>
    fun executeCommand(command: Command)
}
