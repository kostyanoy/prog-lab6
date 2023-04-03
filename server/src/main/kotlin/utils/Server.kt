import di.appModule
import org.koin.core.context.GlobalContext.startKoin
import org.koin.core.context.GlobalContext.stopKoin
import org.koin.core.time.Timer.Companion.start

class Server(private val port: Int) {
    fun main(args: Array<String>) {
        startKoin {
            modules(appModule)
        }
        start()
        stopKoin()
    }
}