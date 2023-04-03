import java.io.IOException
import java.net.ServerSocket

class ServerApp(private val port: Int): KoinComponent {
    private val application: Application by inject()
        println("Сервер запускается на порту: $port")
        try {
            //прослеживает подключения
            val serverSocket = ServerSocket(port)
            //как я поняла, такой +- должен быть бесконечный цикл, который ожидает подключения
            while (true) {
                val socket = serverSocket.accept()//блокируется пока новый клиент не подключится(каждый раз новый сокет будет)
                println("Произошло подключение клиента: ${socket.inetAddress.hostAddress}")//ip получает
                clientConnect(socket)//передает сокет для общения с клиентом
            }
        } catch (e: IOException) {
            println("Ошибка подключения клиента: ${e.message}")
        }
    }