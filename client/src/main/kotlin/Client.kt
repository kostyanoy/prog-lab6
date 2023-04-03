import di.clientModule
import org.koin.core.context.startKoin


/**
 * Main function that starts the application
 */
fun main() {
    startKoin {
        modules(clientModule)
    }

    ClientApp(12345).start()
}

