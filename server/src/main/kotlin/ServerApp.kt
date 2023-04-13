import data.MusicBand
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import serialize.FrameSerializer
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.SelectionKey
import java.nio.channels.Selector
import java.nio.channels.ServerSocketChannel
import java.nio.channels.SocketChannel
import org.apache.log4j.Logger
import utils.*

/**

The ServerApp class represents the server application that listens to incoming client requests,executes them and sends back the response.

@property [commandManager] The CommandManager instance used to execute the received commands.
@property [saver] The FileSaver instance used to save and load the collection.
@property [storage] The StorageM anager instance used to manage the collection.
@property [running] A boolean value indicating whether the server is running or not.
@property [selector] The Selector instance used for selecting incoming channels and operations.
@property [serverChannel] The ServerSocketChannel instance used to listen for incoming requests.
 */
class ServerApp(private val port: Int) : KoinComponent {
    private val commandManager: CommandManager by inject()
    private val saver: Saver<LinkedHashMap<Int, MusicBand>> by inject()
    private val storage: Storage<LinkedHashMap<Int, MusicBand>, Int, MusicBand> by inject()
    private val serializer = FrameSerializer()
    private val logger = Logger.getLogger(ServerApp::class.java)
    private var running = true
    private var selector: Selector = Selector.open()
    private lateinit var serverChannel: ServerSocketChannel
    /**
    Starts the server and listens for incoming client requests.
     */
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
    /**
    Accepts a new incoming connection and registers it with the selector.
     */
    private fun acceptConnection(key: SelectionKey, selector: Selector) {
        val serverSocketChannel = key.channel() as ServerSocketChannel
        val socketChannel = serverSocketChannel.accept()
        socketChannel.configureBlocking(false)
        socketChannel.register(selector, SelectionKey.OP_READ)
    }
    /**
    Reads the incoming request from the client, executes it and sends back the response.
     */
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
    /**
    Processes a client request and returns a response frame.
     */
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
    /**
    Stops the server
     */
    fun stop() {
        running = false
        selector.wakeup()
    }
    fun saveCollection() {
        val saver: Saver<LinkedHashMap<Int, MusicBand>> by inject()
        saver.save(storage.getCollection { true })
        logger.info("Коллекция сохранена")
    }
    fun loadCollection() {
        saver.load().forEach { storage.insert(it.key, it.value) }
        logger.info("Коллекция загружена")
    }
}