package com.example.putinder.content_screen.map_screen

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.putinder.R
import com.example.putinder.content_screen.swipes_screen.models.PlaceResponse
import com.example.putinder.content_screen.swipes_screen.view_model.SwipesViewModel
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.map.Map
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.ui_view.ViewProvider

class MapFragment : Fragment() {

    private lateinit var mapView: MapView

    private var swipesViewModel: SwipesViewModel? = null

    private var placeInfo: PlaceResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        MapKitFactory.initialize(requireContext())

        mapView = view.findViewById(R.id.map_view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipesViewModel = ViewModelProvider(requireActivity())[SwipesViewModel::class.java]

        placeInfo = swipesViewModel?.place

        mapView.map.move(
            CameraPosition(Point(placeInfo!!.lat.toDouble(), placeInfo!!.lon.toDouble()),
                15.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0.toFloat()),
            null
        )
        val mapPoint = Point(placeInfo!!.lat.toDouble(), placeInfo!!.lon.toDouble())
        val viewPoint = View(requireContext()).apply {
            background = ContextCompat.getDrawable(context, R.drawable.ic_baseline_location_on_24)
        }
        mapView.map.mapObjects.addPlacemark(mapPoint, ViewProvider(viewPoint))
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()

        mapView.map.addInputListener(object : InputListener {
            override fun onMapTap(p0: Map, p1: Point) {
                Log.d("MapFragment", "Location lat:${p1.latitude}, lon:${p1.longitude}")
            }

            override fun onMapLongTap(p0: Map, p1: Point) {

            }

        })
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        mapView.onStop()
        super.onStop()
    }

    companion object {
        fun newInstance() = MapFragment()
    }

}