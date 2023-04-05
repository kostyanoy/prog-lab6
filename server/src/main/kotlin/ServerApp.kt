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

class ServerApp(private val port: Int) : KoinComponent {
    private val commandManager: CommandManager by inject()
    private val serializer = FrameSerializer()

    fun start() {
        println("Сервер запускается на порту: $port")
        val selector = Selector.open()
        val serverChannel = ServerSocketChannel.open()
        serverChannel.bind(InetSocketAddress(port))
        serverChannel.register(selector, SelectionKey.OP_ACCEPT)
        serverChannel.configureBlocking(false)

        while (true) {
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
            val request = serializer.deserialize(buffer.array().decodeToString())
            val response = clientRequest(request)
            buffer.clear()
            buffer.put(serializer.serialize(response).toByteArray())
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
}

