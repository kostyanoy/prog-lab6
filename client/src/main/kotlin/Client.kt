import di.clientModule
import org.koin.core.context.GlobalContext.startKoin
import serialize.FrameSerializer


/**
 * Main function that starts the application
 */
fun main() {
    startKoin {
        modules(clientModule)
    }

    var command = "connect"

    while (command != "exit") {
        if (command == "connect") {
            ClientApp("localhost", 2228).start()
            println("Клиент закрылся")
        }
        print("connect or exit: ")
        command = readln()
    }
}

