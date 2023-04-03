package com.example.putinder.sign_screen.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.putinder.query_preferences.QueryPreferences
import com.example.putinder.R
import com.example.putinder.content_screen.activity.ContentActivity
import com.example.putinder.sign_screen.models.UserInfoAuth
import com.example.putinder.sign_screen.view_model.SignViewModel

class SignInFragment : Fragment() {

    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signButton: Button
    private lateinit var loader: ProgressBar
    private lateinit var forgotPassword: TextView
    private lateinit var containerLayout: RelativeLayout

    private val signViewModel: SignViewModel by lazy {
        ViewModelProvider(this)[SignViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        loginEditText = view.findViewById(R.id.login_edit_text)
        passwordEditText = view.findViewById(R.id.password_edit_text)
        signButton = view.findViewById(R.id.sign_button)
        loader = view.findViewById(R.id.loader)
        forgotPassword = view.findViewById(R.id.forgot_password_text_view)
        containerLayout = view.findViewById(R.id.container)

        return view
    }

    override fun onStart() {
        super.onStart()

        signButton.setOnClickListener {
            if (loginEditText.text.isEmpty() || passwordEditText.text.isEmpty()) {
                Toast.makeText(requireContext(), "Заполните все поля", Toast.LENGTH_SHORT).show()
            } else {
                onLoadResume()
                val userInfoAuth = UserInfoAuth(loginEditText.text.toString(),
                    passwordEditText.text.toString())
                signViewModel.updateAuthUserInfo(userInfoAuth)
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
                    QueryPreferences.setStoredQuery(requireContext(), it.token, it.user.id)
                    val intent = ContentActivity.newIntent(requireContext())
                    startActivity(intent)
                    requireActivity().overridePendingTransition(R.transition.fade_in, R.transition.fade_out)
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
        fun newInstance() = SignInFragment()
    }
}