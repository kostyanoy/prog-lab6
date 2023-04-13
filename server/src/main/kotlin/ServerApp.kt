import data.MusicBand
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import serialize.FrameSerializer
import utils.CommandManager
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import org.apache.log4j.Logger
import utils.FileSaver
import utils.Saver
import utils.StorageManager

class ServerApp(private val port: Int) : KoinComponent {
    private val commandManager: CommandManager by inject()
    private val saver: FileSaver by inject()
    private val storage: StorageManager by inject()
    private val serializer = FrameSerializer()
    private val logger = Logger.getLogger(ServerApp::class.java)
    private var running = true
    private var selector: Selector = Selector.open()
    private lateinit var serverChannel: ServerSocketChannel
    fun start() {
        logger.info("Сервер запускается на порту: $port")
        val serverChannel = ServerSocketChannel.open()
        serverChannel.bind(InetSocketAddress(port))
        serverChannel.configureBlocking(false)
        serverChannel.register(selector, SelectionKey.OP_ACCEPT)

        while (running) {
            selector.select()
            val selectedKeys = selector.selectedKeys().iterator()

            while (selectedKeys.hasNext()) {
                val key = selectedKeys.next()
                selectedKeys.remove()

                if (!key.isValid) {
                    continue
                }

                if (key.isAcceptable) {
                    acceptConnection(key, selector)
                } else if (key.isReadable) {
                    readRequest(key, selector)
                }
            }
        }
        serverChannel.close()
        selector.close()
        logger.info("Сервер закрыт")
    }

    private fun acceptConnection(key: SelectionKey, selector: Selector) {
        val serverSocketChannel = key.channel() as ServerSocketChannel
        val socketChannel = serverSocketChannel.accept()
        socketChannel.configureBlocking(false)
        socketChannel.register(selector, SelectionKey.OP_READ)
    }

    private fun readRequest(key: SelectionKey, selector: Selector) {
        val socketChannel = key.channel() as SocketChannel
        val buffer = ByteBuffer.allocate(1024)

        try {
            socketChannel.read(buffer)
            buffer.flip()
            val len = buffer.limit() - buffer.position()
            val str = ByteArray(len)
            buffer.get(str, buffer.position(), len)
            buffer.flip()
            val request = serializer.deserialize(str.decodeToString())
            val response = clientRequest(request)
            buffer.clear()
            buffer.put(serializer.serialize(response).toByteArray())
            buffer.put('\n'.code.toByte())
            buffer.flip()
            socketChannel.write(buffer)
        } catch (e: Exception) {
            logger.error(e.message)
            key.cancel()
            socketChannel.close()
        }
    }

    private fun clientRequest(request: Frame): Frame {
        return when (request.type) {
            FrameType.COMMAND_REQUEST -> {
                val response = Frame(FrameType.COMMAND_RESPONSE)
                val commandName = request.body["name"] as String
                val args = request.body["args"] as Array<Any>
                val command = commandManager.getCommand(commandName)
                val result = command.execute(args)
                response.setValue("data", result)
                response
            }

            FrameType.LIST_OF_COMMANDS_REQUEST -> {
                val response = Frame(FrameType.LIST_OF_COMMANDS_RESPONSE)
                val commands = commandManager.commands.mapValues { it.value.getArgumentTypes() }.toMap()
                response.setValue("commands", commands)
                response
            }

            else -> {
                val response = Frame(FrameType.COMMAND_RESPONSE)
                response.setValue("data", "Неверный тип запроса")
                response
            }
        }
    }

    fun stop() {
        running = false
        selector.wakeup()
    }

    fun saveCollection() {
        val saver: Saver<LinkedHashMap<Int, MusicBand>> by inject()
        saver.save(storage.getCollection { true })
    }

    fun loadCollection() {
        saver.load().forEach { storage.insert(it.key, it.value) }
    }
}