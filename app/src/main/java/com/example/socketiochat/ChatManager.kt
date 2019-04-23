package com.example.socketiochat

import io.socket.client.Socket
import io.socket.client.Socket.EVENT_CONNECT
import io.socket.client.Socket.EVENT_CONNECT_ERROR
import org.json.JSONObject


class ChatManager(
    private val socket: Socket
) {

    private var currentUser: String = "Guest"
    private var chatListener: ChatListener? = null

    fun setListener(chatListener: ChatListener?) {
        this.chatListener = chatListener
    }

    fun subscribe() {
        socket
            .on(EVENT_CONNECT) { args ->
                chatListener?.onNewMessage(Message("Bot:", "Connected", false))
            }
            .on(EVENT_MESSAGE) { result ->
                val userMessage = result.first() as JSONObject
                val message = userMessage[MESSAGE_JSON_KEY] as String
                val user = userMessage[USER_JSON_KEY] as String
                if (currentUser != user) {
                    chatListener?.onNewMessage(Message(user, message, false))
                }
            }
            .on(EVENT_CONNECT_ERROR) {
                val exception = it[0] as Exception
                chatListener?.onNewMessage(Message("Chat Error", exception.message.toString(), false))
            }
        socket.connect()
    }

    fun unsubscribe() {
        socket.disconnect()
    }

    fun sendMessage(user: String, message: String) {
        currentUser = user
        val messageObject = JSONObject()
        messageObject.put(MESSAGE_JSON_KEY, message)
        messageObject.put(USER_JSON_KEY, currentUser)
        socket.emit(EVENT_MESSAGE, messageObject)
        chatListener?.onNewMessage(Message(user, message, true))
    }

    companion object {
        const val EVENT_MESSAGE = "message"
        const val USER_JSON_KEY = "user"
        const val MESSAGE_JSON_KEY = "message"
    }
}