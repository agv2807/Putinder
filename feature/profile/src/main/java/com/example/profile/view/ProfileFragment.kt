package com.example.profile.view

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.elyeproj.loaderviewlibrary.LoaderImageView
import com.elyeproj.loaderviewlibrary.LoaderTextView
import com.example.data.query_preferences.StoreUserTokenAndId
import com.example.model.user.ProfileResponse
import com.example.ui.R
import com.example.profile.view_model.ProfileViewModel
import javax.inject.Inject

class ProfileFragment @Inject constructor(): Fragment() {

    private lateinit var profilePhoto: LoaderImageView
    private lateinit var userNameTextView: LoaderTextView

    private val profileViewModel: ProfileViewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    private lateinit var getContent: ActivityResultLauncher<String>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        getContent = registerForActivityResult(ActivityResultContracts.GetContent()) {
                uri: Uri? ->
            if (uri != null) {
                profileViewModel.uploadPhoto(uri)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        profilePhoto = view.findViewById(R.id.photo_profile_image)
        userNameTextView = view.findViewById(R.id.user_name_text_view)

        val token = StoreUserTokenAndId.getStoredToken(requireContext())
        profileViewModel.loadUserInfo(token)

        return view
    }

    override fun onStart() {
        super.onStart()

        profilePhoto.setOnClickListener {
            getContent.launch("image/*")
            profilePhoto.resetLoader()

        }

        userNameTextView.setOnClickListener {
            showChangeNameDialog()
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

    private fun updateUserPhoto(uri: Uri) {
        Glide
            .with(this)
            .load(uri)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(profilePhoto)
    }

    private fun updateUI(userResponse: ProfileResponse) {
        userNameTextView.text = userResponse.name
    }

    private fun showChangeNameDialog() {
        val dialog = EditTextDialog.newInstance(userNameTextView.text.toString())
        dialog.onOk = {
            val newName = dialog.editText.text.toString()
            profileViewModel.updateUserName(newName)
            userNameTextView.resetLoader()
        }
        dialog.show(this@ProfileFragment.childFragmentManager, "editDescription")
    }

    companion object {
        fun newInstance() = ProfileFragment()
    }

}