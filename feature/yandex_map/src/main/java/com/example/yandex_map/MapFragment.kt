package com.example.yandex_map

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.data.query_preferences.StoreCoordinates
import com.yandex.mapkit.Animation
import com.yandex.mapkit.MapKitFactory
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.map.CameraPosition
import com.yandex.mapkit.map.InputListener
import com.yandex.mapkit.mapview.MapView
import com.yandex.runtime.ui_view.ViewProvider
import java.util.*
import com.example.ui.R
import com.yandex.mapkit.map.Map
import javax.inject.Inject

class MapFragment @Inject constructor() : Fragment() {

    interface Callbacks {
        fun onMapClosed()
    }

    private lateinit var mapView: MapView
    private lateinit var addressInfoContainer: LinearLayout
    private lateinit var addressTextView: TextView
    private lateinit var cityTextView: TextView
    private lateinit var coordsTextView: TextView

    private var callbacks: Callbacks? = null

    private val inputListener = object : InputListener {
        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onMapTap(p0: Map, p1: Point) {
            val geocoder = Geocoder(requireContext(), Locale.getDefault())
            if (Build.VERSION.SDK_INT >= 33) {
                geocoder.getFromLocation(p1.latitude, p1.longitude, 1
                ) {
                    showAddressInfo(it[0])
                }
            } else {
                geocoder.getFromLocation(p1.latitude, p1.longitude, 1,
                    object : Geocoder.GeocodeListener {

                    override fun onGeocode(p0: MutableList<Address>) {
                        showAddressInfo(p0[0])
                    }

                    override fun onError(errorMessage: String?) {
                        super.onError(errorMessage)
                        Toast.makeText(requireContext(), "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        override fun onMapLongTap(p0: Map, p1: Point) {}
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
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

        val pointLat = StoreCoordinates.getStoredLat(requireContext())
        val pointLon = StoreCoordinates.getStoredLon(requireContext())

        mapView.map.move(
            CameraPosition(
                Point(pointLat.toDouble(), pointLon.toDouble()), 15.0f, 0.0f, 0.0f),
                Animation(Animation.Type.SMOOTH, 0.toFloat()),
                null
            )

        val mapPoint = Point(pointLat.toDouble(), pointLon.toDouble())
        val viewPoint = View(requireContext()).apply {
            background = ContextCompat.getDrawable(context, R.drawable.ic_baseline_location_on_24)
        }

        mapView.map.mapObjects.addPlacemark(mapPoint, ViewProvider(viewPoint))
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

    @SuppressLint("SetTextI18n")
    private fun showAddressInfo(address: Address?) {
        if (address?.subLocality != null) {
            addressInfoContainer.visibility = View.VISIBLE
            addressTextView.text = "${address.thoroughfare}, ${address.subThoroughfare}"
            cityTextView.text = "${address.subLocality}, ${address.subAdminArea}, ${address.countryName}"
            coordsTextView.text = "Координаты: ${address.longitude}, ${address.latitude}"
        }
    }

    companion object {
        fun newInstance() = MapFragment()
    }

}