package com.example.putinder.content_screen.swipes_screen.place_card_screen

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
import com.arindicatorview.ARIndicatorView
import com.bumptech.glide.Glide
import com.example.putinder.R
import com.example.putinder.content_screen.swipes_screen.models.PlaceResponse
import com.example.putinder.content_screen.swipes_screen.view_model.SwipesViewModel
import com.google.android.material.transition.MaterialContainerTransform

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

        private val placeImageView: ImageView = itemView.findViewById(R.id.place_image)
        private val categoriesTextView: TextView = itemView.findViewById(R.id.categories)
        private val placeTitleTextView: TextView = itemView.findViewById(R.id.place_title_text_view)
        private val indicator: ARIndicatorView = itemView.findViewById(R.id.page_indicator)
        private val leftView: View = itemView.findViewById(R.id.left_view)
        private val rightView: View = itemView.findViewById(R.id.right_view)

        private var index = 0

        fun bind(placeInfo: PlaceResponse) {

            placeTitleTextView.text = placeInfo.title
            categoriesTextView.text = placeInfo.description

            indicator.numberOfIndicators = placeInfo.uri.size

            Glide
                .with(this@PlaceCardFragment)
                .load(placeInfo.uri[index])
                .centerCrop()
                .placeholder(R.color.gray)
                .into(placeImageView)

            leftView.setOnClickListener {
                if (index > 0) {
                    index--
                    indicator.selectedPosition = index
                    Glide
                        .with(this@PlaceCardFragment)
                        .load(placeInfo.uri[index])
                        .centerCrop()
                        .placeholder(R.color.gray)
                        .into(placeImageView)
                }
            }

            rightView.setOnClickListener {
                if (index < placeInfo.uri.size - 1) {
                    index++
                    indicator.selectedPosition = index
                    Glide
                        .with(this@PlaceCardFragment)
                        .load(placeInfo.uri[index])
                        .centerCrop()
                        .placeholder(R.color.gray)
                        .into(placeImageView)
                }
            }
        }
    }

    private inner class PlaceDescriptionReviewsHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val descriptionTextView: TextView = itemView.findViewById(R.id.place_description_text_view)

        fun bind(placeInfo: PlaceResponse) {
            descriptionTextView.text = "Амин сходил сказал гавно полное не ходи туда"
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