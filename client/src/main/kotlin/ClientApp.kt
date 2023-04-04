import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import serialize.FrameSerializer
import utils.Interactor
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.Socket

/**
 * Class of the application, uses the DI to get parameters
 */
class ClientApp(private val serverPort: Int) : KoinComponent {
    private val interactor by inject<Interactor>()
    private val frameSerializer by inject<FrameSerializer>()

    private val serverAddress = "localhost"
    lateinit var socket: Socket
        private set

    fun start() {
        socket = Socket(serverAddress, serverPort)

        interactor.start(this)

        socket.close()
    }

    fun sendFrame(frame: Frame) {
        val oos = ObjectOutputStream(socket.getOutputStream())
        oos.writeObject(frameSerializer.serialize(frame))
    }

    fun receiveFrame(): Frame {
        val ois = ObjectInputStream(socket.getInputStream())
        return frameSerializer.deserialize(ois.readObject() as String)
    }
}

