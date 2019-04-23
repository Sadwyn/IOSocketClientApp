package com.example.socketiochat

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.message_item.view.*

class ChatAdapter : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    var messages: ArrayList<Message>? = null

    fun addMessage(message: Message) {
        if (messages == null) {
            messages = ArrayList()
        }
        messages!!.add(message)
        notifyItemInserted(messages!!.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        return ChatViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_item, parent, false))
    }

    override fun getItemCount(): Int {
        messages?.let {
            return it.size
        }
        return 0
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        messages?.let {
            bind(holder, it, position)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun bind(
        holder: ChatViewHolder, it: ArrayList<Message>, position: Int
    ): Unit? {
        holder.messageTv?.text = "${it[position].userName} : ${it[position].userMessage}"
        return if (it[position].isCurrentUser) {
            setViewsGravity(holder, true)
        } else {
            setViewsGravity(holder, false)
        }
    }

    private fun setViewsGravity(
        holder: ChatViewHolder,
        isCurrentUser: Boolean
    ) {
        when (isCurrentUser) {
            true -> {
                holder.group?.gravity = Gravity.END
                var params = holder.avatarImg?.layoutParams as LinearLayoutCompat.LayoutParams
                params.gravity = Gravity.END
                params = holder.messageTv?.layoutParams as LinearLayoutCompat.LayoutParams
                params.gravity = Gravity.END
                holder.avatarImg.setImageResource(R.drawable.user)
            }
            else -> {
                holder.group?.gravity = Gravity.START
                var params = holder.avatarImg?.layoutParams as LinearLayoutCompat.LayoutParams
                params.gravity = Gravity.START
                params = holder.messageTv?.layoutParams as LinearLayoutCompat.LayoutParams
                params.gravity = Gravity.START
                holder.avatarImg.setImageResource(R.drawable.bot)
            }
        }

    }

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val messageTv: TextView? = itemView.messageTv
        val avatarImg: ImageView? = itemView.avatarImg
        val group: LinearLayoutCompat? = itemView.root
    }
}