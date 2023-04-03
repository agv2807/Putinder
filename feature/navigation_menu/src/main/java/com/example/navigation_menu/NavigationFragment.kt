package com.example.navigation_menu

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.ui.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.inject.Inject

class NavigationFragment @Inject constructor() : Fragment() {

    interface Callbacks {
        fun onSwipesPressed()
        fun onChatsPressed()
        fun onProfilePressed()
    }

    private lateinit var bottomMenu: BottomNavigationView

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
        val view = inflater.inflate(R.layout.fragment_content, container, false)

        bottomMenu = view.findViewById(R.id.bottom_menu)
        bottomMenu.selectedItemId = R.id.profile_item
        bottomMenu.setOnItemSelectedListener {
            if (it.itemId != bottomMenu.selectedItemId) {
                when(it.itemId) {
                    R.id.swipes_item -> callbacks?.onSwipesPressed()
                    R.id.profile_item -> callbacks?.onProfilePressed()
                    R.id.chats_item -> callbacks?.onChatsPressed()
                }
            }
            true
        }

        return view
    }

    companion object {
        fun newInstance() = NavigationFragment()
    }
}