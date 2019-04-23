package com.example.socketiochat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socketiochat.App.Companion.chatManager
import io.socket.client.IO
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ChatListener {
    private lateinit var adapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        chatManager.setListener(this)
        adapter = ChatAdapter()

        if (savedInstanceState != null) {
            adapter.messages = savedInstanceState.getParcelableArrayList(MESSAGES_KEY)
            adapter.notifyDataSetChanged()
        }

        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter
        initializeViewListeners()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(MESSAGES_KEY, adapter.messages)
        super.onSaveInstanceState(outState)
    }

    private fun initializeViewListeners() {
        sendBtn.setOnClickListener {
            val user = userNameEt.text.toString()
            val message = inputEt.text.toString()
            chatManager.sendMessage(user, message)
            inputEt.text.clear()
        }
        inputEt.doAfterTextChanged {
            sendBtn.isEnabled = it?.length!! > 0 && userNameEt.text.isNotEmpty()
        }

        userNameEt.doAfterTextChanged {
            sendBtn.isEnabled = it?.length!! > 0 && inputEt.text.isNotEmpty()
        }
    }

    override fun onDestroy() {
        chatManager.setListener(null)
        super.onDestroy()
    }

    override fun onNewMessage(message: Message) {
        runOnUiThread {
            adapter.addMessage(message)
            val manager = recycler.layoutManager as LinearLayoutManager
            manager.scrollToPositionWithOffset(2, 20)
        }
    }

    companion object {
        const val MESSAGES_KEY = "MESSAGES_KEY"
    }
}
