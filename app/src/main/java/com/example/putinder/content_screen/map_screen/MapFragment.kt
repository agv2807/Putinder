package com.example.putinder.content_screen.map_screen

import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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
import java.util.*

class MapFragment : Fragment() {

    private lateinit var mapView: MapView
    private lateinit var addressInfoContainer: LinearLayout
    private lateinit var addressTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var coordsTextView: TextView

    private var swipesViewModel: SwipesViewModel? = null
    private var mapViewModel: MapViewModel? = null

    private var placeInfo: PlaceResponse? = null

    private val inputListener = object : InputListener {
        override fun onMapTap(p0: Map, p1: Point) {
            Log.d("MapFragment", "Location lat:${p1.latitude}, lon:${p1.longitude}")
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            val address = geocoder.getFromLocation(p1.latitude, p1.longitude, 1)
            showAddressInfo(address?.get(0))
        }

        override fun onMapLongTap(p0: Map, p1: Point) {

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        MapKitFactory.initialize(requireContext())

        mapView = view.findViewById(R.id.map_view)
        addressInfoContainer = view.findViewById(R.id.address_info_container)
        addressTextView = view.findViewById(R.id.address_text_view)
        cityTextView = view.findViewById(R.id.city_text_view)
        coordsTextView = view.findViewById(R.id.coords_text_view)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        swipesViewModel = ViewModelProvider(requireActivity())[SwipesViewModel::class.java]
        mapViewModel = ViewModelProvider(requireActivity())[MapViewModel::class.java]

        placeInfo = swipesViewModel?.place

        mapView.map.move(
            CameraPosition(Point(placeInfo!!.lat.toDouble(), placeInfo!!.lon.toDouble()),
                15.0f, 0.0f, 0.0f),
           // CameraPosition(Point(59.5619, 30.1850), 15.0f, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 0.toFloat()),
            null
        )
        val mapPoint = Point(placeInfo!!.lat.toDouble(), placeInfo!!.lon.toDouble())
        //val mapPoint = Point(59.5619, 30.1850)
        val viewPoint = View(requireContext()).apply {
            background = ContextCompat.getDrawable(context, R.drawable.ic_baseline_location_on_24)
        }
        mapView.map.mapObjects.addPlacemark(mapPoint, ViewProvider(viewPoint))

        mapViewModel?.address?.observe(
            viewLifecycleOwner,
            Observer {
                Log.d("MapFragment", it.country + it.city + it.street + it.house)
            }
        )
    }

    override fun onStart() {
        super.onStart()
        MapKitFactory.getInstance().onStart()
        mapView.onStart()

        mapView.map.addInputListener(inputListener)
    }

    override fun onStop() {
        MapKitFactory.getInstance().onStop()
        mapView.onStop()
        super.onStop()
    }

    private fun showAddressInfo(address: Address?) {
        if (address?.subLocality != null) {
            addressInfoContainer.visibility = View.VISIBLE
            addressTextView.text = "${address?.thoroughfare}, ${address?.subThoroughfare}"
            cityTextView.text = "${address?.subLocality}, ${address?.subAdminArea}, ${address?.countryName}"
            coordsTextView.text = "Координаты: ${address?.longitude}, ${address?.latitude}"
        }
    }

    companion object {
        fun newInstance() = MapFragment()
    }

}