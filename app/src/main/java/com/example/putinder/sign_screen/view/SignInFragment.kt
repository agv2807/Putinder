package com.example.putinder.sign_screen.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.putinder.QueryPreferences.QueryPreferences
import com.example.putinder.R
import com.example.putinder.content_screen.activity.ContentActivity
import com.example.putinder.sign_screen.models.UserInfoAuth
import com.example.putinder.sign_screen.view_model.SignViewModel
import org.w3c.dom.Text

class SignInFragment : Fragment() {

    interface Callbacks {
        fun onRegPressed()
    }

    private lateinit var loginEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signButton: Button
    private lateinit var regTextView: TextView
    private lateinit var loader: ProgressBar

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

        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        loginEditText = view.findViewById(R.id.login_edit_text)
        passwordEditText = view.findViewById(R.id.password_edit_text)
        signButton = view.findViewById(R.id.sign_button)
        regTextView = view.findViewById(R.id.reg_text_view)
        loader = view.findViewById(R.id.loader)

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

        regTextView.setOnClickListener {
            callbacks?.onRegPressed()
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
                }
            }
        )
    }

    private fun onLoadResume() {
        loader.visibility = View.VISIBLE
        loginEditText.visibility = View.GONE
        passwordEditText.visibility = View.GONE
        signButton.visibility = View.GONE
        regTextView.visibility = View.GONE
    }

    private fun onLoadFinish() {
        loader.visibility = View.GONE
        loginEditText.visibility = View.VISIBLE
        passwordEditText.visibility = View.VISIBLE
        signButton.visibility = View.VISIBLE
        regTextView.visibility = View.VISIBLE
    }

    companion object {
        fun newInstance() = SignInFragment()
    }
}