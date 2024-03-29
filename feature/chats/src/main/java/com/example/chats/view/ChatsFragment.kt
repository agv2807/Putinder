package com.example.chats.view

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.ui.R
import com.example.model.chats.Chat
import com.example.chats.view_model.ChatsViewModel
import com.example.data.query_preferences.StoreUserTokenAndId
import com.factor.bouncy.BouncyRecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class ChatsFragment @Inject constructor() : Fragment() {

    interface Callbacks {
        fun onNewChatPressed()
    }

    private lateinit var searchEditText: EditText
    private lateinit var chatsRecyclerView: BouncyRecyclerView
    private lateinit var newChatButton: FloatingActionButton
    private lateinit var loader: ProgressBar

    private var adapter: ChatsAdapter? = ChatsAdapter(emptyList())

    private var token = ""
    private var id = ""

    private val chatsViewModel: ChatsViewModel by lazy {
        ViewModelProvider(this)[ChatsViewModel::class.java]
    }

    private var callbacks: Callbacks? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chats, container, false)

        searchEditText = view.findViewById(R.id.search_edit_text)
        chatsRecyclerView = view.findViewById(R.id.chat_recycler_view)
        newChatButton = view.findViewById(R.id.new_chat_button)
        loader = view.findViewById(R.id.loader)

        chatsRecyclerView.layoutManager = LinearLayoutManager(context)
        chatsRecyclerView.adapter = adapter

        token = StoreUserTokenAndId.getStoredToken(requireContext())
        id = StoreUserTokenAndId.getStoredId(requireContext())

        chatsViewModel.loadChatsList(token)

        chatsViewModel.initWebSocket(token)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chatsViewModel.chatsLiveData.observe(
            viewLifecycleOwner,
            Observer {
                adapter = ChatsAdapter(it)
                chatsRecyclerView.adapter = adapter
                loader.visibility = View.GONE
            }
        )
    }

    override fun onStart() {
        super.onStart()
        newChatButton.setOnClickListener {
            callbacks?.onNewChatPressed()
        }
        chatsViewModel.loadChatsList(token)
    }

    companion object {
        fun newInstance() = ChatsFragment()
    }

    private inner class ChatsHolder(view: View) : RecyclerView.ViewHolder(view), OnClickListener {
        private val userPhotoImageView: ImageView = itemView.findViewById(R.id.user_photo_message)
        private val nameChatTextView: TextView = itemView.findViewById(R.id.chat_name_text_view)
        private val lastMessageTextView: TextView = itemView.findViewById(R.id.last_message_text_view)
        private val dateChatTextView: TextView = itemView.findViewById(R.id.date_chat_text_view)

        private var chatId = ""
        private var userImage = ""
        private var userName = ""

        init {
            itemView.setOnClickListener(this)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(chat: Chat) {
            chatId = chat.id

            if (chat.lastMessage != null) {
                lastMessageTextView.text = chat.lastMessage.message
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ")
                val localDateTime = LocalDateTime.parse(chat.lastMessage.date, formatter)
                    .format(DateTimeFormatter.ofPattern("HH:mm"))
                dateChatTextView.text = localDateTime.toString()
            } else {
                lastMessageTextView.text = ""
                dateChatTextView.text = ""
            }
            chat.users.forEach {
                if (it.id != id) {
                    nameChatTextView.text = it.name
                    userImage = it.image
                    userName = it.name
                }
            }

           chatsViewModel.loadImage(userImage) {
               updatePhoto(it)
            }
        }

        private fun updatePhoto(uri: Uri?) {
            Glide
                .with(this@ChatsFragment)
                .load(uri)
                .centerCrop()
                .placeholder(R.color.gray)
                .into(userPhotoImageView)
        }

        override fun onClick(p0: View?) {
//            val intent = ChatActivity.newIntent(requireContext())
//            intent.putExtra("CHAT_ID", chatId)
//            intent.putExtra("USER_IMAGE", userImage)
//            intent.putExtra("USER_NAME", userName)
//            startActivity(intent)
        }
    }

    private inner class ChatsAdapter(var chats: List<Chat>) : RecyclerView.Adapter<ChatsHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatsHolder {
            val view = layoutInflater.inflate(R.layout.chats_item, parent, false)
            return ChatsHolder(view)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun onBindViewHolder(holder: ChatsHolder, position: Int) {
            val chat = chats[position]
            holder.bind(chat)
        }

        override fun getItemCount() = chats.size
    }

}