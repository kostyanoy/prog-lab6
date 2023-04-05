package utils

import ArgumentType
import ClientApp
import FileManager
import Frame
import FrameType
import data.MusicGenre
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

/**
 * Implements [Interactor] interface. Executes load command on start
 *
 * @param userManager used to show messages to user and get input from user
 * @param fileManager used to execute command file
 */
class InteractionManager(
    private val userManager: ReaderWriter,
    private val fileManager: FileManager
) : KoinComponent, Interactor{

    private val validator: Validator by inject()
    private val commandManager: CommandManager by inject()
    private val invitation = ">>>"
    private var isActive = true
    private var lastArgument: String? = null
    private val executingFiles = ArrayDeque<String>()
    private lateinit var clientApp: ClientApp

    override fun start(clientApp: ClientApp) {
        this.clientApp = clientApp
        userManager.writeLine("Произошло подключение к ${clientApp.socket.inetAddress}:${clientApp.socket.port}")
        userManager.writeLine("Здрасьте, для вывода списка команд введите help")
        while (isActive) {
            interact()
        }
    }

    override fun exit() {
        isActive = false
    }

    override fun executeCommandFile(path: String) {
        val text = fileManager.readFile(path)
        if (path in executingFiles) {
            userManager.writeLine("Предотвращение зацикливания!")
            return
        }
        executingFiles.add(path)
        FileInteractor(this, text.lines()).start(clientApp)
        executingFiles.removeLast()
    }

    private fun interact() {
        userManager.write(invitation)
        val input = userManager.readLine().split(" ")
        if (input.count() > 2) {
            userManager.writeLine("Слишком много аргументов в строке")
            return
        }
        try {
            val command = input[0]
            lastArgument = if (input.count() == 2) input[1] else null
            executeCommand(command)
        } catch (e: Throwable) {
            userManager.writeLine(e.message ?: "")
        } finally {
            executingFiles.clear()
        }
    }

    override fun executeCommand(command: String) {
        val argTypes = commandManager.getArgs(command)
        val args = getArgs(argTypes)
        clientApp.sendFrame(Frame(FrameType.COMMAND_REQUEST))
        TODO()
//        when (val result = command.execute(args)) {
//            is CommandResult.Failure -> userManager.writeLine("Команда ${result.commandName} завершилась ошибкой: ${result.throwable.message}")
//            is CommandResult.Success -> {
//                userManager.writeLine("Команда ${result.commandName} исполнена.")
//                result.message?.let { userManager.writeLine(it) }
//                history.executedCommand(command)
//            }
//        }
    }

    override fun getArgs(argTypes: List<ArgumentType>): ArrayList<Any> {
        val args = arrayListOf<Any>()
        argTypes.forEach {
            args.add(when (it) {
                ArgumentType.INT -> lastArgument?.toIntOrNull() ?: validator.getInt()
                ArgumentType.STRING -> lastArgument ?: validator.getString()
                ArgumentType.GENRE -> lastArgument?.let { MusicGenre.valueOfOrNull(it) } ?: validator.getGenre()
                ArgumentType.MUSIC_BAND -> validator.getMusicBand()
            })
        }
        return args
    }
}