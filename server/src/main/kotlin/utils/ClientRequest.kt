import java.io.Serializable

class ClientRequest(private val frame: Frame) : Serializable {
    // здесь будет обработка запросов через лямбды, например:
    fun process(): String {
        return when (frame.type) {
        }
    }
}