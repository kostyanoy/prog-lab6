import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import serialize.FrameSerializer
import utils.Interactor
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

    /**
     * Connects to the server
     */
    fun start() {
        try {
            channel = SocketChannel.open(InetSocketAddress(serverAddress, serverPort))
            channel.socket().soTimeout = 5000 // timeout for server respond

            println("Произошло подключение к ${channel.remoteAddress}")

            interactor.start(this)
        } catch (e: SocketTimeoutException) {
            println("Сервер не отвечает (${e.message})")
        } catch (e: ConnectException) {
            println("Невозможно подключиться (${e.message})")
        } finally {
            stop()
        }
    }

    /**
     * Closes the connection
     */
    fun stop() {
        if (channel.isConnected)
            channel.finishConnect()
        if (channel.isOpen)
            channel.close()
    }

    /**
     * Sends frame to the server
     *
     * @param frame which should be sent
     */
    fun sendFrame(frame: Frame) {
        val s = frameSerializer.serialize(frame)
        channel.socket().getOutputStream().write(s.toByteArray())
    }

    /**
     * Receives frame to the server
     *
     * @return [Frame] which server sent
     */
    fun receiveFrame(): Frame {
        val array = ArrayList<Byte>()
        var char = channel.socket().getInputStream().read()
        //not very good way to do this/ Can be stuck forever.
        while (char == -1 || Char(char) != '\n') {
            if (char == -1)
                continue
            array.add(char.toByte())
            char = channel.socket().getInputStream().read()
        }
        val str = String(array.toByteArray())
        return frameSerializer.deserialize(str)
    }
}

