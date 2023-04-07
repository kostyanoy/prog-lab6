import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
class Frame (val type: FrameType) {

    val body = mutableMapOf<String, @Contextual Any>()

    fun setValue(key: String, value: Any){
        body[key] = value
    }
}
