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
import utils.StorageManager

class ServerApp(private val port: Int) : KoinComponent {
    private val commandManager: CommandManager by inject()
    private val saver: FileSaver by inject()
    private val storage: StorageManager by inject()
    private val serializer = FrameSerializer()
    private val logger = Logger.getLogger(ServerApp::class.java)
    private var running = true
    private lateinit var selector: Selector
    private lateinit var serverChannel: ServerSocketChannel
    init {
        selector = Selector.open()
    }
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
            println(buffer)
            val len = buffer.limit() - buffer.position()
            val str = ByteArray(len)
            println(buffer.get(str, buffer.position(), len))
            buffer.flip()
            println(str.decodeToString())
            println(buffer)
            val request = serializer.deserialize(str.decodeToString())
            logger.info(request)
            val response = clientRequest(request)
            println(response)
            buffer.clear()
            buffer.put(serializer.serialize(response).toByteArray())
            buffer.put('\n'.toByte())
            buffer.flip()
            socketChannel.write(buffer)
        } catch (e: Exception) {
            key.cancel()
            socketChannel.close()
        }
    }

    private fun clientRequest(request: Frame): Frame {
        val response = Frame(FrameType.COMMAND_RESPONSE)

        when (request.type) {
            FrameType.COMMAND_REQUEST -> {
                val commandName = request.body["name"] as String
                val args = request.body["args"] as List<Any>
                val command = commandManager.getCommand(commandName)
                val result = command.execute(args.associateBy({ it.toString() }, { it }))
                response.setValue("data", result)
            }

            FrameType.LIST_OF_COMMANDS_REQUEST -> {
                val commands = commandManager.commands.mapValues { it.value.getArgumentTypes() }.toMap()
                response.setValue("commands", commands)
            }

            else -> {
                response.setValue("data", "Неверный тип запроса")
            }
        }
        return response
    }
    fun stop() {
        running = false
        selector.wakeup()
    }
    fun saveCollection() {
        saver.save(storage.getCollection { true })
    }

    fun loadCollection() {
        val saver: FileSaver by inject()
        storage.clear()
        saver.load().forEach { storage.insert(it.key, it.value) }
    }
}

