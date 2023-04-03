package di
import ClientConnect
import Server
import ServerApp
import org.koin.dsl.module
import java.net.Socket

val appModule = module {
    single {
        Server(2222)
    }
    factory { (socket: Socket) -> ClientConnect(socket) }
    single { ServerApp(2222) }
}
