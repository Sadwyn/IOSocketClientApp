package com.example.socketiochat

import android.app.Application
import android.content.ComponentCallbacks
import android.os.Handler
import io.socket.client.IO

class App : Application() {
    val options = IO.Options()
    val url = "http://185.13.90.140:8081"
    val socket = IO.socket(url, options)

    override fun onCreate() {
        super.onCreate()
        chatManager = ChatManager(socket)
        Handler().postDelayed({
            chatManager.subscribe()
        }, 2000)
    }


    companion object {
        lateinit var chatManager: ChatManager
    }
}