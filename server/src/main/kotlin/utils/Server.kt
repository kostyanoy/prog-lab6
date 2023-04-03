import di.appModule
import org.koin.core.context.GlobalContext.startKoin

class Server(private val port: Int) {
    fun main() {
        startKoin {
            modules(appModule)
        }
    }
}
