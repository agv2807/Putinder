package com.example.putinder.activities

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.chats.view.ChatsFragment
import com.example.create_chat.view.CreateChatFragment
import com.example.profile.view.ProfileFragment
import com.example.putinder.di.DaggerAppComponent
import com.example.swipes.view.PlaceCardFragment
import com.example.swipes.view.SwipesFragment
import com.example.ui.R
import com.example.yandex_map.MapFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.transition.MaterialContainerTransform
import javax.inject.Inject

class ContentFragment : Fragment(),
    SwipesFragment.Callbacks,
    ChatsFragment.Callbacks,
    CreateChatFragment.Callbacks,
    MapFragment.Callbacks{

    @Inject
    lateinit var chatsFragment: ChatsFragment
    @Inject
    lateinit var swipesFragment: SwipesFragment
    @Inject
    lateinit var profileFragment: ProfileFragment
    @Inject
    lateinit var createChatFragment: CreateChatFragment
    @Inject
    lateinit var placeCardFragment: PlaceCardFragment
    @Inject
    lateinit var mapFragment: MapFragment

    private var currentFragment: Fragment? = null

    private lateinit var bottomMenu: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DaggerAppComponent.create().inject(this)
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
            when(it.itemId) {
                R.id.swipes_item -> if (it.itemId != bottomMenu.selectedItemId) routeToSwipes()
                R.id.profile_item -> if (it.itemId != bottomMenu.selectedItemId) routeToProfile()
                R.id.chats_item -> if (it.itemId != bottomMenu.selectedItemId) routeToChats()
            }
            true
        }

        currentFragment = childFragmentManager.findFragmentById(R.id.container)

        if (currentFragment == null) {
            childFragmentManager.beginTransaction()
                .add(R.id.container, profileFragment)
                .add(R.id.container, swipesFragment)
                .hide(swipesFragment)
                .add(R.id.container, chatsFragment)
                .hide(chatsFragment)
                .commit()
        } else {
            childFragmentManager.beginTransaction()
                .add(R.id.container, currentFragment!!)
                .commit()
        }
        currentFragment = profileFragment

        return view
    }

    override fun onStart() {
        super.onStart()
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                when(currentFragment) {
                    createChatFragment -> deleteCreateChatFragment()
                    mapFragment -> deleteMapFragment()
                    placeCardFragment -> deletePlaceCardFragment()
                    else -> requireActivity().onBackPressed()
                }
            }

        })
    }

    private fun routeToSwipes() {
        if (currentFragment != swipesFragment) {
            childFragmentManager.beginTransaction()
                .hide(currentFragment!!)
                .show(swipesFragment)
                .commit()
            currentFragment = swipesFragment
        }

    }

    private fun routeToProfile() {
        if (currentFragment != profileFragment) {
            childFragmentManager.beginTransaction()
                .hide(currentFragment!!)
                .show(profileFragment)
                .commit()
            currentFragment = profileFragment
        }
    }

    private fun routeToChats() {
        if (currentFragment != chatsFragment) {
            childFragmentManager.beginTransaction()
                .hide(currentFragment!!)
                .show(chatsFragment)
                .commit()
            currentFragment = chatsFragment
        }
    }

    private fun routeToCreateChat() {
        createChatFragment = CreateChatFragment.newInstance()
        bottomMenu.visibility = View.GONE
        childFragmentManager.beginTransaction()
            .add(R.id.container, createChatFragment)
            .hide(currentFragment!!)
            .commit()
        currentFragment = createChatFragment
    }

    private fun routeToPlaceCardFragment(view: View) {
        placeCardFragment = PlaceCardFragment.newInstance()
        placeCardFragment.sharedElementEnterTransition = MaterialContainerTransform()
        bottomMenu.visibility = View.GONE
        childFragmentManager.beginTransaction()
            .addSharedElement(view, "shared_element_container")
            .add(R.id.container, placeCardFragment)
            .hide(currentFragment!!)
            .commit()
        currentFragment = placeCardFragment
    }

    private fun deletePlaceCardFragment() {
        bottomMenu.visibility = View.VISIBLE
        currentFragment = swipesFragment
        childFragmentManager.beginTransaction()
            .remove(placeCardFragment)
            .show(currentFragment!!)
            .commit()
    }

    private fun deleteMapFragment() {
        bottomMenu.visibility = View.VISIBLE
        currentFragment = swipesFragment
        childFragmentManager.beginTransaction()
            .remove(mapFragment)
            .show(currentFragment!!)
            .commit()
    }

    override fun onOpenCardButtonPressed(view: View) {
        routeToPlaceCardFragment(view)
    }

    override fun onNewChatPressed() {
        routeToCreateChat()
    }

    override fun onNewChatCreated() {
        deleteCreateChatFragment()
    }

    override fun onMapClosed() {
        deleteMapFragment()
    }

    private fun deleteCreateChatFragment() {
        bottomMenu.visibility = View.VISIBLE
        currentFragment = chatsFragment
        childFragmentManager.beginTransaction()
            .remove(createChatFragment)
            .show(currentFragment!!)
            .commit()
    }

    override fun onFabPressed() {
        bottomMenu.visibility = View.GONE
        childFragmentManager.beginTransaction()
            .add(R.id.container, mapFragment)
            .hide(currentFragment!!)
            .addToBackStack("MapFragment")
            .commit()
        currentFragment = mapFragment
    }

    companion object {
        fun newInstance() = ContentFragment()
    }
}