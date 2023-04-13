import di.serverModule
import org.koin.core.context.GlobalContext.startKoin
import kotlin.concurrent.thread

fun main() {
    val server = ServerApp(2228)
    val thread = thread {
        while (true) {
            val command = readlnOrNull()
            when (command) {
                "exit" -> {
                    server.stop()
                }
                "save" -> {
                    server.saveCollection()
                }
                "load" -> { server.loadCollection()

                }
            }
            break

        }
    }

    startKoin {
        modules(serverModule)
    }
    server.start()
    thread.join()
}