import di.serverModule
import org.koin.core.context.GlobalContext.startKoin
import kotlin.concurrent.thread

fun main() {
    val server = ServerApp(2222)
    val thread = thread {
        while (true) {
            val command = readlnOrNull()
            if (command == "exit") {
                server.stop()
                break
            }
        }
    }
    startKoin {
        modules(serverModule)
    }
    server.start()
    thread.join()
}