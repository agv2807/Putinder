package com.example.putinder.content_screen.swipes_screen.view

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.arindicatorview.ARIndicatorView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.putinder.R
import com.example.putinder.content_screen.swipes_screen.models.PlaceResponse
import com.example.putinder.content_screen.swipes_screen.view_model.SwipesViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.yuyakaido.android.cardstackview.*

class SwipesFragment : Fragment(), CardStackListener {

    interface Callbacks {
        fun onFabPressed()
        fun onOpenCardButtonPressed(view: View)
    }

    private var callbacks: Callbacks? = null

    private lateinit var cardStackView: CardStackView
    private lateinit var loader: ProgressBar

    private var swipesViewModel: SwipesViewModel? = null

    private var adapter: CardStackAdapter = CardStackAdapter(emptyList())

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_swipes, container, false)

        cardStackView = view.findViewById(R.id.card_stack_view)
        loader = view.findViewById(R.id.loader)
        val layoutManager = CardStackLayoutManager(requireContext(), this).apply {
            setSwipeableMethod(SwipeableMethod.AutomaticAndManual)
            setOverlayInterpolator(LinearInterpolator())
        }
        cardStackView.layoutManager = layoutManager
        cardStackView.adapter = adapter

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipesViewModel = ViewModelProvider(requireActivity())[SwipesViewModel::class.java]

        swipesViewModel?.places?.observe(
            viewLifecycleOwner,
            Observer {
                adapter = CardStackAdapter(it)
                cardStackView.adapter = adapter
                loader.visibility = View.GONE
            }
        )
    }

    override fun onCardDragging(direction: Direction?, ratio: Float) {
    }

    override fun onCardSwiped(direction: Direction?) {
    }

    override fun onCardRewound() {
    }

    override fun onCardCanceled() {
    }

    override fun onCardAppeared(view: View?, position: Int) {
    }

    override fun onCardDisappeared(view: View?, position: Int) {

    }

    private inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val image: ImageView = itemView.findViewById(R.id.image)
        private val titleTextView: TextView = itemView.findViewById(R.id.title_text_view)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.description_text_view)
        private val fab: FloatingActionButton = itemView.findViewById(R.id.fab)
        private val leftView: View = itemView.findViewById(R.id.left_view)
        private val rightView: View = itemView.findViewById(R.id.right_view)
        private val pageIndicator: ARIndicatorView = itemView.findViewById(R.id.page_indicator)


        private var index = 0

        fun bind(placeInfo: PlaceResponse) {
            titleTextView.text = placeInfo.title
            descriptionTextView.text = placeInfo.description
            if (placeInfo.uri.isNotEmpty()) {
                Glide
                    .with(this@SwipesFragment)
                    .load(placeInfo.uri[index])
                    .centerCrop()
                    .placeholder(R.color.gray)
                    .into(image)
            }

            pageIndicator.numberOfIndicators = placeInfo.uri.size

            fab.setOnClickListener {
                swipesViewModel?.place = placeInfo
                callbacks?.onFabPressed()
            }

            titleTextView.setOnClickListener {
                swipesViewModel?.place = placeInfo
                callbacks?.onOpenCardButtonPressed(image)
            }

            leftView.setOnClickListener {
                if (index > 0) {
                    index--
                    pageIndicator.selectedPosition = index
                    Glide
                        .with(this@SwipesFragment)
                        .load(placeInfo.uri[index])
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .centerCrop()
                        .placeholder(R.color.gray)
                        .into(image)
                }
            }

            rightView.setOnClickListener {
                if (index < placeInfo.uri.size - 1) {
                    index++
                    pageIndicator.selectedPosition = index
                    Glide
                        .with(this@SwipesFragment)
                        .load(placeInfo.uri[index])
                        .centerCrop()
                        .placeholder(R.color.gray)
                        .into(image)
                }
            }
        }
    }

    private inner class CardStackAdapter(private val items: List<PlaceResponse>) : RecyclerView.Adapter<ItemHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
            val view = layoutInflater.inflate(R.layout.card_view_place, parent, false)
            return ItemHolder(view)
        }

        override fun onBindViewHolder(holder: ItemHolder, position: Int) {
            val placeInfo = items[position]
            holder.bind(placeInfo)
        }

        override fun getItemCount() = items.size
    }

    companion object {
        fun newInstance() = SwipesFragment()
    }

}