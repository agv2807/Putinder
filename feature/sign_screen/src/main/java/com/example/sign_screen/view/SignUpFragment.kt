package com.example.sign_screen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.data.query_preferences.StoreUserTokenAndId
import com.example.model.user.UserInfo
import com.example.ui.R
import com.example.sign_screen.view_model.SignViewModel
import javax.inject.Inject

class SignUpFragment @Inject constructor() : Fragment() {

    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var signButton: Button
    private lateinit var loader: ProgressBar
    private lateinit var containerLayout: RelativeLayout

    private var photoId = ""

    private val signViewModel: SignViewModel by lazy {
        ViewModelProvider(this)[SignViewModel::class.java]
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
        loader = view.findViewById(R.id.loader)
        containerLayout = view.findViewById(R.id.container)

        return view
    }

    override fun onStart() {
        super.onStart()

        signButton.setOnClickListener {
            if (loginEditText.text.isEmpty() || passwordEditText.text.isEmpty()
                || nameEditText.text.isEmpty() || confirmPasswordEditText.text.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else if (passwordEditText.text.toString() != confirmPasswordEditText.text.toString()){
                Toast.makeText(requireContext(), "Пароли не совпадают", Toast.LENGTH_SHORT).show()
            } else {
                onLoadResume()
                val userInfo = UserInfo(
                    loginEditText.text.toString(),
                    passwordEditText.text.toString(),
                    nameEditText.text.toString(),
                    photoId
                )
                signViewModel.updateUserInfo(userInfo)
            }
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
                    //val intent = ContentActivity.newIntent(requireContext())
                    StoreUserTokenAndId.setStoredTokenAndId(requireContext(), it.token, it.user.id)
                    //startActivity(intent)
                    requireActivity().finish()
                }
            }
        )
    }

    private fun onLoadResume() {
        loader.visibility = View.VISIBLE
        containerLayout.visibility = View.GONE
    }

    private fun onLoadFinish() {
        loader.visibility = View.GONE
        containerLayout.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance() = SignUpFragment()
    }
}