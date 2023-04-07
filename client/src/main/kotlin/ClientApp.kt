import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import serialize.FrameSerializer
import utils.Interactor
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.net.ConnectException
import java.net.InetSocketAddress
import java.net.SocketTimeoutException
import java.nio.channels.SocketChannel

/**
 * Class of the application, uses the DI to get parameters
 */
class ClientApp(private val serverAddress: String, private val serverPort: Int) : KoinComponent {
    private val interactor by inject<Interactor>()
    private val frameSerializer by inject<FrameSerializer>()

    private lateinit var channel: SocketChannel

    fun start() {
        try {
            channel = SocketChannel.open(InetSocketAddress(serverAddress, serverPort))
            channel.socket().soTimeout = 5000

            println("Произошло подключение к ${channel.remoteAddress}")

            interactor.start(this)
            stop()
        } catch (e: SocketTimeoutException) {
            println("Сервер не отвечает (${e.message})")
        } catch (e: ConnectException) {
            println("Невозможно подключиться (${e.message})")
        }
    }

    fun stop() {
        channel.close()
    }

    fun sendFrame(frame: Frame) {
        val s = frameSerializer.serialize(frame)
        channel.socket().getOutputStream().write(s.toByteArray())
    }

    fun receiveFrame(): Frame {
        val array = ArrayList<Byte>()
        var char = channel.socket().getInputStream().read()
        while (char == -1 || Char(char) != '\n'){
            if (char == -1)
                continue
            array.add(char.toByte())
            char = channel.socket().getInputStream().read()
        }
        val str = String(array.toByteArray())
        return frameSerializer.deserialize(str)
    }
}

