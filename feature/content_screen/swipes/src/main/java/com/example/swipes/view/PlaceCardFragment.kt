package com.example.swipes.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.model.place.PlaceResponse
import com.example.ui.R
import com.example.swipes.view_model.SwipesViewModel
import com.google.android.material.transition.MaterialContainerTransform
import me.relex.circleindicator.CircleIndicator3

class PlaceCardFragment : Fragment() {

    private lateinit var placeCardRecyclerView: RecyclerView

    private var swipesViewModel: SwipesViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_place_card, container, false)

        placeCardRecyclerView = view.findViewById(R.id.place_info_recycler_view)
        placeCardRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipesViewModel = ViewModelProvider(requireActivity())[SwipesViewModel::class.java]
        val placeInfo = swipesViewModel?.place

        val adapter = PlaceCardAdapter(placeInfo!!)
        placeCardRecyclerView.adapter = adapter

    }

    private inner class PlaceImageTitleHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val viewPager: ViewPager2 = itemView.findViewById(R.id.view_pager)
        private val categoriesTextView: TextView = itemView.findViewById(R.id.categories)
        private val placeTitleTextView: TextView = itemView.findViewById(R.id.place_title_text_view)
        private val indicator: CircleIndicator3 = itemView.findViewById(R.id.page_indicator)

        fun bind(placeInfo: PlaceResponse) {

            val viewPagerAdapter = ViewPagerAdapter(placeInfo.uri)
            viewPager.adapter = viewPagerAdapter
            viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            indicator.setViewPager(viewPager)

            placeTitleTextView.text = placeInfo.title
            categoriesTextView.text = placeInfo.description
        }

        private inner class PageHolder(view: View) : RecyclerView.ViewHolder(view) {
            val imageView: ImageView = itemView.findViewById<View>(R.id.item_image) as ImageView

            fun bind(uri: Uri) {
                Glide
                    .with(this@PlaceCardFragment)
                    .load(uri)
                    .centerCrop()
                    .placeholder(R.color.gray)
                    .into(imageView)
            }
        }

        private inner class ViewPagerAdapter(val images: List<Uri>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

            override fun onCreateViewHolder(
                parent: ViewGroup,
                viewType: Int
            ): RecyclerView.ViewHolder {
                return PageHolder(layoutInflater.inflate(R.layout.view_pager_item, parent, false))

            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val holderCustom = holder as PageHolder
                holderCustom.bind(images[position])
            }

            override fun getItemCount() = images.size

        }
    }

    private inner class PlaceDescriptionReviewsHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val descriptionTextView: TextView = itemView.findViewById(R.id.place_description_text_view)

        fun bind(placeInfo: PlaceResponse) {
            descriptionTextView.text = "Что-то про место"
        }
    }

    private inner class PlaceCardAdapter(var place: PlaceResponse) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun getItemViewType(position: Int) = position

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return if (viewType == 0) {
                PlaceImageTitleHolder(layoutInflater.inflate(R.layout.place_image_item_view, parent, false))
            } else {
                PlaceDescriptionReviewsHolder(layoutInflater.inflate(R.layout.place_description_reviews_item_view, parent, false))
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (position == 0) {
                val holderCustom = holder as PlaceImageTitleHolder
                holderCustom.bind(place)
            } else {
                val holderCustom = holder as PlaceDescriptionReviewsHolder
                holderCustom.bind(place)
            }
        }

        override fun getItemCount() = 2

    }

    companion object {
        fun newInstance() = PlaceCardFragment()
    }

}