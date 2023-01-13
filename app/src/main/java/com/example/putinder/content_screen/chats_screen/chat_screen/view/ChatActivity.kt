package com.example.putinder.content_screen.chats_screen.chat_screen.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.putinder.QueryPreferences.QueryPreferences
import com.example.putinder.R
import com.example.putinder.content_screen.chats_screen.chat_screen.view_model.ChatViewModel
import com.example.putinder.content_screen.chats_screen.chat_screen.models.MessageResponse
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ChatActivity : AppCompatActivity() {

    private lateinit var userPhotoImageView: ImageView
    private lateinit var userNameTextView: TextView
    private lateinit var messagesListRecyclerView: RecyclerView
    private lateinit var messageEditText: EditText
    private lateinit var sendMessageButton: FloatingActionButton

    private var adapter = ChatAdapter(emptyList())

    private val chatViewModel: ChatViewModel by lazy {
        ViewModelProvider(this)[ChatViewModel::class.java]
    }

    private var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        userPhotoImageView = findViewById(R.id.user_photo_chat)
        userNameTextView = findViewById(R.id.chat_name_text_view)
        messagesListRecyclerView = findViewById(R.id.messages_list_recycler_view)
        messageEditText = findViewById(R.id.message_edit_text)
        sendMessageButton = findViewById(R.id.send_message_button)

        val token = QueryPreferences.getStoredToken(this)
        id = QueryPreferences.getStoredId(this)
        val chatId = intent.getStringExtra("CHAT_ID").toString()
        val userImage = intent.getStringExtra("USER_IMAGE").toString()
        val userName = intent.getStringExtra("USER_NAME").toString()

        userNameTextView.text = userName

        chatViewModel.initWebSocket(token)

        chatViewModel.loadUserImage(userImage)
        chatViewModel.loadMessages(token, chatId)

        messagesListRecyclerView.layoutManager = LinearLayoutManager(this)
        messagesListRecyclerView.adapter = adapter

        chatViewModel.messagesLiveData.observe(
            this,
            Observer {
                adapter = ChatAdapter(it)
                messagesListRecyclerView.adapter = adapter
                messagesListRecyclerView.scrollToPosition(it.size - 1)
            }
        )

        chatViewModel.userImageLiveData.observe(
            this,
            Observer {
                Glide
                    .with(this)
                    .load(it)
                    .centerCrop()
                    .placeholder(R.color.gray)
                    .into(userPhotoImageView)
            }
        )

        sendMessageButton.setOnClickListener {
            if (!messageEditText.text.isNullOrEmpty()) {
                chatViewModel.pushMessage(token, messageEditText.text.toString() ,chatId)
                messageEditText.text.clear()
            }
        }
    }

    private inner class MessageHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val messageTextView: TextView = view.findViewById(R.id.message_text_view)
        private val dateTextView: TextView = view.findViewById(R.id.date_text_view)

        fun bind(messageResponse: MessageResponse) {
            messageTextView.text = messageResponse.message
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
            val localDateTime = LocalDateTime.parse(messageResponse.date, formatter)
                .format(DateTimeFormatter.ofPattern("HH:mm"))
            dateTextView.text = localDateTime
        }
    }

    private inner class MyMessageHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val messageTextView: TextView = view.findViewById(R.id.message_text_view)
        private val dateTextView: TextView = view.findViewById(R.id.date_text_view)

        fun bind(messageResponse: MessageResponse) {
            messageTextView.text = messageResponse.message
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
            val localDateTime = LocalDateTime.parse(messageResponse.date, formatter)
                .format(DateTimeFormatter.ofPattern("HH:mm"))
            dateTextView.text = localDateTime
        }
    }

    private inner class ChatAdapter(var messages: List<MessageResponse>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == 0)  {
                MyMessageHolder(layoutInflater.inflate(R.layout.my_message_item, parent, false))
            } else {
                MessageHolder(layoutInflater.inflate(R.layout.message_item, parent, false))
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val message = messages[position]
            if (message.user.id == id) {
                val holderCustom = holder as MyMessageHolder
                holderCustom.bind(message)
            } else {
                val holderCustom = holder as MessageHolder
                holderCustom.bind(message)
            }
        }

        override fun getItemCount() = messages.size

        override fun getItemViewType(position: Int) = if (messages[position].user.id == id) 0 else 1

    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ChatActivity::class.java)
        }
    }

}