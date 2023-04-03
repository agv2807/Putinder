package com.example.create_chat.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.ui.R
import com.example.create_chat.view_model.CreateChatViewModel
import com.example.data.query_preferences.StoreUserTokenAndId
import com.example.model.user.ProfileResponse
import javax.inject.Inject

class CreateChatFragment @Inject constructor(): Fragment() {

    interface Callbacks {
        fun onNewChatCreated()
    }

    private lateinit var searchUserEditText: EditText
    private lateinit var usersListRecyclerView: RecyclerView
    private lateinit var loader: ProgressBar

    private val createChatViewModel: CreateChatViewModel by lazy {
        ViewModelProvider(this)[CreateChatViewModel::class.java]
    }

    private var adapter: UsersListAdapter? = UsersListAdapter(emptyList())

    private var token = ""

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
        val view = inflater.inflate(R.layout.fragment_create_chat, container, false)

        searchUserEditText = view.findViewById(R.id.search_user_edit_text)
        usersListRecyclerView = view.findViewById(R.id.users_list_recycler_view)
        loader = view.findViewById(R.id.loader)

        token = StoreUserTokenAndId.getStoredToken(requireContext())

        usersListRecyclerView.layoutManager = LinearLayoutManager(context)
        usersListRecyclerView.adapter = adapter

        createChatViewModel.getUsersList(token)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        createChatViewModel.usersListLiveData.observe(
            viewLifecycleOwner,
            Observer {
                adapter = UsersListAdapter(it)
                usersListRecyclerView.adapter = adapter
                loader.visibility = View.GONE
            }
        )
    }

    private inner class NewChatHolder(view: View) : RecyclerView.ViewHolder(view), OnClickListener {

        private val userImageView: ImageView = itemView.findViewById(R.id.user_photo_message)
        private val userNameTextView: TextView = itemView.findViewById(R.id.chat_name_text_view)
        private val lastMessage: TextView = itemView.findViewById(R.id.last_message_text_view)
        private val date: TextView = itemView.findViewById(R.id.date_chat_text_view)

        private var userId = ""
        private var userImage = ""
        private var userName = ""

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(profileResponse: ProfileResponse) {

            userNameTextView.text = profileResponse.name
            lastMessage.text = ""
            date.text = ""

            userId = profileResponse.id
            userName = profileResponse.name
            userImage = profileResponse.image

            createChatViewModel.loadImage(profileResponse.image) {
                if (isAdded) {
                    Glide
                        .with(this@CreateChatFragment)
                        .load(it)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .centerCrop()
                        .placeholder(R.color.gray)
                        .into(userImageView)
                }
            }

        }

        override fun onClick(p0: View?) {
            createChatViewModel.createNewChat(token, userId) {
                callbacks?.onNewChatCreated()
//                val intent = ChatActivity.newIntent(requireContext())
//                intent.putExtra("CHAT_ID", it?.id)
//                intent.putExtra("USER_IMAGE", userImage)
//                intent.putExtra("USER_NAME", userName)
//                startActivity(intent)
            }
        }
    }

    private inner class UsersListAdapter(var users: List<ProfileResponse>) : RecyclerView.Adapter<NewChatHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewChatHolder {
            val view = layoutInflater.inflate(R.layout.chats_item, parent, false)
            return NewChatHolder(view)
        }

        override fun onBindViewHolder(holder: NewChatHolder, position: Int) {
            val user = users[position]
            holder.bind(user)
        }

        override fun getItemCount() = users.size
    }

    companion object {
        fun newInstance() = CreateChatFragment()
    }
}