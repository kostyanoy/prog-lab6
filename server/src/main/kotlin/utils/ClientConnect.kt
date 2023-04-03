class ClientConnect(socket: Socket) {
    try {
        val gis = socket.getInputStream()
        val gos = socket.getOutputStream()
        val ois = ObjectInputStream(gis)
        val oos = ObjectOutputStream(gos)
        while (true) {
            val request = ois.readObject()
            val response = ClientRequest(request)
            oos.writeObject(response)
            oos.flush()
        }
    } catch (e: SocketException) {
        println("Клиент отключился: ${socket.inetAddress.hostAddress}")
    } catch (e: Exception) {
        println("Error occurred while handling client request: ${e.message}")
    } finally {
        socket.close()
    }
}
