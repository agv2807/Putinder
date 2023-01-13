package com.example.putinder.sign_screen.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.putinder.QueryPreferences.QueryPreferences
import com.example.putinder.R
import com.example.putinder.content_screen.activity.ContentActivity
import com.example.putinder.sign_screen.models.UserInfo
import com.example.putinder.sign_screen.view_model.SignViewModel

private const val REQUEST_CODE = 3

class SignUpFragment : Fragment() {

    interface Callbacks {
        fun onAuthPressed()
    }

    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var signButton: Button
    private lateinit var authTextView: TextView
    private lateinit var addPhotoImageView: ImageView
    private lateinit var photoImageView: ImageView
    private lateinit var loader: ProgressBar

    private var photoId = ""

    private val signViewModel: SignViewModel by lazy {
        ViewModelProvider(this)[SignViewModel::class.java]
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

        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        loginEditText = view.findViewById(R.id.login_edit_text)
        passwordEditText = view.findViewById(R.id.password_edit_text)
        signButton = view.findViewById(R.id.sign_button)
        nameEditText = view.findViewById(R.id.name_edit_text)
        confirmPasswordEditText = view.findViewById(R.id.confirm_password_edit_text)
        authTextView = view.findViewById(R.id.auth_text_view)
        addPhotoImageView = view.findViewById(R.id.user_photo_image_view)
        photoImageView = view.findViewById(R.id.photo_image_view)
        loader = view.findViewById(R.id.loader)

        return view
    }

    override fun onStart() {
        super.onStart()

        signButton.setOnClickListener {
            onLoadResume()
            val userInfo = UserInfo(loginEditText.text.toString(),
                passwordEditText.text.toString(),
                nameEditText.text.toString(),
                photoId)
            signViewModel.updateUserInfo(userInfo)
        }

        authTextView.setOnClickListener {
            callbacks?.onAuthPressed()
        }

        addPhotoImageView.setOnClickListener {
           val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signViewModel.userInfoLiveData.observe(
            viewLifecycleOwner,
            Observer {
                onLoadFinish()
                if (it == null) {
                    Toast.makeText(requireContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                } else {
                    val intent = ContentActivity.newIntent(requireContext())
                    QueryPreferences.setStoredQuery(requireContext(), it.token, it.user.id)
                    startActivity(intent)
                }
            }
        )

        signViewModel.userPhotoLiveData.observe(
            viewLifecycleOwner,
            Observer {
                Glide
                    .with(this)
                    .load(it)
                    .centerCrop()
                    .placeholder(R.color.gray)
                    .into(photoImageView)
            }
        )

        signViewModel.photoIdLiveData.observe(
            viewLifecycleOwner,
            Observer {
                photoId = it
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            if (data?.data != null) {
                signViewModel.uploadPhoto(data.data!!)
            }
        }
    }

    private fun onLoadResume() {
        loader.visibility = View.VISIBLE
        loginEditText.visibility = View.GONE
        passwordEditText.visibility = View.GONE
        signButton.visibility = View.GONE
        nameEditText.visibility = View.GONE
        confirmPasswordEditText.visibility = View.GONE
        authTextView.visibility = View.GONE
        addPhotoImageView.visibility = View.GONE

    }

    private fun onLoadFinish() {
        loader.visibility = View.GONE
        loginEditText.visibility = View.VISIBLE
        passwordEditText.visibility = View.VISIBLE
        signButton.visibility = View.VISIBLE
        nameEditText.visibility = View.VISIBLE
        confirmPasswordEditText.visibility = View.VISIBLE
        authTextView.visibility = View.VISIBLE
        addPhotoImageView.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance() = SignUpFragment()
    }
}