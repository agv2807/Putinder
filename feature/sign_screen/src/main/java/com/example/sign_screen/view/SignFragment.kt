package com.example.sign_screen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.sign_screen.di.DaggerSignComponent
import com.example.ui.R
import javax.inject.Inject

class SignFragment : Fragment() {

    @Inject
    lateinit var signInFragment: SignInFragment
    @Inject
    lateinit var signUpFragment: SignUpFragment

    private var activeFragment: Fragment? = null

    private lateinit var signButton: Button
    private lateinit var signUpButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerSignComponent.create().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign, container, false)

        signButton = view.findViewById(R.id.sign_text_view)
        signUpButton = view.findViewById(R.id.sign_up_text_view)

        val currentFragment = childFragmentManager.findFragmentById(R.id.main_container)
        activeFragment = signInFragment

        if (currentFragment == null) {
            childFragmentManager.beginTransaction()
                .add(R.id.main_container, signInFragment)
                .add(R.id.main_container, signUpFragment)
                .hide(signUpFragment)
                .commit()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        signButton.setOnClickListener {
            signUpButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            signButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            childFragmentManager.beginTransaction()
                .hide(activeFragment!!)
                .show(signInFragment)
                .commit()
            activeFragment = signInFragment
        }

        signUpButton.setOnClickListener {
            signUpButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
            signButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.gray))
            childFragmentManager.beginTransaction()
                .hide(activeFragment!!)
                .show(signUpFragment)
                .commit()
            activeFragment = signUpFragment
        }
    }

    companion object {
        fun newInstance() = SignFragment()
    }

}