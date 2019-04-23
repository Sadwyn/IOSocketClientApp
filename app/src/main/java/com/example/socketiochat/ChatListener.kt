package com.example.socketiochat

interface ChatListener{
    fun onNewMessage(message: Message)
}