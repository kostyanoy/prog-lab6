import di.serverModule
import org.koin.core.context.GlobalContext.startKoin

    fun main() {
        startKoin {
            modules(serverModule)
        }
        ServerApp(2222).start()
    }

