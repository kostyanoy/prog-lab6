import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.java.KoinJavaComponent.inject
import java.net.Socket
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.SocketException

class ClientConnect(private val socket: Socket) : KoinComponent {
    private val clientRequest: ClientRequest by inject()
    fun start() {
        try {
            val gis = socket.getInputStream()
            val gos = socket.getOutputStream()
            val ois = ObjectInputStream(gis)
            val oos = ObjectOutputStream(gos)
            while (true) {
                val request = ois.readObject()
                val response = clientRequest.process(request)
                oos.writeObject(response)
                oos.flush()
            }
        } catch (e: SocketException) {
            println("Клиент отключился: ${socket.inetAddress.hostAddress}")
        } finally {
            socket.close()
        }
    }
}