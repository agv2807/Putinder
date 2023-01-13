package com.example.putinder.content_screen.profile_screen.view

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.putinder.QueryPreferences.QueryPreferences
import com.example.putinder.R
import com.example.putinder.content_screen.profile_screen.models.ProfileResponse
import com.example.putinder.content_screen.profile_screen.view_model.ProfileViewModel

private const val REQUEST_CODE = 3

class ProfileFragment : Fragment() {

    private lateinit var profilePhoto: ImageView
    private lateinit var userNameTextView: TextView

    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profilePhoto = view.findViewById(R.id.photo_profile_image)
        userNameTextView = view.findViewById(R.id.user_name_text_view)

        val token = QueryPreferences.getStoredToken(requireContext())
        profileViewModel.loadUserInfo(token)

        return view
    }

    override fun onStart() {
        super.onStart()
        profilePhoto.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        profileViewModel.userInfoLiveData.observe(
            viewLifecycleOwner,
            Observer {
                updateUI(it)
            }
        )

        profileViewModel.userPhotoLiveData.observe(
            viewLifecycleOwner,
            Observer {
                updateUserPhoto(it)
            }
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            if (data?.data != null) {
                profileViewModel.uploadPhoto(data.data!!)
            }
        }
    }

    private fun updateUserPhoto(uri: Uri) {
        Glide
            .with(this)
            .load(uri)
            .centerCrop()
            .placeholder(R.color.gray)
            .into(profilePhoto)
    }

    private fun updateUI(userResponse: ProfileResponse) {
        userNameTextView.text = userResponse.name
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }

}