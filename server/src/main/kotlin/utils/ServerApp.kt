import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.net.ServerSocket
import ClientConnect.*
import ClientRequest.*
import java.io.IOException
import java.net.Socket

class ServerApp(private val port: Int) : KoinComponent {
    private val server: Server by inject ()
    private val clientConnectFactory: (Socket) -> ClientConnect by inject()
    fun start() {
        println("Сервер запускается на порту: $port")
        try {
            //прослеживает подключения
            val serverSocket = ServerSocket(port)
            //как я поняла, такой +- должен быть бесконечный цикл, который ожидает подключения
            while (true) {
                val socket = serverSocket.accept() //блокируется пока новый клиент не подключится(каждый раз новый сокет будет)
                println("Произошло подключение клиента: ${socket.inetAddress.hostAddress}") //ip получает
                val clientConnect = clientConnectFactory(socket)
                clientConnect.start()
            }
        } catch (e: IOException) {
            println("Ошибка подключения клиента: ${e.message}")
        }
    }
}