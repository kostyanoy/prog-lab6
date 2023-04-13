import di.serverModule
import org.koin.core.context.GlobalContext.startKoin
import kotlin.concurrent.thread
/**
Main function that starts the server application and listens to commands from the console.
The application can be stopped using the 'exit' command, and the collection can be saved or loaded.
 */
fun main() {
    val server = ServerApp(2228)
    val thread = thread {
        while (true) {
            val command = readlnOrNull()
            when (command) {
                "exit" -> {
                    server.stop()
                    break
                }
                "save" -> {
                    server.saveCollection()
                }
                "load" -> { server.loadCollection()
                }
            }

        }
    }
    startKoin {
        modules(serverModule)
    }
    server.start()
    thread.join()
}