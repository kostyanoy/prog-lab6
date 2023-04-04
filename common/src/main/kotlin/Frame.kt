class Frame (val type: FrameType) {
    val body = mutableMapOf<String, Any>()

    fun setValue(key: String, value: Any){
        body[key] = value
    }
}