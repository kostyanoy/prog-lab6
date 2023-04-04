import org.koin.core.component.KoinComponent
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

class ServerApp(private val port: Int) : KoinComponent {
    fun start() {
        println("Сервер запускается на порту: $port")
        try {
            //прослеживает подключения
            val serverSocket = ServerSocket(port)
            while (true) {
                val socket = serverSocket.accept() //блокируется пока новый клиент не подключится(каждый раз новый сокет будет)
                println("Произошло подключение клиента: ${socket.inetAddress.hostAddress}") //ip получает
                clientConnect(socket)
            }
        } catch (e: IOException) {
            println("Ошибка подключения клиента: ${e.message}")
        }
    }

    fun clientConnect(socket: Socket) {
        try {
            val gis = socket.getInputStream()
            val gos = socket.getOutputStream()
            val ois = ObjectInputStream(gis)
            val oos = ObjectOutputStream(gos)
            while (true) {
                val request = ois.readObject() as Frame
                val response = clientRequest(request)
                oos.writeObject(response)
                oos.flush()
            }
        } catch (e: SocketException) {
            println("Клиент отключился: ${socket.inetAddress.hostAddress}")
        } finally {
            socket.close()
        }
    }

    fun clientRequest(request: Frame) {
        // здесь будет обработка запросов через лямбды
        fun process(request: Any): String {
            return when (request) {

        }
        return process(request)
    }
}
