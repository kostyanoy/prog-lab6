import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

sealed class CommandResult {
    @Serializable
    data class Success(val commandName: String, val message: String? = null) : CommandResult()
    @Serializable
    data class Failure(val commandName: String, val throwable: @Contextual Throwable) : CommandResult()
}
